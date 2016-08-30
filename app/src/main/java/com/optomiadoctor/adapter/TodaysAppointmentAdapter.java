package com.optomiadoctor.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optomiadoctor.R;
import com.optomiadoctor.model.AppointmentModel;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import java.util.ArrayList;

/**
 * Created by vikas.patil on 4/25/2016.
 */
public class TodaysAppointmentAdapter extends BaseAdapter {
    Activity objActivity;
    public SharedPreferences objSharedPreferences;
    Typeface objTypefaceRegular;
    LinearLayout objLinearLayoutMain, objLinearLayoutInner;
    TextView txtName, txtPaientID, txtTimeTitle, txtSetTime,txtDateTitle,txtSetDate;
    public static ArrayList<AppointmentModel> arrAppointmentModel = new ArrayList<AppointmentModel>();

    public TodaysAppointmentAdapter(Activity activity, ArrayList<AppointmentModel> arr) {
        this.objActivity = activity;
        this.arrAppointmentModel = arr;
        objSharedPreferences = objActivity.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        objTypefaceRegular = Typeface.createFromAsset(objActivity.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
    }

    @Override
    public int getCount() {
        return arrAppointmentModel.size();
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
        LayoutInflater inflater = objActivity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_appointment, null, true);
        final int intPosition = position;
        objLinearLayoutMain = (LinearLayout) rowView.findViewById(R.id.ll_maincontainer_item);
        objLinearLayoutMain.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));

        objLinearLayoutInner = (LinearLayout) rowView.findViewById(R.id.ll_inner_layout_item);
        objLinearLayoutInner.setPadding((int) (objSharedPreferences.getInt("width", 0) / 28), 0,
                0, 0);
        txtName = (TextView) rowView.findViewById(R.id.txt_name_item);
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        txtName.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0, (int) (objSharedPreferences.getInt("width", 0) * 0.032), 0);
        txtName.setTypeface(objTypefaceRegular, Typeface.BOLD);
        String strTemp = arrAppointmentModel.get(intPosition).getStrFirstName() + " " + arrAppointmentModel.get(intPosition).getStrLastName();

        txtName.setText(arrAppointmentModel.get(intPosition).getStrFirstName() + " " + arrAppointmentModel.get(intPosition).getStrLastName());

        txtPaientID = (TextView) rowView.findViewById(R.id.txt_patientid_item);
        txtPaientID.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtPaientID.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0, (int) (objSharedPreferences.getInt("width", 0) * 0.032), 0);
        txtPaientID.setTypeface(objTypefaceRegular);
        txtPaientID.setText("Patient ID : " + arrAppointmentModel.get(intPosition).getStrAppId());

        txtDateTitle = (TextView) rowView.findViewById(R.id.txt_date_title_item);
        txtDateTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtDateTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtDateTitle.setTypeface(objTypefaceRegular);


        txtSetDate = (TextView) rowView.findViewById(R.id.txt_setdate_item);
        txtSetDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtSetDate.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.010), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtSetDate.setTypeface(objTypefaceRegular);
        String strDate=arrAppointmentModel.get(intPosition).getStrApprovedDate();
        String[] separated = strDate.split("T");
        //separated[1];
        txtSetDate.setText(separated[0]);

        txtTimeTitle = (TextView) rowView.findViewById(R.id.txt_time_title_item);
        txtTimeTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtTimeTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtTimeTitle.setTypeface(objTypefaceRegular);

        txtSetTime = (TextView) rowView.findViewById(R.id.txt_settime_item);
        txtSetTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtSetTime.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.010), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtSetTime.setTypeface(objTypefaceRegular);
        txtSetTime.setText(arrAppointmentModel.get(intPosition).getStrApprovedTime());
        return rowView;
    }
}