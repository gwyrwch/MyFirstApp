package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.annotation.SuppressLint;


import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSIONS_REQUEST_DEVICE_ID = 0;
    View mainLayout;
    TextView deviceIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main_layout);

        TextView versionName = findViewById(R.id.textName);
        versionName.setText("version : " + BuildConfig.VERSION_NAME);
        deviceIdTextView = findViewById(R.id.textId);
        deviceIdTextView.setText("id: ");

        showDeviceId();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_DEVICE_ID) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Snackbar.make(mainLayout, R.string.device_id_permission_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();

                outputDeviceId();
            } else {
                Snackbar.make(mainLayout, R.string.device_id_permission_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private void showDeviceId() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            outputDeviceId();
        } else {
            requestIdPermission();
        }
    }

    private void requestIdPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(mainLayout, R.string.device_id_access_required, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            PERMISSIONS_REQUEST_DEVICE_ID);
                }
            }).show();
        } else {
            // самый первый запуск, когда еще ни разу не было deny
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_DEVICE_ID);
        }
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private void outputDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // security check skipped because it has already been checked
        assert telephonyManager != null;
        deviceIdTextView.setText(String.format("id : %s", telephonyManager.getDeviceId()));
    }

}
