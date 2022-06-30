package com.example.hqy.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Iterator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class NewActivity extends Activity{

    private static final String TAG = "NEW_ACTIVITY";
    Activity mActivity;
    Handler timeHandler = new Handler();
    Runnable runnable;

    LocationManager locationManager;
    private TextView textView;
    int index = 0;
    double last_lat = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        // 调整dialog大小
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);
        p.width = (int) (d.getWidth() * 0.85);
        getWindow().setAttributes(p);     //设置生效

        mActivity = this;
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                Log.e("Time to finish", new Date().toString());

//                timeHandler.postDelayed(this, 2000);//两次之间的间隔
                mActivity.finish();
            }
        };
        timeHandler.postDelayed(runnable, 7000);

        textView = findViewById(R.id.main_text);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 100);
        } else {
            initLocation();
        }
    }
    @Override
    public void onDestroy() {
        locationManager.removeUpdates(listener);
        super.onDestroy();
    }

    // 设置多少秒一次
    @SuppressLint("MissingPermission")
    void initLocation() {
        Log.i("xml", "权限已取得，开始获取经纬度");
        long minSecond = 1 * 1000;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minSecond, 0, listener);//LocationManager.GPS_PROVIDER
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minSecond, 0, listener);//LocationManager.NETWORK_PROVIDER
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户点击了同意授权
                    initLocation();
                } else {
                    //用户拒绝了授权
                    Toast.makeText(NewActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public Location updateLocation(Location location) {
        String result;
        double lat = 0;
        double lon = 0;
        double speed = 0;
        int stars = 0;

        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            speed = location.getSpeed();
            GpsStatus gpsStatus = locationManager.getGpsStatus(null);

            result = String.format("【%d】%s\n纬度：%f\n经度：%f\n", index++, new Date(), lat, lon); // ""纬度：" + lat + "\n经度：" + lon + "\n";
            Log.i("xml", "显示结果 = " + result);
        } else {
            result = "无法获取经纬度信息";
        }
        if(lat - last_lat != 0) {
            textView.setText(textView.getText() + "GPS Position Changed!\n");
        }
        textView.setText(textView.getText() + result);

//        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        last_lat = lat;
        return location;
    }

    public final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i("xml", "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i("xml", "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i("xml", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateLocation(null);
        }
    };

}