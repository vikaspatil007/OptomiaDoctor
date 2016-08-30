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
 * Created by vikas.patil on 5/3/2016.
 */
public class HistoryAdapter extends BaseAdapter {
    Activity objActivity;
    public SharedPreferences objSharedPreferences;
    Typeface objTypefaceRegular;
    LinearLayout objLinearLayoutMain;
    TextView txtName, txtPaientID, txtTimeTitle, txtSetTime, txtDateTitle, txtSetDate,txtStatus;
    public static ArrayList<AppointmentModel> arrAppointmentModel = new ArrayList<AppointmentModel>();

    public HistoryAdapter(Activity activity, ArrayList<AppointmentModel> arr) {
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
        View rowView = inflater.inflate(R.layout.list_item_history, null, true);
        final int intPosition = position;
        objLinearLayoutMain = (LinearLayout) rowView.findViewById(R.id.ll_maincontainer_history);
        objLinearLayoutMain.setPadding((int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32),
                (int) (objSharedPreferences.getInt("width", 0) / 32), (int) (objSharedPreferences.getInt("width", 0) / 32));

        txtName = (TextView) rowView.findViewById(R.id.txt_name_history);
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.05));
        txtName.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0, (int) (objSharedPreferences.getInt("width", 0) * 0.032), 0);
        txtName.setTypeface(objTypefaceRegular, Typeface.BOLD);
        String strTemp = arrAppointmentModel.get(intPosition).getStrFirstName() + " " + arrAppointmentModel.get(intPosition).getStrLastName();

        txtName.setText(strTemp.substring(0, 1).toUpperCase()
                + strTemp.substring(1).toLowerCase());

        txtPaientID = (TextView) rowView.findViewById(R.id.txt_patientid_history);
        txtPaientID.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        txtPaientID.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), 0, (int) (objSharedPreferences.getInt("width", 0) * 0.032), 0);
        txtPaientID.setTypeface(objTypefaceRegular);
        txtPaientID.setText("Patient ID : " + arrAppointmentModel.get(intPosition).getStrAppId());

        txtTimeTitle = (TextView) rowView.findViewById(R.id.txt_time_title_history);
        txtTimeTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtTimeTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtTimeTitle.setTypeface(objTypefaceRegular);

        txtSetTime = (TextView) rowView.findViewById(R.id.txt_settime_history);
        txtSetTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtSetTime.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.010), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtSetTime.setTypeface(objTypefaceRegular);
        txtSetTime.setText(arrAppointmentModel.get(intPosition).getStrApprovedTime());


        txtDateTitle = (TextView) rowView.findViewById(R.id.txt_date_title_history);
        txtDateTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtDateTitle.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtDateTitle.setTypeface(objTypefaceRegular);


        txtSetDate = (TextView) rowView.findViewById(R.id.txt_setdate_history);
        txtSetDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtSetDate.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.010), (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                (int) (objSharedPreferences.getInt("width", 0) * 0.010),
                0);
        txtSetDate.setTypeface(objTypefaceRegular);
        String strDate=arrAppointmentModel.get(intPosition).getStrApprovedDate();
        String[] separated = strDate.split("T");
        txtSetDate.setText(separated[0]);


        txtStatus= (TextView) rowView.findViewById(R.id.txt_Status_history);
        txtStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.04));
        txtStatus.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));
        txtStatus.setTypeface(objTypefaceRegular);

        if (arrAppointmentModel.get(intPosition).getStrStatus().equals("0")) {
            txtStatus.setBackgroundColor(Color.parseColor("#ed1c24"));
            txtStatus.setText("Pending");
        }else if(arrAppointmentModel.get(intPosition).getStrStatus().equals("1")){
            txtStatus.setBackgroundColor(Color.parseColor("#06ae1a"));
            txtStatus.setText("Approved");
        }else if(arrAppointmentModel.get(intPosition).getStrStatus().equals("2")){
            txtStatus.setBackgroundColor(Color.parseColor("#0395ab"));
            txtStatus.setText("Completed");
        }else if(arrAppointmentModel.get(intPosition).getStrStatus().equals("3")){
            txtStatus.setBackgroundColor(Color.parseColor("#d37c00"));
            txtStatus.setText("Cancel");
        }
        return rowView;
    }
}
