package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optomiadoctor.R;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class AppointmentDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    LinearLayout llMainContainer, llTodaysContainer, llTextTodays, llSecondMainContainer, llPreviousContainer, llUpcomingContainer;
    TextView txtSetTodaysDate, txtTitleToday, txtAppointmentCount, txtTitleNext, txtTitlePreious;
    AlertDialog dialogLoading;
    ImageView imgToolbarBack;
    TextView txtToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_dashboard);

        init();
        initListener();
        getTodaysCount();
    }


    private void init() {
        dialogLoading = new SpotsDialog(this);
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        imgToolbarBack = (ImageView) findViewById(R.id.img_back);
        imgToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_right,
                        R.anim.slide_out_right);
            }
        });

        txtToolbarTitle = (TextView) findViewById(R.id.txt_heading);
        txtToolbarTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.050));
        txtToolbarTitle.setTypeface(objTypefaceRegular);
        txtToolbarTitle.setText("Appointment's Dashboard");
        Calendar objCalendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd");
        String strFormattedDate = df.format(objCalendar.getTime());
        llMainContainer = (LinearLayout) findViewById(R.id.ll_maincontainer_app_dash);
        llMainContainer.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));

        llTodaysContainer = (LinearLayout) findViewById(R.id.ll_today_container);
        llTodaysContainer.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));

        txtSetTodaysDate = (TextView) findViewById(R.id.txt_settodays_date);
        txtSetTodaysDate.setPadding((int) (objSharedPreferences.getInt("width", 0) / 50), (int) (objSharedPreferences.getInt("width", 0) / 94),
                (int) (objSharedPreferences.getInt("width", 0) / 50), (int) (objSharedPreferences.getInt("width", 0) / 94));
        txtSetTodaysDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtSetTodaysDate.setTypeface(objTypefaceRegular, Typeface.BOLD);
        txtSetTodaysDate.setText(strFormattedDate);

        llTextTodays = (LinearLayout) findViewById(R.id.ll_txt_todays);
        llTextTodays.setPadding((int) (objSharedPreferences.getInt("width", 0) / 44), 0, (int) (objSharedPreferences.getInt("width", 0) / 44), 0);

        txtTitleToday = (TextView) findViewById(R.id.txt_todays_title);
        txtTitleToday.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        txtTitleToday.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtTitleToday.setTypeface(objTypefaceRegular, Typeface.BOLD);

        txtAppointmentCount = (TextView) findViewById(R.id.txt_appointment_count);
        txtAppointmentCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtAppointmentCount.setTypeface(objTypefaceRegular, Typeface.BOLD);

        llSecondMainContainer = (LinearLayout) findViewById(R.id.ll_second_container);
        llSecondMainContainer.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));

        txtTitleNext = (TextView) findViewById(R.id.txt_title_nextapp);
        txtTitleNext.setPadding((int) (objSharedPreferences.getInt("width", 0) / 64), (int) (objSharedPreferences.getInt("width", 0) / 25),
                0, (int) (objSharedPreferences.getInt("width", 0) / 25));
        txtTitleNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtTitleNext.setTypeface(objTypefaceRegular, Typeface.BOLD);

        txtTitlePreious = (TextView) findViewById(R.id.txt_title_previousapp);
        txtTitlePreious.setPadding((int) (objSharedPreferences.getInt("width", 0) / 64), (int) (objSharedPreferences.getInt("width", 0) / 25),
                (int) (objSharedPreferences.getInt("width", 0) / 45), (int) (objSharedPreferences.getInt("width", 0) / 25));
        txtTitlePreious.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtTitlePreious.setTypeface(objTypefaceRegular, Typeface.BOLD);

        llPreviousContainer = (LinearLayout) findViewById(R.id.ll_previous_container);
        llUpcomingContainer = (LinearLayout) findViewById(R.id.ll_upcoming_container);
    }

    private void initListener() {
        llPreviousContainer.setOnClickListener(this);
        llUpcomingContainer.setOnClickListener(this);
    }

    private void getTodaysCount() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(AppointmentDashboardActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(AppointmentDashboardActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.COUNT_URL, new GetResponse("Todays"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("emp_id", objSharedPreferences.getString("empid", ""));
                        map.put("access_token", objSharedPreferences.getString("access_token",""));
                        OptomiaDoctorConstant.printMessage("VALUE:::", map.toString());
                        return map;
                    }
                };
                requestQueue.add(request);
            } else {
                dialogLoading.dismiss();
                OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
            }
        } catch (Exception e) {
            dialogLoading.dismiss();
            e.printStackTrace();
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
        }
    }

    @Override
    public void onClick(View v) {
        Intent objIntent;
        switch (v.getId()) {
            case R.id.ll_today_container:
                objIntent = new Intent(this, TodaysAppointmentActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
            case R.id.ll_previous_container:
                editor.putString("appointment_flag", "histroy").commit();
                objIntent = new Intent(this, PreviousAppointmentActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
            case R.id.ll_upcoming_container:
                editor.putString("appointment_flag", "upcoming").commit();
                objIntent = new Intent(this, PreviousAppointmentActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
        }
    }

    class GetResponse implements Response.Listener<String> {
        String strType;

        public GetResponse(String Type) {
            this.strType = Type;
        }

        @Override
        public void onResponse(String strResponse) {
            OptomiaDoctorConstant.printMessage("Result:::", strResponse);

            if (strType.equals("Todays")) {
                JSONObject jsonObject;
                JSONObject resultObject;
                dialogLoading.dismiss();
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        if (!resultObject.getString("count").equals("0")) {
                            txtAppointmentCount.setText(resultObject.getString("count"));
                            llTodaysContainer.setOnClickListener(AppointmentDashboardActivity.this);
                        } else {
                            txtAppointmentCount.setText("0");
                            llTodaysContainer.setOnClickListener(null);
                        }
                    } else {
                        txtAppointmentCount.setText("0");
                        llTodaysContainer.setOnClickListener(null);
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                    e.printStackTrace();
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
                }
            }
        }
    }

    class GetError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
            dialogLoading.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_right,
                R.anim.slide_out_right);
    }
}
