package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optomiadoctor.R;
import com.optomiadoctor.adapter.TodaysAppointmentAdapter;
import com.optomiadoctor.model.AppointmentModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class TodaysAppointmentActivity extends AppCompatActivity {
    ListView lvTodaysAppointment;
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    AlertDialog dialogLoading;
    ArrayList<AppointmentModel> arrAppointment = new ArrayList<AppointmentModel>();
    AppointmentModel objAppointmentModel;
    TodaysAppointmentAdapter objAdapter;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_appointment);
        init();
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
        txtToolbarTitle.setText("Today's Appointment");

        lvTodaysAppointment = (ListView) findViewById(R.id.lvtodays_app);

        if (OptomiaDoctorConstant.isNetworkAvailable(TodaysAppointmentActivity.this)) {
            getTodaysAppData();
        } else {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
        }

    }

    private void getTodaysAppData() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(TodaysAppointmentActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(TodaysAppointmentActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.TODAYS_APPOINTMENT_URL, new GetResponse("Todays"), new GetError()) {
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
                finish();
            }
        } catch (Exception e) {
            dialogLoading.dismiss();
            e.printStackTrace();
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
            finish();
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
                JSONArray jsonArray = null;
                arrAppointment.clear();
                dialogLoading.dismiss();
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        jsonArray = resultObject.getJSONArray("app_array");
                        for (int intLoopCount = 0; intLoopCount < jsonArray.length(); intLoopCount++) {
                            objAppointmentModel = new AppointmentModel(jsonArray.getJSONObject(intLoopCount).getString("app_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("patient_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_date").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_time").toString(), jsonArray.getJSONObject(intLoopCount).getString("first_name").toString(), jsonArray.getJSONObject(intLoopCount).getString("last_name").toString());
                            arrAppointment.add(objAppointmentModel);
                        }
                        objAdapter = new TodaysAppointmentAdapter(TodaysAppointmentActivity.this, arrAppointment);
                        lvTodaysAppointment.setAdapter(objAdapter);
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                    e.printStackTrace();
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
                    finish();
                }
            }
        }
    }

    class GetError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
            dialogLoading.dismiss();
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_right,
                R.anim.slide_out_right);
    }
}