package com.example.fbuddybluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;

    private Set<BluetoothDevice> devices;
    ImageView mblueiv;
    TextView mstatus, pairedtv;
    Button monbtn;
    Button moffbtn;
    Button mdisco;
    Button mpaired;
    private BluetoothAdapter mblueadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        mblueiv = findViewById(R.id.bluepic);
        mstatus = findViewById(R.id.status);
        monbtn = findViewById(R.id.onbtn);
        moffbtn = findViewById(R.id.offbtn);
        mdisco = findViewById(R.id.disco);
        mpaired = findViewById(R.id.paired);
        pairedtv = findViewById(R.id.pairedtv);
        mblueadapter = BluetoothAdapter.getDefaultAdapter();

        // Check Bluetooth availability
        if (mblueadapter == null) {
            mstatus.setText("BLUETOOTH NOT AVAILABLE");
        } else {
            mstatus.setText("BLUETOOTH AVAILABLE");
        }

        // Set Bluetooth icon based on status
        updateBluetoothIcon();

        // Enable Bluetooth
        monbtn.setOnClickListener(v -> enableBluetooth());

        // Disable Bluetooth
        moffbtn.setOnClickListener(v -> disableBluetooth());

        // Make device discoverable
        mdisco.setOnClickListener(v -> makeDiscoverable());

        // Show paired devices
        mpaired.setOnClickListener(v -> showPairedDevices());
    }

    private void updateBluetoothIcon() {
        if (mblueadapter.isEnabled()) {
            mblueiv.setImageResource(R.drawable.bluetooth_on);
        } else {
            mblueiv.setImageResource(R.drawable.bluetooth_off);
        }
    }

    private void enableBluetooth() {
        if (mblueadapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {
            if (!mblueadapter.isEnabled()) {
                // Request permission if needed
                if (checkBluetoothPermission()) {
                    Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
                } else {
                    requestBluetoothPermission();
                }
            }
        }
    }

    private void disableBluetooth() {
        if (mblueadapter.isEnabled()) {
            if (checkBluetoothPermission()) {
                mblueadapter.disable();
                showToast("Turning off Bluetooth");
                updateBluetoothIcon();
            } else {
                requestBluetoothPermission();
            }
        } else {
            showToast("Bluetooth is already off");
        }
    }

    private void makeDiscoverable() {
        if (!mblueadapter.isEnabled()) {
            showToast("Turning on Bluetooth to make discoverable");
            enableBluetooth();
        }
        Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivityForResult(discoverIntent, REQUEST_DISCOVER_BT);
    }

    private void showPairedDevices() {
        if (checkBluetoothPermission()) {
            Set<BluetoothDevice> pairedDevices = mblueadapter.getBondedDevices();
            pairedtv.setText("Paired Devices:");
            for (BluetoothDevice device : pairedDevices) {
                pairedtv.append("\nDevice: " + device.getName() + ", " + device);
            }
        } else {
            requestBluetoothPermission();
        }
    }

    // Check Bluetooth permissions for Android 12+
    private boolean checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                showToast("Bluetooth is on");
                updateBluetoothIcon();
            } else {
                showToast("Failed to enable Bluetooth");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Bluetooth permission granted");
            } else {
                showToast("Bluetooth permission denied");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
