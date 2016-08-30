package com.optomiadoctor.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    LinearLayout llMainLayout;
    EditText etFirstName, etLastName, etPhonnumber, etEmail, etPassword;
    CheckBox chMale, chFemale;
    String strGender = "M";
    Button btncancel, btnSubmit;
    String strEmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._]+\\.+[a-z]+";
    AlertDialog dialogLoading;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        initLisenter();
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
        txtToolbarTitle.setText("Sign up");

        llMainLayout = (LinearLayout) findViewById(R.id.ll_maincontianer_signup);
        llMainLayout.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));
        etFirstName = (EditText) findViewById(R.id.etfirstname_signup);
        etFirstName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etFirstName.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etFirstName.setTypeface(objTypefaceRegular);
        etFirstName.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));


        etLastName = (EditText) findViewById(R.id.etlast_name_signup);
        etLastName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etLastName.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etLastName.setTypeface(objTypefaceRegular);
        etLastName.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));


        etPhonnumber = (EditText) findViewById(R.id.etphone_num_signup);
        etPhonnumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etPhonnumber.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etPhonnumber.setTypeface(objTypefaceRegular);
        etPhonnumber.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));


        etEmail = (EditText) findViewById(R.id.etemail_signup);
        etEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etEmail.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etEmail.setTypeface(objTypefaceRegular);
        etEmail.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        etPassword = (EditText) findViewById(R.id.etpassword_signup);
        etPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etPassword.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etPassword.setTypeface(objTypefaceRegular);
        etPassword.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));


        chMale = (CheckBox) findViewById(R.id.chkSignUpMale);
        chMale.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        chMale.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        chMale.setTypeface(objTypefaceRegular);

        chFemale = (CheckBox) findViewById(R.id.chkSignUpFemale);
        chFemale.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        chFemale.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        chFemale.setTypeface(objTypefaceRegular);

        chMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chFemale.setChecked(false);
                    strGender = "M";
                } else {
                    if (chFemale.isChecked()) {
                        chMale.setChecked(false);
                        chFemale.setChecked(true);
                        strGender = "F";
                    } else {
                        chMale.setChecked(true);
                        chFemale.setChecked(false);
                        strGender = "M";
                    }

                }
            }
        });
        chFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chMale.setChecked(false);
                    strGender = "F";
                } else {
                    if (chMale.isChecked()) {
                        chMale.setChecked(true);
                        chFemale.setChecked(false);
                        strGender = "M";
                    } else {
                        chMale.setChecked(false);
                        chFemale.setChecked(true);
                        strGender = "F";
                    }
                }
            }
        });
        btncancel = (Button) findViewById(R.id.btn_cancel_signup);
        btncancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btncancel.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btncancel.setTypeface(objTypefaceRegular);


        btnSubmit = (Button) findViewById(R.id.btn_submit_signup);
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btnSubmit.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btnSubmit.setTypeface(objTypefaceRegular);
    }

    private void initLisenter() {
        btncancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_signup:
                finish();
                overridePendingTransition(R.anim.slide_right,
                        R.anim.slide_out_right);
                break;
            case R.id.btn_submit_signup:
                boolean bolNumber = OptomiaDoctorConstant.containsOnlyNumbers(etPhonnumber.getText().toString().trim());
                if (!etFirstName.getText().toString().trim().isEmpty()) {
                    if (!etLastName.getText().toString().trim().isEmpty()) {
                        if (!etEmail.getText().toString().trim().isEmpty() && etEmail.getText().toString().trim().matches(strEmailPattern)) {
                            if (!etPassword.getText().toString().trim().isEmpty()) {
                                if (bolNumber == true) {
                                    if (OptomiaDoctorConstant.isNetworkAvailable(SignupActivity.this)) {
                                        registerUser();
                                    } else {
                                        OptomiaDoctorConstant.showToast(getApplicationContext(), "Network connectivity is not Available");
                                    }
                                } else {
                                    etPhonnumber.setError("Please enter valid phone number");
                                }
                            } else {
                                etPassword.setError("Enter your password");
                            }
                        } else {
                            etEmail.setError("Please Enter your email");
                        }
                    } else {
                        etLastName.setError("Please Enter your last name");
                    }
                } else {
                    etFirstName.setError("Please Enter your name");
                }
                break;
        }
    }

    private void registerUser() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
            requestQueue.getCache().clear();
            dialogLoading.show();
            if (OptomiaDoctorConstant.isNetworkAvailable(SignupActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.USER_REGISTER_URL, new GetResponse("Register"), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("first_name", etFirstName.getText().toString().trim());
                        map.put("last_name", etLastName.getText().toString().trim());
                        map.put("email", etEmail.getText().toString());
                        map.put("contact_no", etPhonnumber.getText().toString().trim());
                        map.put("pass", etPassword.getText().toString());
                        map.put("gender", strGender);
                        OptomiaDoctorConstant.printMessage("Data:::", map.toString());
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
            if (strType.equals("Register")) {
                JSONObject jsonObject;
                JSONObject resultObject;
                try {
                    jsonObject = new JSONObject(strResponse);
                    resultObject = jsonObject.getJSONObject("result");
                    if (resultObject.getString("status").equalsIgnoreCase("success")) {
                        finish();
                        overridePendingTransition(R.anim.slide_right,
                                R.anim.slide_out_right);
                        Toast.makeText(SignupActivity.this, "Registration link has been mailed. Please check your mail", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SignupActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
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
