package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import com.optomiadoctor.R;
import com.optomiadoctor.utils.OptomiaDoctorConstant;
import com.optomiadoctor.utils.ValidationClass;

import org.json.JSONObject;

import java.util.Map;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout llMainContinaer;
    EditText etEmailLogin, etPassLogin;
    Typeface objTypefaceRegular;
    Button btnLogin;
    TextView txtForgetPassword, txtSignUp;
    Dialog dialog;
    private ValidationClass validationClass;
    AlertDialog dialogLoading;
    String strEmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._]+\\.+[a-z]+";
    String PROJECT_NUMBER = "910786379348";
    String strIMEI;
    TelephonyManager objTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);

        init();
        initListener();
    }

    private void init() {
        validationClass = new ValidationClass();
        dialogLoading = new SpotsDialog(this);
        objTelephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        strIMEI = objTelephonyManager.getDeviceId();

        LinearLayout.LayoutParams llMainParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llMainParams.setMargins((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        llMainParams.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams llLoginBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llLoginBtnParams.setMargins(0, (int) (objSharedPreferences.getInt("width", 0) * 0.12), 0, 0);

        llMainContinaer = (LinearLayout) findViewById(R.id.ll_maincontainer_login);
        llMainContinaer.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));
        llMainContinaer.setLayoutParams(llMainParams);

        etEmailLogin = (EditText) findViewById(R.id.etemail_login);
        etEmailLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etEmailLogin.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etEmailLogin.setTypeface(objTypefaceRegular);
        etEmailLogin.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        etPassLogin = (EditText) findViewById(R.id.etpass_login);
        etPassLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etPassLogin.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etPassLogin.setTypeface(objTypefaceRegular);
        etPassLogin.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btnLogin.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btnLogin.setTypeface(objTypefaceRegular);
        btnLogin.setLayoutParams(llLoginBtnParams);

        txtForgetPassword = (TextView) findViewById(R.id.txtforget_login);
        txtForgetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtForgetPassword.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 64),
                (int) (objSharedPreferences.getInt("width", 0) / 20), (int) (objSharedPreferences.getInt("width", 0) / 64));
        txtForgetPassword.setTypeface(objTypefaceRegular);

        txtSignUp = (TextView) findViewById(R.id.txtsign_login);
        txtSignUp.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtSignUp.setPadding((int) (objSharedPreferences.getInt("width", 0) / 20), (int) (objSharedPreferences.getInt("width", 0) / 64),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 64));
        txtSignUp.setTypeface(objTypefaceRegular);

    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        txtForgetPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    private void forgetPassword() {
        // TODO Auto-generated method stub
        RelativeLayout objRelativeLayoutDialog;
        LinearLayout objLinearLayoutEmail;
        TextView objTextViewTitle, objTextViewDesc;
        Button objButtonCancel, objButtonRequest;

        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final EditText objEditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_forgetpass);
        objRelativeLayoutDialog = (RelativeLayout) dialog.findViewById(R.id.dialog_contain_layout);
        objLinearLayoutEmail = (LinearLayout) dialog.findViewById(R.id.layout_email_forgetpassowrd);

        RelativeLayout.LayoutParams dialogParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (objSharedPreferences.getInt("width", 0) / 2));

        dialogParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        dialogParams.setMargins((int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040));

        objRelativeLayoutDialog.setLayoutParams(dialogParams);
        objRelativeLayoutDialog.setBackground(getResources().getDrawable(R.drawable.custom_back_dialog));

        RelativeLayout.LayoutParams EmailLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        EmailLayoutParams.addRule(RelativeLayout.BELOW, R.id.txt_desc_dialog);
        EmailLayoutParams.setMargins((int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040),
                (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        objLinearLayoutEmail.setLayoutParams(EmailLayoutParams);

        objEditTextDialog = (EditText) dialog.findViewById(R.id.edit_user_mail_dialog);

        objTextViewTitle = (TextView) dialog.findViewById(R.id.txt_title_dialog);
        objTextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.050));
        objTextViewTitle.setTypeface(objTypefaceRegular);

        objTextViewDesc = (TextView) dialog.findViewById(R.id.txt_desc_dialog);
        objTextViewDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        objTextViewDesc.setTypeface(objTypefaceRegular);


        objButtonCancel = (Button) dialog.findViewById(R.id.btn_cancel_dialog);
        objButtonCancel.setTypeface(objTypefaceRegular);
        objButtonCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        objButtonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        objButtonRequest = (Button) dialog.findViewById(R.id.btn_send_dialog);
        objButtonRequest.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        objButtonRequest.setTypeface(objTypefaceRegular);

        objButtonRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String strEmail = objEditTextDialog.getText().toString();
                if (OptomiaDoctorConstant.isNetworkAvailable(LoginActivity.this)) {
                    if (!strEmail.isEmpty() && strEmail.matches(strEmailPattern)) {
                        sendForgetPassRequest(strEmail);
                    } else {
                        objEditTextDialog.setError("Invalid Email");
                    }
                } else {
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                }
            }
        });
        dialog.show();
    }

    private void sendForgetPassRequest(final String strEmail) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(LoginActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.FORGET_PASS_URL, new GetResponse("ForgetPass"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("email_addr", strEmail);
                        map.put("app_id", OptomiaDoctorConstant.APPID);
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
        switch (v.getId()) {
            case R.id.btn_login:
                if (etEmailLogin.getText().toString().length() < 1 && etPassLogin.getText().toString().length() < 1) {
                    Toast.makeText(LoginActivity.this, "Please enter email address and password", Toast.LENGTH_SHORT).show();
                } else if (etEmailLogin.getText().toString().length() < 1) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (!validationClass.isValidEmail(etEmailLogin.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (etPassLogin.getText().toString().length() < 1) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    if (OptomiaDoctorConstant.isNetworkAvailable(LoginActivity.this)) {
                        if (OptomiaDoctorConstant.isNetworkAvailable(LoginActivity.this)) {
                            GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
                            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                                @Override
                                public void onSuccess(String registrationId, boolean isNewRegistration) {
                                    OptomiaDoctorConstant.printMessage("Registration id:::", registrationId);
                                    editor.putString("GCM_ID", registrationId).commit();
                                    getLoginDetalis();
                                }

                                @Override
                                public void onFailure(String ex) {
                                    super.onFailure(ex);
                                }
                            });
                        }
                    } else {
                        OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                    }
                }
                break;
            case R.id.txtforget_login:
                forgetPassword();
                break;
            case R.id.txtsign_login:
                Intent objIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.in,
                        R.anim.out);
                break;
        }
    }

    private void getLoginDetalis() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(LoginActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.USER_LOGIN_URL, new GetResponse("Login"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("email_addr", etEmailLogin.getText().toString());
                        map.put("pass", etPassLogin.getText().toString());
                        map.put("os_id", "1");
                        map.put("device_id", strIMEI);
                        map.put("gcm_id", objSharedPreferences.getString("GCM_ID", ""));
                        map.put("module", "6");
                        map.put("Iphone_id", "");
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        OptomiaDoctorConstant.printMessage("VALUE", map.toString());
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

        public GetResponse(String Type) {
            this.strType = Type;
        }

        @Override
        public void onResponse(String strResponse) {
            OptomiaDoctorConstant.printMessage("Result:::", strResponse);
            if (strType.equals("Login")) {
                JSONObject jsonObject;
                JSONObject resultObject;
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        editor.putString("docid", resultObject.getJSONObject("data").getString("doc_id"));
                        editor.putString("userid", resultObject.getJSONObject("data").getString("user_id"));
                        editor.putString("empid", resultObject.getJSONObject("data").getString("empid"));
                        editor.putString("first_name", resultObject.getJSONObject("data").getString("first_name"));
                        editor.putString("last_name", resultObject.getJSONObject("data").getString("last_name"));
                        editor.putString("employee_type", resultObject.getJSONObject("data").getString("employee_type"));
                        editor.putString("address", resultObject.getJSONObject("data").getString("address"));
                        editor.putString("email", resultObject.getJSONObject("data").getString("email"));
                        editor.putString("phone_no", resultObject.getJSONObject("data").getString("phone_no"));
                        editor.putString("gender", resultObject.getJSONObject("data").getString("gender"));
                        editor.putString("visiting_type", resultObject.getJSONObject("data").getString("visiting_type"));
                        editor.putString("category_id", resultObject.getJSONObject("data").getString("id"));
                        editor.putString("category", resultObject.getJSONObject("data").getString("category"));
                        editor.putString("latitude", resultObject.getJSONObject("data").getString("latitude"));
                        editor.putString("longitude", resultObject.getJSONObject("data").getString("longitude"));
                        editor.putString("degree", resultObject.getJSONObject("data").getString("degree"));
                        editor.putString("user_profile", resultObject.getString("url") + resultObject.getJSONObject("data").getString("photo"));
                        editor.putString("access_token", resultObject.getString("access_token"));
                        editor.putString("Login_flag", "Yes");
                        editor.commit();
                        dialogLoading.dismiss();
                        //OptomiaDoctorConstant.showToast(getApplicationContext(), "Login Successfully");
                        Intent objIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(objIntent);
                        overridePendingTransition(R.anim.in,
                                R.anim.out);
                        finish();
                    } else {
                        dialogLoading.dismiss();
                        OptomiaDoctorConstant.showToast(getApplicationContext(), resultObject.getString("msg"));
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                    e.printStackTrace();
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Login fail");
                }
            } else if (strType.equals("ForgetPass")) {
                JSONObject jsonObject;
                JSONObject resultObject;
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        dialogLoading.dismiss();
                        dialog.cancel();
                        OptomiaDoctorConstant.showToast(getApplicationContext(), "Reset password link has been mailed to you. Please check your mail");
                    } else {
                        dialogLoading.dismiss();
                        OptomiaDoctorConstant.showToast(getApplicationContext(), "This user is not registered with us");
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                    e.printStackTrace();
                    OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong ");
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
}