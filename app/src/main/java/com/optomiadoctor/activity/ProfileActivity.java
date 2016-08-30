package com.optomiadoctor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.optomiadoctor.R;
import com.optomiadoctor.utils.GetData_Image;
import com.optomiadoctor.utils.GetData_Image.OnAsyncResult_Image;
import com.optomiadoctor.utils.OptomiaDoctorConstant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ProfileActivity extends Activity implements View.OnClickListener {
    private LinearLayout llDrImage, llFirstMaincontiner, llMainContainer, llMap;
    private SharedPreferences objSharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView txtDrProfileName, txtDrProfileSpecialist;
    private Typeface objTypefaceRegular;
    EditText etFirstName, etLastName, etPhonnumber, etAddress;
    private GoogleMap googleMap;
    TextView txtToolbarTitle;
    ImageView imgToolbarBack, imgEdit;
    long mSystemTime;
    ImageView imgDrProfile;
    Button btnSubmit;
    String saved_path = "";
    AlertDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        init();
        initListener();
        getProfileData();
    }

    private void getProfileData() {
        try {
            loadDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.getCache().clear();
            if (OptomiaDoctorConstant.isNetworkAvailable(ProfileActivity.this)) {
                StringRequest request = new StringRequest(Request.Method.POST, OptomiaDoctorConstant.VIEW_PROFILE_URL, new GetResponse(), new GetError()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("app_id", OptomiaDoctorConstant.APPID);
                        map.put("emp_id", objSharedPreferences.getString("empid", ""));
                        map.put("access_token", objSharedPreferences.getString("access_token", ""));
                        OptomiaDoctorConstant.printMessage("Value:::", map.toString());
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
            System.out.println("NotificationResponse = " +response);
            try {
                JSONObject jsonObject;
                JSONObject resultObject;
                jsonObject = new JSONObject(response);
                resultObject = jsonObject.getJSONObject("result");
                if (resultObject.getString("status").equalsIgnoreCase("success")) {
                    System.out.println("NotificationResponse = " + resultObject.getJSONObject("data").getString("doc_id"));
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
                    editor.commit();
                    setData();
                    loadDialog.dismiss();
                } else {
                    loadDialog.dismiss();
                    OptomiaDoctorConstant.showToast(ProfileActivity.this, "Data not available");
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

    private void init() {
        objSharedPreferences = this.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(this.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        loadDialog = new SpotsDialog(ProfileActivity.this);
        imgToolbarBack = (ImageView) findViewById(R.id.img_back_profile);
        imgToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_right,
                        R.anim.slide_out_right);
            }
        });
        imgEdit = (ImageView) findViewById(R.id.img_edit_profile);
        txtToolbarTitle = (TextView) findViewById(R.id.txt_heading_profile);
        txtToolbarTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (objSharedPreferences.getInt("width", 0) * 0.050));
        txtToolbarTitle.setTypeface(objTypefaceRegular);
        txtToolbarTitle.setText("Profile");

        llFirstMaincontiner = (LinearLayout) findViewById(R.id.ll_firstmain_profile);
        LinearLayout.LayoutParams llFirst = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (objSharedPreferences.getInt("width", 0) * 0.6));
        llFirstMaincontiner.setLayoutParams(llFirst);
        llMainContainer = (LinearLayout) findViewById(R.id.ll_maincontainer_profile);
        llMainContainer.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("height", 0) / 32));

        llDrImage = (LinearLayout) findViewById(R.id.llDrImage);
        llDrImage.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));
        imgDrProfile = (ImageView) findViewById(R.id.imgDrProfile);

        txtDrProfileName = (TextView) findViewById(R.id.txtDrProfileName);
        txtDrProfileName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.070));
        txtDrProfileName.setTypeface(objTypefaceRegular);
        txtDrProfileName.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0, (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                0);


        txtDrProfileSpecialist = (TextView) findViewById(R.id.txtDrProfileSpecialist);
        txtDrProfileSpecialist.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.040));
        txtDrProfileSpecialist.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0,
                (int) (objSharedPreferences.getInt("width", 0) * 0.032), 0);
        txtDrProfileSpecialist.setTypeface(objTypefaceRegular);
        txtDrProfileSpecialist.setText(objSharedPreferences.getString("category", ""));

        etFirstName = (EditText) findViewById(R.id.etfirstname_profile);
        etFirstName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etFirstName.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etFirstName.setTypeface(objTypefaceRegular);
        etFirstName.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        etLastName = (EditText) findViewById(R.id.etlast_name_profile);
        etLastName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etLastName.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etLastName.setTypeface(objTypefaceRegular);
        etLastName.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        etPhonnumber = (EditText) findViewById(R.id.etphone_num_profile);
        etPhonnumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        etPhonnumber.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etPhonnumber.setTypeface(objTypefaceRegular);
        etPhonnumber.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        etAddress = (EditText) findViewById(R.id.etAddress_profile);
        etAddress.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        etAddress.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        etAddress.setTypeface(objTypefaceRegular);
        etAddress.setCompoundDrawablePadding((int) (objSharedPreferences.getInt("width", 0) / 74));

        LinearLayout.LayoutParams paramsMap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (objSharedPreferences.getInt("width", 0) / 2));
        paramsMap.setMargins(0, 0, 0, (int) (objSharedPreferences.getInt("width", 0) / 32));
        llMap = (LinearLayout) findViewById(R.id.llMap_proifle);
        llMap.setLayoutParams(paramsMap);

        btnSubmit = (Button) findViewById(R.id.btn_submit_profile);
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        btnSubmit.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));
        btnSubmit.setTypeface(objTypefaceRegular);
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            double objdoubleLatitude = Double.parseDouble(objSharedPreferences.getString("latitude", ""));
            double objdoubleLongitude = Double.parseDouble(objSharedPreferences.getString("longitude", ""));
            System.out.println("objdoubleLatitude=" + objdoubleLatitude);
            System.out.println("objdoubleLongitude=" + objdoubleLongitude);

            LatLng KIEL = new LatLng(objdoubleLatitude, objdoubleLongitude);
            Marker kiel = googleMap.addMarker(new MarkerOptions().position(KIEL).snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin)));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(objdoubleLatitude, objdoubleLongitude)).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        imgEdit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void setData() {
        btnSubmit.setVisibility(View.INVISIBLE);

        llDrImage.setOnClickListener(null);

        etFirstName.setOnClickListener(null);
        etFirstName.setFocusableInTouchMode(false);
        etFirstName.setCursorVisible(false);

        etLastName.setOnClickListener(null);
        etFirstName.setCursorVisible(false);

        etPhonnumber.setOnClickListener(null);
        etPhonnumber.setFocusableInTouchMode(false);

        etPhonnumber.setCursorVisible(false);

        etFirstName.setText(objSharedPreferences.getString("first_name", ""));
        etLastName.setText(objSharedPreferences.getString("last_name", ""));
        etPhonnumber.setText(objSharedPreferences.getString("phone_no", ""));
        etAddress.setText(objSharedPreferences.getString("address", ""));
        txtDrProfileName.setText(objSharedPreferences.getString("first_name", "") + " " + objSharedPreferences.getString("last_name", ""));
        if (!objSharedPreferences.getString("user_profile", "").isEmpty()) {
            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.parseColor("#ffffff"))
                    .borderWidthDp(1).cornerRadiusDp(1).oval(true).build();
            Picasso.with(this).load(objSharedPreferences.getString("user_profile", ""))
                    .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                    .resize((int) (objSharedPreferences.getInt("width", 0) * 0.28),
                            (int) (objSharedPreferences.getInt("width", 0) * 0.28))
                    .transform(transformation).into(imgDrProfile);
            OptomiaDoctorConstant.printMessage("Image path", saved_path);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llDrImage:
                select_photo_options();
                break;
            case R.id.img_edit_profile:
                btnSubmit.setVisibility(View.VISIBLE);
                llDrImage.setOnClickListener(this);

                etFirstName.setOnClickListener(this);
                etFirstName.setFocusableInTouchMode(true);
                etFirstName.setCursorVisible(true);
                etFirstName.requestFocus();
                InputMethodManager imm = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etFirstName, InputMethodManager.SHOW_IMPLICIT);

                etLastName.setOnClickListener(this);
                etFirstName.setCursorVisible(true);

                etPhonnumber.setOnClickListener(this);
                etPhonnumber.setFocusableInTouchMode(true);
                etPhonnumber.setCursorVisible(true);
                break;

            case R.id.btn_submit_profile:
                boolean bolNumber = OptomiaDoctorConstant.containsOnlyNumbers(etPhonnumber.getText().toString().trim());
                if (!etFirstName.getText().toString().isEmpty()) {
                    if (!etLastName.getText().toString().isEmpty()) {
                        if (!etPhonnumber.getText().toString().isEmpty()) {
                            if (bolNumber == true) {
                                GetData_Image objData = new GetData_Image(OptomiaDoctorConstant.PROFILE_UPDATE_URL,
                                        new String[]{"first_name", "last_name",
                                                "contact_no", "app_id", "id", "access_token"}, new String[]{etFirstName.getText().toString().trim(), etLastName.getText().toString().trim(), etPhonnumber.getText().toString().trim(), OptomiaDoctorConstant.APPID, objSharedPreferences.getString("empid", ""), objSharedPreferences.getString("access_token", "")}, this, "profile", saved_path);
                                objData.setOnResultListener_Image(asynResult);
                                objData.execute("");
                            } else {
                                etPhonnumber.setError("Please enter valid number");
                            }
                        } else {
                            etPhonnumber.setError("Please enter phone number");
                        }
                    } else {
                        etLastName.setError("Please enter your last name");
                    }
                } else {
                    etFirstName.setError("Please enter your first name");
                }
                break;
        }
    }

    void select_photo_options() {
        new BottomSheet.Builder(this, R.style.BottomSheet_Dialog)
                .grid().sheet(R.menu.options)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO
                        switch (which) {
                            case R.id.camera:
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                mSystemTime = System.currentTimeMillis();
                                File f = new File(Environment.getExternalStorageDirectory(), "temp" + mSystemTime + ".jpg");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                startActivityForResult(cameraIntent, 110);
                                break;
                            case R.id.gallery:
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, 2);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 110:
                try {
                    if (requestCode == 110 && resultCode == RESULT_OK) {
                        File dir = Environment.getExternalStorageDirectory();
                        File f = new File(dir, "temp" + mSystemTime + ".jpg");
                        String sPicturePath = f.getAbsolutePath();
                        int rotation = getImageOrientation(sPicturePath);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(rotation);
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Bitmap sourceBitmap = BitmapFactory.decodeFile(sPicturePath,
                                options);
                        sourceBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                                sourceBitmap.getWidth(), sourceBitmap.getHeight(),
                                matrix, true);

                        mSystemTime = System.currentTimeMillis();
                        File f1 = new File(
                                android.os.Environment.getExternalStorageDirectory(),
                                "temp" + mSystemTime + ".jpg");
                        saved_path = "";
                        try {
                            FileOutputStream out = new FileOutputStream(f1);
                            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                            out.flush();
                            out.close();
                            saved_path = f1.getAbsolutePath();
                            File f2 = new File(saved_path);

                            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.parseColor("#ffffff"))
                                    .borderWidthDp(1).cornerRadiusDp(1).oval(true).build();
                            Picasso.with(this).load(f2)
                                    .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                                    .resize((int) (objSharedPreferences.getInt("width", 0) * 0.28),
                                            (int) (objSharedPreferences.getInt("width", 0) * 0.28))
                                    .transform(transformation).into(imgDrProfile);

                            OptomiaDoctorConstant.printMessage("Image path", saved_path);
                            refreshGallery(f1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    saved_path = c.getString(columnIndex);
                    File f2 = new File(saved_path);
                    c.close();
                    Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.parseColor("#ffffff"))
                            .borderWidthDp(1).cornerRadiusDp(1).oval(true).build();
                    Picasso.with(this).load(f2)
                            .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                            .resize((int) (objSharedPreferences.getInt("width", 0) * 0.28),
                                    (int) (objSharedPreferences.getInt("width", 0) * 0.28))
                            .transform(transformation).into(imgDrProfile);
                    OptomiaDoctorConstant.printMessage("Image path", saved_path);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    OnAsyncResult_Image asynResult = new OnAsyncResult_Image() {
        @Override
        public void onResultSuccess(final String strResultFromJson,
                                    final String strType) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        if (strType.equals("profile")) {
                            OptomiaDoctorConstant.printMessage("Result:::", strResultFromJson);
                            JSONObject jsonObject;
                            JSONObject resultObject;
                            JSONArray jsonArray = null;
                            try {
                                jsonObject = new JSONObject(strResultFromJson);
                                resultObject = jsonObject.getJSONObject("result");
                                if (resultObject.getString("status").equalsIgnoreCase("success")) {
                                    jsonArray = resultObject.getJSONArray("data");
                                    editor.putString("first_name", jsonArray.getJSONObject(0).getString("first_name"));
                                    editor.putString("last_name", jsonArray.getJSONObject(0).getString("last_name"));
                                    editor.putString("phone_no", jsonArray.getJSONObject(0).getString("phone_no"));
                                    editor.putString("user_profile", resultObject.getString("url") + jsonArray.getJSONObject(0).getString("photo"));
                                    editor.commit();
                                    OptomiaDoctorConstant.printMessage("URL", objSharedPreferences.getString("user_profile", ""));
                                   Intent objIntent=new Intent(ProfileActivity.this,ProfileActivity.class);
                                    startActivity(objIntent);
                                    finish();
                                } else {
                                    OptomiaDoctorConstant.showToast(getApplicationContext(), resultObject.getString("msg"));
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong ");
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        OptomiaDoctorConstant.printMessage("ERROR ::::", e);
                        OptomiaDoctorConstant.showToast(getApplicationContext(), "Something went wrong ");
                        finish();
                    }

                }
            });
        }

        @Override
        public void onResultFail(final String errorMessage) {
            // need to add this part in ui thread.
            // as you will then thread exception.
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_right,
                R.anim.slide_out_right);
    }
}