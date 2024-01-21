package com.justice.insight.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.justice.insight.R;
import com.justice.insight.guide.GuideActivity;

public class SplashActivity extends AppCompatActivity {

    public Intent guide_opener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        guide_opener = new Intent();
        blockss();
       button_initialize();
    }
    public void blockss()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }
    public void button_initialize(){
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide_open();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finishAffinity();
            }
        });
    }

    public void guide_open(){
        guide_opener.setClass(getApplicationContext(), GuideActivity.class);
        startActivity(guide_opener);
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}