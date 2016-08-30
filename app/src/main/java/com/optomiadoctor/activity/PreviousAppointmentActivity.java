package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.costum.android.widget.PullAndLoadListView;
import com.optomiadoctor.R;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.optomiadoctor.adapter.TodaysAppointmentAdapter;
import com.optomiadoctor.model.AppointmentModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class PreviousAppointmentActivity extends ListActivity {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    int intPullCount = 1;
    int intArraySize = 0;
    AlertDialog dialogLoading;
    ArrayList<AppointmentModel> arrAppointment = new ArrayList<AppointmentModel>();
    AppointmentModel objAppointmentModel;
    TodaysAppointmentAdapter objAdapter;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;
    Typeface objTypefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_appointment);
        init();
    }

    private void init() {
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        dialogLoading = new SpotsDialog(this);
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
        if (objSharedPreferences.getString("appointment_flag", "").equals("histroy")) {
            txtToolbarTitle.setText("Pateints Appointment Histroy");
        } else {
            txtToolbarTitle.setText("Pateints Uppcoming Appointment");
        }

        if (OptomiaDoctorConstant.isNetworkAvailable(PreviousAppointmentActivity.this)) {
            getDataAppointment(1);
        } else {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
        }

        ((PullAndLoadListView) getListView())
                .setOnRefreshListener(new OnRefreshListener() {
                    public void onRefresh() {
                        intPullCount = 1;
                        intArraySize = 0;
                        if (OptomiaDoctorConstant.isNetworkAvailable(PreviousAppointmentActivity.this)) {
                            getDataAppointment(intPullCount);
                        } else {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                        }

                        OptomiaDoctorConstant.printMessage("REFRSH :::", "REFRSH");
                        OptomiaDoctorConstant.printMessage("REFRSH :::", intArraySize);
                    }
                });
        ((PullAndLoadListView) getListView())
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    public void onLoadMore() {
                        OptomiaDoctorConstant.printMessage("REFRSH :::", "LOADMORE");
                        intPullCount++;
                        intArraySize = 0;
                        intArraySize = arrAppointment.size();
                        if (OptomiaDoctorConstant.isNetworkAvailable(PreviousAppointmentActivity.this)) {
                            getDataAppointment(intPullCount);
                        } else {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                        }

                    }
                });
    }

    private void getDataAppointment(final int intPullCount) {
        try {
            String strURL;
            RequestQueue requestQueue = Volley.newRequestQueue(PreviousAppointmentActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (objSharedPreferences.getString("appointment_flag", "").equals("upcoming")) {
                strURL = OptomiaDoctorConstant.UPCOMMING_APPOINTMENT_URL;
            } else {
                strURL = OptomiaDoctorConstant.PREVIOUS_APPOINTMENT_URL;
            }

            if (OptomiaDoctorConstant.isNetworkAvailable(PreviousAppointmentActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, strURL, new GetResponse("Previous", intPullCount), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("emp_id", objSharedPreferences.getString("empid", ""));
                        map.put("page", String.valueOf(intPullCount));
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

    class GetResponse implements Response.Listener<String> {
        String strType;
        int intPullCount;

        public GetResponse(String Type, int count) {
            this.strType = Type;
            this.intPullCount = count;
        }

        @Override
        public void onResponse(String strResponse) {
            OptomiaDoctorConstant.printMessage("Result:::", strResponse);

            if (strType.equals("Previous")) {
                dialogLoading.dismiss();
                JSONObject jsonObject;
                JSONObject resultObject;
                JSONArray jsonArray = null;
                if (intPullCount == 1) {
                    arrAppointment.clear();
                }
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        jsonArray = resultObject.getJSONArray("app_array");
                        for (int intLoopCount = 0; intLoopCount < jsonArray.length(); intLoopCount++) {
                            objAppointmentModel = new AppointmentModel(jsonArray.getJSONObject(intLoopCount).getString("app_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("patient_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_date").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_time").toString(), jsonArray.getJSONObject(intLoopCount).getString("first_name").toString(), jsonArray.getJSONObject(intLoopCount).getString("last_name").toString());
                            arrAppointment.add(objAppointmentModel);
                        }
                        objAdapter = new TodaysAppointmentAdapter(PreviousAppointmentActivity.this, arrAppointment);
                        setListAdapter(objAdapter);
                        objAdapter.notifyDataSetChanged();

                        if (intPullCount == 1) {

                            // ((PullAndLoadListView) getListView()).setSelection(1);
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                        } else {
                            OptomiaDoctorConstant.printMessage("IN SIZE ", intArraySize);
                             ((PullAndLoadListView) getListView()).setSelection(intArraySize);
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                        }
                    } else {
                        ((PullAndLoadListView) getListView()).onRefreshComplete();
                        ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                       /* if (intPullCount == 1) {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "No data found");
                            finish();
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                            //((PullAndLoadListView) getListView()).setSelection(1);
                        } else {
                             ((PullAndLoadListView) getListView()).setSelection(intArraySize);
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                        }*/
                    }

                } catch (Exception e) {
                    finish();
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