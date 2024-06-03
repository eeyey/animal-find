package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothService extends Service {
    private BluetoothAdapter bluetoothAdapter;
    private final boolean LOG_RECORDS = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service", "START");


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startBluetoothScan();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "BIND");

        return null;
    }

    private void startBluetoothScan() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.i("Service", "onScanResult");


//            if (LOG_RECORDS) logScanResult(result);

            ScanRecord scanRecord = result.getScanRecord();
            if (scanRecord == null) return;

            byte[] scanRecordBytes = scanRecord.getBytes();
            if (scanRecordBytes.length <= 3) return;

            byte[] trimmedBytes = Arrays.copyOfRange(scanRecordBytes, 23, scanRecordBytes.length);

            int endIndex = trimmedBytes.length - 1;
            while (endIndex >= 0 && trimmedBytes[endIndex] == 0) {
                endIndex--;
            }

            byte[] resultBytes = Arrays.copyOf(trimmedBytes, endIndex + 1);
            String url = new String(resultBytes, StandardCharsets.UTF_8);

            String idPattern = "fanim\\.ru/(\\w+)";
            Pattern pattern = Pattern.compile(idPattern);
            Matcher matcher = pattern.matcher(url);

            Log.i("URL", url);

            String id = null;
            if (matcher.find()) {
                id = matcher.group(1);

                Intent opeIntent = new Intent("com.example.myapplication.FOUND_DEVICE");
                opeIntent.putExtra("ID_EXTRA", id);
                sendBroadcast(opeIntent);

                NotificationChannel channel = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = new NotificationChannel("TEST_CHANNEL", "TEST DESCR", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Intent intent = new Intent(getApplicationContext(), AnimalActivity.class);
                    intent.putExtra("ID_EXTRA", id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), "TEST_CHANNEL")
                            .setContentTitle("Найдено устройство")
                            .setContentText("Обнаружено устройство " + id)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();

                    notificationManager.notify(42, notification);
                }

                stopBluetoothScan();
            }

        }
    };

    private void logScanResult(ScanResult result) {
        BluetoothDevice device = result.getDevice();

        Log.i("ScanRecord", "Device Name: " + device.getName());
        Log.i("ScanRecord", "Device Address: " + device.getAddress());

        ScanRecord scanRecord = result.getScanRecord();

        if (scanRecord != null) {
            Log.i("ScanRecord", "Advertise Flags: " + scanRecord.getAdvertiseFlags());
            Log.i("ScanRecord", "Service UUIDs: " + scanRecord.getServiceUuids());
            Log.i("ScanRecord", "Manufacturer Specific Data: " + scanRecord.getManufacturerSpecificData());
            Log.i("ScanRecord", "Service Data: " + scanRecord.getServiceData());
            Log.i("ScanRecord", "Tx Power Level: " + scanRecord.getTxPowerLevel());
            Log.i("ScanRecord", "Device Name: " + scanRecord.getDeviceName());
            Log.i("ScanRecord", "Bytes: " + Arrays.toString(scanRecord.getBytes()));

            Log.i("ScanRecord", "END END END");

        } else {
            Log.i("ScanRecord", "ScanRecord is null");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBluetoothScan();
    }

    private void stopBluetoothScan() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }
}
