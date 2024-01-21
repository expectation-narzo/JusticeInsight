package com.justice.insight.prisoner.lawyer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.justice.insight.R;

public class Lawyer_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}