package com.justice.insight.welcome_checker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.justice.insight.authenticate.LoginActivity;
import com.justice.insight.splash.SplashActivity;


public class LauncherActivity extends AppCompatActivity {

    int check;
    private SharedPreferences SavedInfo;
    public Intent Statement_opener = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checker();
        checker_background();
    }
    public void checker(){
        SavedInfo = getSharedPreferences("SavedInfo", Activity.MODE_PRIVATE);
    }
    public void checker_background(){
        if(SavedInfo.getString("OnceOpen","").equals("Yes")){
            Statement_opener.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(Statement_opener);
        }else {
            SavedInfo.edit().putString("OnceOpen","Yes").commit();
            Statement_opener.setClass(getApplicationContext(), SplashActivity.class);
            startActivity(Statement_opener);
        }
    }
}