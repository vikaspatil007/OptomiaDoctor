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
import android.widget.TextView;

import com.optomiadoctor.R;
import com.optomiadoctor.model.SpecialityType;
import com.optomiadoctor.utils.OptomiaDoctorConstant;

import java.util.ArrayList;


/**
 * Created by vikas.patil on 3/25/2016.
 */
public class SpinnerAdapter extends BaseAdapter {
    Activity objActivity;
    ArrayList<SpecialityType> arrProductTypes = new ArrayList<SpecialityType>();
    TextView objTextView;
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    Typeface objTypefaceRegular;
    String strColor;

    public SpinnerAdapter(Activity activity, ArrayList<SpecialityType> arrayList) {
        this.objActivity = activity;
        this.arrProductTypes = arrayList;
        objSharedPreferences = objActivity.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(objActivity.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        this.strColor = "no";
    }

    public SpinnerAdapter(Activity activity, ArrayList<SpecialityType> arrayList, String Color) {
        this.objActivity = activity;
        this.arrProductTypes = arrayList;
        objSharedPreferences = objActivity.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        objTypefaceRegular = Typeface.createFromAsset(objActivity.getAssets(), OptomiaDoctorConstant.FONTREGULAR);
        this.strColor = Color;
    }

    @Override
    public int getCount() {
        return arrProductTypes.size();
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
        View rowView = inflater.inflate(R.layout.spinner_item, null, true);
        final int intPosition = position;

        SpecialityType objProductType = arrProductTypes
                .get(intPosition);
        objTextView = (TextView) rowView.findViewById(R.id.txt_spinner_item);
        objTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (objSharedPreferences.getInt("width", 0) * 0.045));
        objTextView.setPadding((int) (objSharedPreferences.getInt("width", 0) * 0.032), (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032),
                (int) (objSharedPreferences.getInt("width", 0) * 0.032));
        objTextView.setTypeface(objTypefaceRegular);
        if(strColor.equals("yes")){
            objTextView.setTextColor(Color.BLACK);
        }
        objTextView.setText(objProductType.getStrCategoryName().substring(0, 1).toUpperCase()
                + objProductType.getStrCategoryName().substring(1).toLowerCase());
        return rowView;

    }
}
