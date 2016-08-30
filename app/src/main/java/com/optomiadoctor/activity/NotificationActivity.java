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
import com.optomiadoctor.adapter.NotificationAdapter;
import com.optomiadoctor.model.NotificationModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class NotificationActivity extends AppCompatActivity {
    ListView listView;
    NotificationAdapter adapter;
    NotificationModel model;
    ArrayList<NotificationModel> arrayList = new ArrayList<NotificationModel>();
    private SharedPreferences objSharedPreferences;
    private SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    AlertDialog loadDialog;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getId();
        notificationService();
    }

    private void getId() {
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        loadDialog = new SpotsDialog(NotificationActivity.this);
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(null);
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
        txtToolbarTitle.setText("Notification");
    }

    private void notificationService() {

        try {
            loadDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
            requestQueue.getCache().clear();
            if (OptomiaDoctorConstant.isNetworkAvailable(NotificationActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.NOTOFICATION_URL, new GetResponse(), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("emp_id", objSharedPreferences.getString("empid", ""));
                        map.put("access_token", objSharedPreferences.getString("access_token", ""));
                        map.put("user_type",objSharedPreferences.getString("employee_type", ""));
                        return map;
                    }
                };
                requestQueue.add(request);
            } else {
                loadDialog.dismiss();
                OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog.dismiss();
            finish();
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
        }
    }


    class GetResponse implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            System.out.println("NotificationResponse = " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                JSONArray jsonArray = jsonResult.optJSONArray("data");

                if (jsonResult.getString("status").equalsIgnoreCase("success")) {

                    for (int loopCount = 0; loopCount < jsonArray.length(); loopCount++) {
                        model = new NotificationModel(jsonArray.getJSONObject(loopCount).getString("notification"),
                                jsonArray.getJSONObject(loopCount).getString("date_created"),
                                "");
                        arrayList.add(model);
                    }
                    adapter = new NotificationAdapter(NotificationActivity.this, arrayList);
                    listView.setAdapter(adapter);
                    loadDialog.dismiss();
                } else {
                    loadDialog.dismiss();
                    OptomiaDoctorConstant.showToast(NotificationActivity.this, "Data not available");
                    finish();
                }
            } catch (JSONException e) {
                OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
                finish();
                e.printStackTrace();
                loadDialog.dismiss();
            }
        }
    }

    class GetError implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            loadDialog.dismiss();
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
            finish();
        }
    }
}