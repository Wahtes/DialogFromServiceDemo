package com.example.hqy.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ServiceDemo";
    Button buttonStart, buttonStop, buttonActivityStart, buttonActivityStop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 调整dialog大小
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.95
        getWindow().setAttributes(p);     //设置生效



        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        buttonActivityStart = findViewById(R.id.buttonActivityStart);
        buttonActivityStop = findViewById(R.id.buttonActivityStop);
        buttonActivityStart.setOnClickListener(this);
        buttonActivityStop.setOnClickListener(this);
    }

    public void onClick(View src) {
        switch (src.getId()) {
            case R.id.buttonStart:
                Log.i(TAG, "onClick: starting service");
                startService(new Intent(this, MyService.class));
                break;
            case R.id.buttonStop:
                Log.i(TAG, "onClick: stopping service");
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.buttonActivityStart:
                Log.i(TAG, "onClick: starting activity");
                startActivity(new Intent(this, NewActivity.class));
                break;
            case R.id.buttonActivityStop:
                Log.i(TAG, "onClick: stopping activity");
//                finishActivity(new Intent(this, MyService.class));
                break;
        }
    }

}
