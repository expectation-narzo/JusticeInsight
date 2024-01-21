package com.justice.insight.prisoner.prisoner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.justice.insight.R;

public class Prisoner_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}