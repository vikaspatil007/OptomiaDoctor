package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.optomiadoctor.R;
import com.optomiadoctor.adapter.HistoryAdapter;
import com.optomiadoctor.model.AppointmentModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class HistoryActivity extends ListActivity {
    ListView lvTodaysAppointment;
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    AlertDialog dialogLoading;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;
    ArrayList<AppointmentModel> arrHistoryModel = new ArrayList<AppointmentModel>();
    AppointmentModel objHistoryModel;
    HistoryAdapter objAdapter;
    int intPullCount = 1;
    int intArraySize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
        txtToolbarTitle.setText("History");
        lvTodaysAppointment = (ListView) findViewById(R.id.lvtodays_app);

        if (OptomiaDoctorConstant.isNetworkAvailable(HistoryActivity.this)) {
            getHistory(1);
        } else {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
        }

        ((PullAndLoadListView) getListView())
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
                    public void onRefresh() {
                        intPullCount = 1;
                        intArraySize = 0;
                        if (OptomiaDoctorConstant.isNetworkAvailable(HistoryActivity.this)) {
                            getHistory(intPullCount);
                        } else {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                        }
                        OptomiaDoctorConstant.printMessage("REFRSH :::", "REFRSH");
                        OptomiaDoctorConstant.printMessage("REFRSH :::", intArraySize);
                    }
                });
        ((PullAndLoadListView) getListView())
                .setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {
                    public void onLoadMore() {
                        OptomiaDoctorConstant.printMessage("REFRSH :::", "LOADMORE");
                        intPullCount++;
                        intArraySize = 0;
                        intArraySize = arrHistoryModel.size();
                        if (OptomiaDoctorConstant.isNetworkAvailable(HistoryActivity.this)) {
                            getHistory(intPullCount);
                        } else {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                        }
                    }
                });
    }


    private void getHistory(final int intPullCount) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();

            if (OptomiaDoctorConstant.isNetworkAvailable(HistoryActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.HISTORY_APPOINTMENT_URL, new GetResponse("History", intPullCount), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("emp_id", objSharedPreferences.getString("empid", ""));
                        map.put("page", String.valueOf(intPullCount));
                        map.put("visiting_type", objSharedPreferences.getString("visiting_type",""));
                        map.put("employee_type", objSharedPreferences.getString("employee_type",""));
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
        int intPullCount;

        public GetResponse(String Type, int count) {
            this.strType = Type;
            this.intPullCount = count;
        }

        @Override
        public void onResponse(String strResponse) {
            OptomiaDoctorConstant.printMessage("Result:::", strResponse);
            if (strType.equals("History")) {
                dialogLoading.dismiss();
                JSONObject jsonObject;
                JSONObject resultObject;
                JSONArray jsonArray = null;
                if (intPullCount == 1) {
                    arrHistoryModel.clear();
                }
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        jsonArray = resultObject.getJSONArray("app_array");
                        for (int intLoopCount = 0; intLoopCount < jsonArray.length(); intLoopCount++) {
                            objHistoryModel = new AppointmentModel(jsonArray.getJSONObject(intLoopCount).getString("app_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("patient_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_date").toString(), jsonArray.getJSONObject(intLoopCount).getString("approved_time").toString(), jsonArray.getJSONObject(intLoopCount).getString("first_name").toString(), jsonArray.getJSONObject(intLoopCount).getString("last_name").toString(),jsonArray.getJSONObject(intLoopCount).getString("status").toString());
                            arrHistoryModel.add(objHistoryModel);
                        }
                        objAdapter = new HistoryAdapter(HistoryActivity.this, arrHistoryModel);
                        setListAdapter(objAdapter);
                        objAdapter.notifyDataSetChanged();
                        ((PullAndLoadListView) getListView()).onRefreshComplete();
                        ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                        if (intPullCount == 1) {
                            // ((PullAndLoadListView) getListView()).setSelection(1);
                        } else {
                            OptomiaDoctorConstant.printMessage("IN SIZE ", intArraySize);
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                             ((PullAndLoadListView) getListView()).setSelection(intArraySize);
                        }
                    } else {
                        ((PullAndLoadListView) getListView()).onRefreshComplete();
                        ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                        if (intPullCount == 1) {
                            OptomiaDoctorConstant.showToast(getApplicationContext(), "No data found");
                            finish();
                            //((PullAndLoadListView) getListView()).setSelection(1);
                        } else {
                            ((PullAndLoadListView) getListView()).onRefreshComplete();
                            ((PullAndLoadListView) getListView()).onLoadMoreComplete();
                             ((PullAndLoadListView) getListView()).setSelection(intArraySize);
                        }
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