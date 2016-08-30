package com.optomiadoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optomiadoctor.R;
import com.optomiadoctor.model.NotificationModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import java.util.ArrayList;


/**
 * Created by sandeep.magar on 5/20/2016.
 */
public class NotificationAdapter extends BaseAdapter {

    Activity objActivity;
    TextView txtNotifications, txtNotify_date;
    LinearLayout llNotification;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Typeface objTypefaceRegular;

    ArrayList<NotificationModel> arrayList = new ArrayList<NotificationModel>();

    public NotificationAdapter(Activity objActivity, ArrayList<NotificationModel> arrayList) {
        this.objActivity = objActivity;
        this.arrayList = arrayList;
        this.sharedPreferences = objActivity.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        this.editor = sharedPreferences.edit();
    }

    @Override
    public int getCount() {
        System.out.println("notification Count=" + arrayList.size());
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int intPosition = position;
        LayoutInflater inflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.row_notification, null);
        objTypefaceRegular = Typeface.createFromAsset(objActivity.getAssets(), OptomiaDoctorConstant.FONTREGULAR);

        LinearLayout.LayoutParams paramsInfo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsInfo.setMargins((int) (sharedPreferences.getInt("width", 0) * 0.025), (int) (sharedPreferences.getInt("width", 0) * 0.025),
                (int) (sharedPreferences.getInt("width", 0) * 0.025), (int) (sharedPreferences.getInt("width", 0) * 0.025));
        llNotification = (LinearLayout) convertView.findViewById(R.id.llNotification);
        llNotification.setLayoutParams(paramsInfo);


        llNotification = (LinearLayout) convertView.findViewById(R.id.llNotification);
        llNotification.setPadding((int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.010),
                (int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.010));

        txtNotifications = (TextView) convertView.findViewById(R.id.txtNotifications);
        txtNotifications.setTypeface(objTypefaceRegular, Typeface.BOLD);
        txtNotifications.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (sharedPreferences.getInt("width", 0) * 0.045));
        txtNotifications.setPadding((int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.010),
                (int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.010));

        txtNotify_date = (TextView) convertView.findViewById(R.id.txtNotify_date);
        txtNotify_date.setTypeface(objTypefaceRegular);
        txtNotify_date.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (sharedPreferences.getInt("width", 0) * 0.040));
        txtNotify_date.setPadding((int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.032),
                (int) (sharedPreferences.getInt("width", 0) * 0.032));


        NotificationModel model = arrayList.get(intPosition);
        txtNotifications.setText(model.getNotification().substring(0, 1).toUpperCase() + model.getNotification().substring(1).toLowerCase());
        txtNotify_date.setText(model.getNotify_date());
        return convertView;
    }

}
