package com.optomiadoctor.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;

import dmax.dialog.SpotsDialog;

@SuppressWarnings("deprecation")
public class GetData_Image extends AsyncTask<String, Void, Void> {
    AlertDialog dialogLoading;
    private String URL = "";
    private String[] strarrParameterName;
    private String[] strarrParameterValue;
    String strResult = null;
    Activity objActivity;
    String strResultFromJson = null;
    String strType;
    Handler handler;
    String strImagePath;
    FileBody file_body = null;


    public GetData_Image(String Url, String[] ParameterName,
                         String[] ParameterValue, Activity activity, String Type,
                         String ImagePath) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.URL = Url;
        this.strarrParameterName = ParameterName;
        this.strarrParameterValue = ParameterValue;
        this.objActivity = activity;
        this.strType = Type;
        this.strImagePath = ImagePath;
        dialogLoading = new SpotsDialog(objActivity);
    }

    OnAsyncResult_Image objonAsyncResult;

    public void setOnResultListener_Image(OnAsyncResult_Image onAsyncResult) {
        if (onAsyncResult != null) {
            this.objonAsyncResult = onAsyncResult;
        }
    }

    protected void onPreExecute() {
        try {
            dialogLoading.show();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        // TODO Auto-generated method stub
        strResultFromJson = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            MultipartEntity nameMultipartEntity = new MultipartEntity();

            for (int i = 0; i < strarrParameterName.length; i++) {
                nameMultipartEntity.addPart(strarrParameterName[i],
                        new StringBody(strarrParameterValue[i]));
                System.out.println("\nVALUE IN GETDATA:::"
                        + strarrParameterName[i] + ":::::"
                        + strarrParameterValue[i]);
            }

            if (strType.equals("profile")) {
                if (!strImagePath.equalsIgnoreCase("")) {
                    File file = new File(strImagePath);
                    file_body = new FileBody(file);
                    nameMultipartEntity.addPart("userfile", file_body);
                }
            }

            httppost.setEntity(nameMultipartEntity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            strResultFromJson = httpclient.execute(httppost, responseHandler);
            OptomiaDoctorConstant.printMessage("IMAGE RESULT :::",strResultFromJson);
            if (strResultFromJson.isEmpty() || strResultFromJson == null) {
                objonAsyncResult.onResultFail("Something went wrong");
            } else {
                dialogLoading.dismiss();
                objonAsyncResult.onResultSuccess(strResultFromJson, strType);
            }
        } catch (Exception e) {
            // TODO: handle exception
            dialogLoading.dismiss();
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Void unused) {
        try {
            dialogLoading.dismiss();
            handler.removeCallbacks(null);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public interface OnAsyncResult_Image {
        public abstract void onResultSuccess(String message, String type);

        public abstract void onResultFail(String errorMessage);
    }
}
