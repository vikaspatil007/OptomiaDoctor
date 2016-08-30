package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optomiadoctor.R;
import com.optomiadoctor.adapter.SpinnerAdapter;
import com.optomiadoctor.model.SpecialityType;
import com.optomiadoctor.utils.BarcodeScanner;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RequestPatientActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    LinearLayout objLinearLayoutMain;
    EditText etDate, etRequirment, etRemark, etPatientid;
    TextView txtToTime, txtFromTime;
    Spinner spSurgeryCategory, spSurgerytype, spPriority;
    AlertDialog dialogLoading;
    ArrayList<SpecialityType> arrSurgeryCategory = new ArrayList<SpecialityType>();
    ArrayList<SpecialityType> arrSurgery = new ArrayList<SpecialityType>();
    ArrayList<SpecialityType> arrPriority = new ArrayList<SpecialityType>();
    SpecialityType objSurgeryModel, objTypeName;
    SpinnerAdapter objSpinnerSurgeryCategoryAdapter, objSpinnerSurgeryAdapter, objSpinnerPriorityAdapter;
    int start_date = 0;
    int start_month = 0;
    int start_year = 0;
    int mHour, mMinute;
    Button btnSubmit;
    ImageView imgScan;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_patient);
        init();
        initListener();

        if (OptomiaDoctorConstant.isNetworkAvailable(RequestPatientActivity.this)) {
            getSurgeryCategory();
        } else {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
        }
        setType();
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
        txtToolbarTitle.setText("Referral");

        LinearLayout.LayoutParams paramsEdittext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (objSharedPreferences.getInt("width", 0) * 0.4));

        objLinearLayoutMain = (LinearLayout) findViewById(R.id.ll_maincontainer_request);
        objLinearLayoutMain.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));

        etDate = (EditText) findViewById(R.id.etDate_request);
        etDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etDate.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etDate.setTypeface(objTypefaceRegular);
        etDate.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        spSurgeryCategory = (Spinner) findViewById(R.id.spcategory_surgerytype_request);
        spSurgeryCategory.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.020), (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.020),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));


        spSurgerytype = (Spinner) findViewById(R.id.spSurgerytype_request);
        spSurgerytype.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.020), (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.020),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));


        spPriority = (Spinner) findViewById(R.id.spPriority_request);
        spPriority.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.020), (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.020),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));

        etPatientid = (EditText) findViewById(R.id.et_patientid_request);
        etPatientid.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etPatientid.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etPatientid.setTypeface(objTypefaceRegular);

        imgScan = (ImageView) findViewById(R.id.img_scan_request);
        imgScan.setPadding(0, (int) (objSharedPreferences.getInt("width", 0) / 64),
                0, (int) (objSharedPreferences.getInt("width", 0) / 38));

        txtToTime = (TextView) findViewById(R.id.editTotime);
        txtToTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        txtToTime.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        txtToTime.setTypeface(objTypefaceRegular);

        txtFromTime = (TextView) findViewById(R.id.editfromtime);
        txtFromTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        txtFromTime.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        txtFromTime.setTypeface(objTypefaceRegular);

        etRequirment = (EditText) findViewById(R.id.etRequirment_request);
        etRequirment.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etRequirment.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etRequirment.setTypeface(objTypefaceRegular);
        etRequirment.setLayoutParams(paramsEdittext);

        etRemark = (EditText) findViewById(R.id.etRemark_request);
        etRemark.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etRemark.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etRemark.setTypeface(objTypefaceRegular);
        etRemark.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));
        etRemark.setLayoutParams(paramsEdittext);

        btnSubmit = (Button) findViewById(R.id.btn_submit_request);
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btnSubmit.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btnSubmit.setTypeface(objTypefaceRegular);
    }

    private void initListener() {
        etDate.setOnClickListener(this);
        imgScan.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        txtToTime.setOnClickListener(this);
        txtFromTime.setOnClickListener(this);
    }

    private void setDateFrom() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(RequestPatientActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;

                String strDate = year + "-" + monthOfYear + "-" + dayOfMonth;
                String inputPattern="yyyy-d-mm";
                String outputPattern="yyyy-dd-mm";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                Date date = null;
                String str = null;
                try {
                    date = inputFormat.parse(strDate);
                    str = outputFormat.format(date);
                    etDate.setText(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                start_date = dayOfMonth;
                start_month = monthOfYear;
                start_year = year;
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    protected void onActivityResult(int intRequestCode, int intResultCode,
                                    Intent intentData) {
        switch (intRequestCode) {
            case 100:
                if (intResultCode == RESULT_OK) {
                    Bundle objBundle = intentData.getExtras();
                    int intPos = objBundle.getInt("Pos");
                    String strStatus = objBundle.getString("status");
                    OptomiaDoctorConstant.printMessage("SCAN DATA:::",strStatus);
                    String strData = strStatus;
                    String[] separated = strData.split("-");
                    etPatientid.setText(separated[separated.length-1]);
                }
        }
    }

    private void getSurgeryCategory() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(RequestPatientActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(RequestPatientActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.SURGERY_CATEGORY_URL, new GetResponse("Surgery_Category"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("access_token", objSharedPreferences.getString("access_token", ""));
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

    private void setSurgeryCategory(String strResponse) {
        JSONObject jsonObject;
        JSONObject resultObject;
        JSONArray jsonArray = null;
        arrSurgeryCategory.clear();
        try {
            jsonObject = new JSONObject(strResponse);
            OptomiaDoctorConstant.printMessage("result:::", strResponse.toString());
            resultObject = jsonObject.getJSONObject("result");
            if (resultObject.getString("status").equalsIgnoreCase("success")) {
                jsonArray = resultObject.getJSONArray("data");
                objSurgeryModel = new SpecialityType("-1", "Select Surgery Category");
                arrSurgeryCategory.add(objSurgeryModel);
                editor.putString("Surgery", "Select Surgery Category").commit();

                for (int intLoopCount = 0; intLoopCount < jsonArray.length(); intLoopCount++) {
                    objSurgeryModel = new SpecialityType(jsonArray.getJSONObject(intLoopCount).getString("sur_cat_id").toString(), jsonArray.getJSONObject(intLoopCount).getString("category_name").toString());
                    arrSurgeryCategory.add(objSurgeryModel);
                }
                objSpinnerSurgeryCategoryAdapter = new SpinnerAdapter(this, arrSurgeryCategory, "yes");
                spSurgeryCategory.setAdapter(objSpinnerSurgeryCategoryAdapter);
                spSurgeryCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        editor.putString("Surgery_Cate", arrSurgeryCategory.get(position).getStrCategoryName()).commit();
                        editor.putString("Surgery_Cate_Id", arrSurgeryCategory.get(position).getStrID()).commit();
                        OptomiaDoctorConstant.printMessage("ID:::", arrSurgeryCategory.get(position).getStrID());
                        if (!arrSurgeryCategory.get(position).getStrID().equals("-1")) {
                            spSurgerytype.setVisibility(View.VISIBLE);
                            getSurgery(arrSurgeryCategory.get(position).getStrID());
                        } else {
                            spSurgerytype.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
            }
        } catch (Exception e) {
            System.out.println(e);
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
        }
    }

    private void getSurgery(final String strID) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(RequestPatientActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(RequestPatientActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.SURGERY_URL, new GetResponse("Surgery"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("access_token", objSharedPreferences.getString("access_token", ""));
                        map.put("sur_cat_id", strID);
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

    private void setSurgery(String strResponse) {
        JSONObject jsonObject;
        JSONObject resultObject;
        JSONArray jsonArray = null;
        arrSurgery.clear();
        try {
            jsonObject = new JSONObject(strResponse);
            OptomiaDoctorConstant.printMessage("result:::", strResponse.toString());
            resultObject = jsonObject.getJSONObject("result");
            if (resultObject.getString("status").equalsIgnoreCase("success")) {
                jsonArray = resultObject.getJSONArray("data");
                objSurgeryModel = new SpecialityType("-1", "Select Surgery");
                arrSurgery.add(objSurgeryModel);
                editor.putString("Surgery", "Select Surgery ").commit();

                for (int intLoopCount = 0; intLoopCount < jsonArray.length(); intLoopCount++) {
                    objSurgeryModel = new SpecialityType(jsonArray.getJSONObject(intLoopCount).getString("id").toString(), jsonArray.getJSONObject(intLoopCount).getString("surgery_name").toString());
                    arrSurgery.add(objSurgeryModel);
                }
                objSpinnerSurgeryAdapter = new SpinnerAdapter(this, arrSurgery, "yes");
                spSurgerytype.setAdapter(objSpinnerSurgeryAdapter);
                spSurgerytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        editor.putString("Surgery", arrSurgery.get(position).getStrCategoryName()).commit();
                        editor.putString("Surgery_Id", arrSurgery.get(position).getStrID()).commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
            }
        } catch (Exception e) {
            System.out.println(e);
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
        }
    }

    private void setType() {
        arrPriority.clear();
        try {
            objTypeName = new SpecialityType("0", "Select priority of Surgery");
            arrPriority.add(objTypeName);

            objTypeName = new SpecialityType("1", "Critical");
            arrPriority.add(objTypeName);
            objTypeName = new SpecialityType("2", "High");
            arrPriority.add(objTypeName);
            objTypeName = new SpecialityType("3", "Medium");
            arrPriority.add(objTypeName);
            objTypeName = new SpecialityType("4", "Low");
            arrPriority.add(objTypeName);

            objSpinnerPriorityAdapter = new SpinnerAdapter(this, arrPriority, "yes");
            spPriority.setAdapter(objSpinnerPriorityAdapter);
            spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    editor.putString("Priority", arrPriority.get(position).getStrCategoryName()).commit();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            System.out.println(e);
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Data not available ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_scan_request:
                Intent objIntent = new Intent(this, BarcodeScanner.class);
                startActivityForResult(objIntent, 100);
                break;

            case R.id.etDate_request:
                setDateFrom();
                break;

            case R.id.btn_submit_request:
                OptomiaDoctorConstant.printMessage("SURGERY:::", objSharedPreferences.getString("Surgery", ""));
                if (!etPatientid.getText().toString().trim().isEmpty()) {
                    if (!etDate.getText().toString().isEmpty()) {
                        if (!etRequirment.getText().toString().isEmpty()) {
                            if (!etRemark.getText().toString().isEmpty()) {
                                if (!objSharedPreferences.getString("Surgery", "").equals("Select Surgery")) {
                                    if (!objSharedPreferences.getString("Priority", "").equals("Select priority of Surgery")) {
                                        if (!txtToTime.getText().toString().isEmpty() && !txtFromTime.getText().toString().isEmpty()) {
                                            sendRequest();
                                        } else {
                                            OptomiaDoctorConstant.showToast(this, "Please set Time ");
                                        }
                                    } else {
                                        OptomiaDoctorConstant.showToast(this, "Please select priority surgery");
                                    }
                                } else {
                                    OptomiaDoctorConstant.showToast(this, "Please select Surgery");
                                }
                            } else {
                                etRemark.setError("Enter your remark");
                            }
                        } else {
                            etRequirment.setError("Please enter your requirment");
                        }
                    } else {
                        etDate.setError("Please select date");
                    }
                } else {
                    etPatientid.setError("Please enter patient id");
                }
                break;
            case R.id.editTotime:
                setTime(txtToTime);

                break;
            case R.id.editfromtime:
                setTime(txtFromTime);
                break;
        }

    }

    private void setTime(final TextView txtTime) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void sendRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(RequestPatientActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(RequestPatientActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.SENDREQUEST_URL, new GetResponse("Request"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("patient_id", etPatientid.getText().toString());
                        map.put("doc_id", objSharedPreferences.getString("docid", ""));
                        map.put("required_element", etRequirment.getText().toString());
                        map.put("surgery_type", objSharedPreferences.getString("Surgery_Id", ""));
                        map.put("expected_date", etDate.getText().toString());
                        map.put("priority", objSharedPreferences.getString("Priority", ""));
                        map.put("remark", etRemark.getText().toString());
                        map.put("access_token", objSharedPreferences.getString("access_token", ""));
                        map.put("from_time", txtFromTime.getText().toString());
                        map.put("to_time", txtToTime.getText().toString());
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
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong please try again later");
        }
    }

    class GetResponse implements Response.Listener<String> {
        String strType;

        public GetResponse(String Type) {
            this.strType = Type;
        }

        @Override
        public void onResponse(String strResponse) {
            JSONObject jsonObject;
            JSONObject resultObject;
            if (strType.equals("Surgery")) {
                setSurgery(strResponse);
            } else if (strType.equals("Surgery_Category")) {
                setSurgeryCategory(strResponse);
            } else if (strType.equals("Request")) {
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(RequestPatientActivity.this, "Request Send Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_right,
                                R.anim.slide_out_right);
                    } else {
                        Toast.makeText(RequestPatientActivity.this, resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                }
            }
            dialogLoading.dismiss();
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
