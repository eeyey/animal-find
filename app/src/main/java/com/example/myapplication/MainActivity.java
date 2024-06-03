//package com.example.myapplication;
//
//
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothAssignedNumbers;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.le.ScanCallback;
//import android.bluetooth.le.ScanRecord;
//import android.bluetooth.le.ScanResult;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class MainActivity extends AppCompatActivity {
//    private final boolean LOG_RECORDS = false;
//
//    private BluetoothAdapter bluetoothAdapter;
//
//    private final int REQUEST_ENABLE_BT = 1;
//    private final int REQUEST_FINE_LOCATION = 2;
//
//    private TextView resultText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        resultText = findViewById(R.id.find_device);
//
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT);
//                return;
//            }
//
//            startActivity(enableBtIntent);
//        } else {
//            startBluetoothScan();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        stopBluetoothScan();
//        startBluetoothScan();
//    }
//
//    private void startBluetoothScan() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
//            return;
//        }
//
//        bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
//    }
//
//    private void stopBluetoothScan() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
//            return;
//        }
//
//        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
//    }
//
//    private final ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, @NonNull ScanResult result) {
//            super.onScanResult(callbackType, result);
//
//            if (LOG_RECORDS) logScanResult(result);
//
//            ScanRecord scanRecord = result.getScanRecord();
//            if (scanRecord == null) return;
//
//            byte[] scanRecordBytes = scanRecord.getBytes();
//            if (scanRecordBytes.length <= 3) return;
//
//            byte[] trimmedBytes = Arrays.copyOfRange(scanRecordBytes, 23, scanRecordBytes.length);
//
//            int endIndex = trimmedBytes.length - 1;
//            while (endIndex >= 0 && trimmedBytes[endIndex] == 0) {
//                endIndex--;
//            }
//
//            byte[] resultBytes = Arrays.copyOf(trimmedBytes, endIndex + 1);
//            String url = new String(resultBytes, StandardCharsets.UTF_8);
//
//            String idPattern = "fanim\\.ru/(\\w+)";
//            Pattern pattern = Pattern.compile(idPattern);
//            Matcher matcher = pattern.matcher(url);
//
//            String id = null;
//            if (matcher.find()) {
//                id = matcher.group(1); // Получаем значение группы 1 из регулярного выражения
//
//                Intent intent = new Intent(MainActivity.this, AnimalActivity.class);
//                intent.putExtra("ID_EXTRA", id);
//                startActivity(intent);
//
//                stopBluetoothScan();
//            }
//        }
//    };
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_FINE_LOCATION || requestCode == REQUEST_ENABLE_BT) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startBluetoothScan();
//            } else {
//                Toast.makeText(this, "Permission denied, cannot scan for Bluetooth devices", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//
//    private void logScanResult(ScanResult result) {
//        BluetoothDevice device = result.getDevice();
//
//        Log.i("ScanRecord", "Device Name: " + device.getName());
//        Log.i("ScanRecord", "Device Address: " + device.getAddress());
//
//        ScanRecord scanRecord = result.getScanRecord();
//
//        if (scanRecord != null) {
//            Log.i("ScanRecord", "Advertise Flags: " + scanRecord.getAdvertiseFlags());
//            Log.i("ScanRecord", "Service UUIDs: " + scanRecord.getServiceUuids());
//            Log.i("ScanRecord", "Manufacturer Specific Data: " + scanRecord.getManufacturerSpecificData());
//            Log.i("ScanRecord", "Service Data: " + scanRecord.getServiceData());
//            Log.i("ScanRecord", "Tx Power Level: " + scanRecord.getTxPowerLevel());
//            Log.i("ScanRecord", "Device Name: " + scanRecord.getDeviceName());
//            Log.i("ScanRecord", "Bytes: " + Arrays.toString(scanRecord.getBytes()));
//
//            Log.i("ScanRecord", "END END END");
//
//        } else {
//            Log.i("ScanRecord", "ScanRecord is null");
//
//        }
//    }
//}


package com.example.myapplication;

        import android.bluetooth.BluetoothAdapter;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.pm.PackageManager;
        import android.os.Bundle;

        import android.util.Log;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {
    private final boolean LOG_RECORDS = false;
    private Intent bluetoothService;
    private BluetoothAdapter bluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private final int REQUEST_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "START");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableBluetooth();

        bluetoothService = new Intent(this, BluetoothService.class);
        startService(bluetoothService);

        IntentFilter filter = new IntentFilter("com.example.myapplication.FOUND_DEVICE");
        registerReceiver(receiver, filter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        stopService(bluetoothService);
        startService(bluetoothService);
    }


    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT);
                return;
            }
            startActivity(enableBtIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINE_LOCATION || requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableBluetooth();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra("ID_EXTRA");
            Intent animalIntent = new Intent(MainActivity.this, AnimalActivity.class);
            animalIntent.putExtra("ID_EXTRA", id);
            startActivity(animalIntent);
        }
    };


}


