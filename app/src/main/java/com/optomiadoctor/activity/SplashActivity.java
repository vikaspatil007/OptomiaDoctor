package com.optomiadoctor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.optomiadoctor.R;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    private static final int timeOut = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int intScreenWidth = displayMetrics.widthPixels;
        int intScreenHeight = displayMetrics.heightPixels;

        editor.putInt("width", intScreenWidth);
        editor.putInt("height", intScreenHeight);
        editor.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (objSharedPreferences.getString("Login_flag", "").equals("Yes")) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in,
                            R.anim.out);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in,
                            R.anim.out);
                    finish();
                }
            }
        }, timeOut);
    }
}