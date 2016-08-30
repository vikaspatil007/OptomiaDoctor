package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.optomiadoctor.R;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtAppointment, txtAddPatient, txtProfile, txtHistory;
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    ImageView rlIcon1, rlIcon2, rlIcon3, rlIcon4;
    Dialog dialog;
    int start_date = 0;
    int start_month = 0;
    int start_year = 0;
    AlertDialog dialogLoading;
    FloatingActionMenu rightLowerMenu;
    AlertDialog.Builder builder;
    String strAccessToken;
    JSONObject jsonObject;
    JSONObject resultObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        floatingTest();
        initListener();
    }

    private void floatingTest() {
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.plus_menu));
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .build();
        rightLowerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (objSharedPreferences.getInt("width", 0) / 7), (int) (objSharedPreferences.getInt("width", 0) / 7));
        rLSubBuilder.setLayoutParams(params);

        rlIcon1 = new ImageView(this);
        rlIcon2 = new ImageView(this);
        rlIcon3 = new ImageView(this);
        rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.setting));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.block));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.sign_out));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.notifi));
        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        if (objSharedPreferences.getString("employee_type", "").equals("1")) {
            rightLowerMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                    .attachTo(rightLowerButton).setRadius(180)
                    .build();
        } else {
            rightLowerMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                    .attachTo(rightLowerButton).setRadius(200)
                    .build();
        }


        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(75);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });
        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(DashboardActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Info");
                builder.setMessage("Are you sure you want to logout this account?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                OptomiaDoctorConstant.printMessage("ACCESS TOKEN BEFOR", objSharedPreferences.getString("access_token", ""));
                                logout();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                builder.show();
                editor.clear();
                editor.commit();

            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(objIntent);
            }
        });
    }


    private void init() {
        dialogLoading = new SpotsDialog(this);
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        strAccessToken = objSharedPreferences.getString("access_token", "");
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        OptomiaDoctorConstant.printMessage("TYPE :::", objSharedPreferences.getString("access_token", ""));
        txtAppointment = (TextView) findViewById(R.id.txt_appointment_dashboard);
        txtAppointment.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtAppointment.setTypeface(objTypefaceRegular, Typeface.BOLD);

        txtAddPatient = (TextView) findViewById(R.id.txt_request_patient_dashboard);
        txtAddPatient.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtAddPatient.setTypeface(objTypefaceRegular, Typeface.BOLD);

        txtProfile = (TextView) findViewById(R.id.txt_profile_dashboard);
        txtProfile.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtProfile.setTypeface(objTypefaceRegular, Typeface.BOLD);

        txtHistory = (TextView) findViewById(R.id.txt_history_dashboard);
        txtHistory.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtHistory.setTypeface(objTypefaceRegular, Typeface.BOLD);
    }

    private void initListener() {
        txtAddPatient.setOnClickListener(this);
        txtProfile.setOnClickListener(this);
        txtHistory.setOnClickListener(this);
        txtAppointment.setOnClickListener(this);
    }

    private void displayDialog() {
        dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.gray_out_dialog);
        LinearLayout llMaincontainerDialog;
        final EditText etFromdate, etTodate;
        Button btnSubmitDialog;
        llMaincontainerDialog = (LinearLayout) dialog.findViewById(R.id.ll_maincontainer_dialog);
        llMaincontainerDialog.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));
        etFromdate = (EditText) dialog.findViewById(R.id.etfromdate_dialog);
        etFromdate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etFromdate.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etFromdate.setTypeface(objTypefaceRegular);

        etTodate = (EditText) dialog.findViewById(R.id.ettodate_dialog);
        etTodate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etTodate.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etTodate.setTypeface(objTypefaceRegular);

        btnSubmitDialog = (Button) dialog.findViewById(R.id.btn_submit_dialog);
        btnSubmitDialog.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btnSubmitDialog.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btnSubmitDialog.setTypeface(objTypefaceRegular);

        etFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(etFromdate);
            }
        });

        etTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(etTodate);
            }
        });
        btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFromdate.getText().toString().trim().isEmpty()) {
                    if (!etTodate.getText().toString().trim().isEmpty()) {
                        cancelAppointment(etFromdate.getText().toString(), etTodate.getText().toString());
                    } else {
                        etTodate.setError("Please select date");
                    }
                } else {
                    etFromdate.setError("Please select date");
                }
            }
        });
        dialog.show();
    }

    private void cancelAppointment(final String strFromdate, final String strTodate) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(DashboardActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.CANCEL_APPOINTMENT, new GetResponse("cancel"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("doc_id", objSharedPreferences.getString("docid", ""));
                        map.put("from_date", strFromdate);
                        map.put("to_date", strTodate);
                        map.put("access_token", strAccessToken);
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

    private void setDate(final EditText editText) {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(DashboardActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                editText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                start_date = dayOfMonth;
                start_month = monthOfYear;
                start_year = year;
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    @Override
    public void onClick(View v) {
        Intent objIntent = null;
        switch (v.getId()) {
            case R.id.txt_appointment_dashboard:
                OptomiaDoctorConstant.printMessage("TYPE:::", objSharedPreferences.getString("employee_type", ""));
                if (objSharedPreferences.getString("employee_type", "").equals("1")) {
                    OptomiaDoctorConstant.showToast(DashboardActivity.this, "Access Denied");
                } else {
                    objIntent = new Intent(this, AppointmentDashboardActivity.class);
                    startActivity(objIntent);
                    overridePendingTransition(R.anim.in,
                            R.anim.out);
                }

                break;
            case R.id.txt_request_patient_dashboard:
                objIntent = new Intent(this, RequestPatientActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
            case R.id.txt_profile_dashboard:
                objIntent = new Intent(this, ProfileActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
            case R.id.txt_history_dashboard:
                objIntent = new Intent(this, HistoryActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
        }
    }

    private void logout() {
        try {
            objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
            OptomiaDoctorConstant.printMessage("ACCESS TOKEN", objSharedPreferences.getString("access_token", ""));
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(DashboardActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.LOGOUT_URL, new GetResponse("LOGOUT"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("access_token", strAccessToken);
                        map.put("os_id", "1");
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
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
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

            if (strType.equals("cancel")) {
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        dialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "Appointment cancel Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DashboardActivity.this, resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                }
            } else if (strType.equals("LOGOUT")) {
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        OptomiaDoctorConstant.printMessage("Result:::", strResponse);
                        editor.clear();
                        editor.commit();
                        Intent objIntent = new Intent(DashboardActivity.this, SplashActivity.class);
                        objIntent.setAction(Intent.ACTION_MAIN);
                        objIntent.addCategory(Intent.CATEGORY_HOME);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        objIntent
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(objIntent);
                        finish();
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                        System.exit(1);
                    }


                } catch (Exception e) {
                    dialogLoading.dismiss();
                    e.printStackTrace();
                    OptomiaDoctorConstant.printMessage("ERROR::", e.toString());
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
                }
            }
            dialogLoading.dismiss();
        }
    }


    class GetError implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong");
            dialogLoading.dismiss();

        }
    }
}
