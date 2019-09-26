package com.example.user.lankabellapps.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.lankabellapps.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
