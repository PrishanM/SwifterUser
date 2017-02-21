package com.evensel.swyftr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    private Context context;
    private int locationServicesState=0;
    private int locationCoarseState=0;
    private int writeExternalStorage=0;
    private final List<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startHandler();
        }else{
            locationServicesState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION); // Permission for location services
            locationCoarseState = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION); // Permission for location services
            writeExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if(locationServicesState!=PackageManager.PERMISSION_GRANTED){
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(locationCoarseState!=PackageManager.PERMISSION_GRANTED){
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if(writeExternalStorage!=PackageManager.PERMISSION_GRANTED){
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissionList.size()>0){
                String[] permissionArray = new String[permissionList.size()];
                permissionList.toArray(permissionArray);
                int REQUEST_CODE = 1100;
                requestPermissions(permissionArray, REQUEST_CODE);
            }else{
                startHandler();
            }
        }
    }

    private void startHandler() {
        int SPLASH_TIMEOUT_TIME = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent videoIntent = new Intent(SplashActivity.this,IntroActivity.class);
                startActivity(videoIntent);
                finish();


            }
        }, SPLASH_TIMEOUT_TIME);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startHandler();
                } else {
                    // Permission Denied
                    System.exit(1);
                }
                break;
            default:
                System.exit(1);
                break;
        }
    }
}
