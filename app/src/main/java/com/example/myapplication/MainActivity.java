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


