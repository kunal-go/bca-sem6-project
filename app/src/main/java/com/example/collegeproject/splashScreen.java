package com.example.collegeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;

public class splashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemClock.sleep(1500);

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
