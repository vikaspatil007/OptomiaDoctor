package com.optomiadoctor.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optomiadoctor.R;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

public class DoctorProfileActivity extends AppCompatActivity {
    private SharedPreferences objSharedPreferences;
    private SharedPreferences.Editor editor;
    private Typeface objTypefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        init();

        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(),OptomiaDoctorConstant.FONTREGULAR);
    }

    public void init() {
    }

}
