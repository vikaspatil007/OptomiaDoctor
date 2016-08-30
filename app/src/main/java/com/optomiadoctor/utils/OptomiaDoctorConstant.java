package com.optomiadoctor.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class OptomiaDoctorConstant {

    public static final String PREFS_NAME = "MyPrefsFileDoc";
    public static String APPID = "6d95859bcf6364fc3e7f8ce29b356dfc";
    public static String FONTREGULAR = "fonts/SEGOEUI.TTF";
    public static int debug = 1;

    /////////////////////////////////////////////////////////////////////////////////

    // public static final String HOST_URL = "http://192.168.1.2/optomia/";
    public static final String HOST_URL = "http://108.179.198.195/~optomia/";

    public static final String LOGIN_SERVICE_URL = HOST_URL + "ws/consultant/Home_consultant/";
    public static final String OTHER_SERVICE_URL = HOST_URL + "ws/consultant/Manage_consultant/";
    public static final String USER_REGISTER_URL = LOGIN_SERVICE_URL + "register_consultant";
    public static final String USER_LOGIN_URL = LOGIN_SERVICE_URL + "login";
    public static final String FORGET_PASS_URL = LOGIN_SERVICE_URL + "forgot_password";
    public static final String LOGOUT_URL = LOGIN_SERVICE_URL + "logout";

    public static final String TODAYS_APPOINTMENT_URL = OTHER_SERVICE_URL + "todays_appointment";
    public static final String PREVIOUS_APPOINTMENT_URL = OTHER_SERVICE_URL + "prev_appointment";
    public static final String UPCOMMING_APPOINTMENT_URL = OTHER_SERVICE_URL + "upcomming_appointment";
    public static final String COUNT_URL = OTHER_SERVICE_URL + "todays_appointment_count";
    public static final String SURGERY_CATEGORY_URL = OTHER_SERVICE_URL + "all_surgery_category";
    public static final String SURGERY_URL = OTHER_SERVICE_URL + "all_surgery_type";
    public static final String CANCEL_APPOINTMENT = OTHER_SERVICE_URL + "cancal_appointment";
    public static final String SENDREQUEST_URL = OTHER_SERVICE_URL + "insert_doctor_request";
    public static final String HISTORY_APPOINTMENT_URL = OTHER_SERVICE_URL + "history_appointment";
    public static final String PROFILE_UPDATE_URL = OTHER_SERVICE_URL + "profile_update";
    public static final String VIEW_PROFILE_URL = OTHER_SERVICE_URL + "my_profile ";
    public static final String NOTOFICATION_URL = OTHER_SERVICE_URL + "view_allnotification";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void printMessage(String key, Object value) {
        if (OptomiaDoctorConstant.debug == 1) {
            System.out.println(key + " : " + value);
        }
    }

    public static void showToast(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean containsOnlyNumbers(String data) {
        data = data.trim();
        if (data.matches("^[0-9]{1,}$")) {
            return true;
        } else {
            return false;
        }
    }
}