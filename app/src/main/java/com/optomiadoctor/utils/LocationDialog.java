package com.optomiadoctor.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.optomiadoctor.R;
import com.optomiadoctor.activity.SignupActivity;
import com.optomiadoctor.model.ExploreNewLocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by vikas.patil on 5/2/2016.
 */
public class LocationDialog {
    SharedPreferences objSharedPreferences;
    SharedPreferences.Editor editor;
    GooglePlacesAutocompleteAdapter objAdapter;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAT5JpeqLKeH-7h5ZfFWLSsJwq2xZRk46s";
    Activity objActivity;
    EditText editText;
     Dialog dialog;
    public static EditText objEditText;
    public static ListView objListView;

    public LocationDialog(Activity Activity, EditText etAddress) {
        this.objActivity = Activity;
        this.editText = etAddress;
        objSharedPreferences = objActivity.getSharedPreferences(OptomiaDoctorConstant.PREFS_NAME, 0);
        editor = objSharedPreferences.edit();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void getLocation() {
        dialog = new Dialog(objActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_test);
        objEditText = (EditText) dialog.findViewById(R.id.temp);
        objListView = (ListView) dialog.findViewById(R.id.lvtodays_app);
        objAdapter = new GooglePlacesAutocompleteAdapter(
                objActivity,
                R.layout.listview_explore_new_loc);
        objEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub
                if (objEditText.getText().toString().length() > 0) {
                    System.out.println("GOOGLE API");
                    objAdapter.clear();
                    objAdapter.getFilter().filter(
                            s.toString().toLowerCase());
                    objListView.setAdapter(objAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        dialog.show();
    }

    public static ArrayList<ExploreNewLocationModel> autocomplete(String input) {
        ArrayList<ExploreNewLocationModel> resultList = null;

        HttpURLConnection objHttpConnection = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            objHttpConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(
                    objHttpConnection.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {
            return resultList;
        } finally {
            if (objHttpConnection != null) {
                objHttpConnection.disconnect();
            }
        }

        try {

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<ExploreNewLocationModel>(
                    predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                ExploreNewLocationModel model = new ExploreNewLocationModel();
                model.setStrGoogleDesc(predsJsonArray.getJSONObject(i)
                        .getString("description"));
                model.setStrGoogleId(predsJsonArray.getJSONObject(i).getString(
                        "place_id"));
                resultList.add(model);
            }
        } catch (JSONException e) {

        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends
            ArrayAdapter<ExploreNewLocationModel> implements Filterable {
        private ArrayList<ExploreNewLocationModel> resultList = new ArrayList<ExploreNewLocationModel>();
        int layout;

        public GooglePlacesAutocompleteAdapter(Context context, int layout) {
            super(context, layout);
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public ExploreNewLocationModel getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = objActivity.getLayoutInflater();
            View rowView = inflater.inflate(layout, null, true);
            try {
                TextView objTextViewCityName = (TextView) rowView
                        .findViewById(R.id.txtnearcity_explore);

                objTextViewCityName.setText(resultList.get(position)
                        .getStrGoogleDesc());

                objListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        ExploreNewLocationModel objExploreNewLocationModel = resultList
                                .get(position);
                        System.out.println("GOOGLE:::::::"
                                + objExploreNewLocationModel.getStrGoogleDesc());
                        objEditText.setText(objExploreNewLocationModel.getStrGoogleDesc());
                        editText.setText(objExploreNewLocationModel.getStrGoogleDesc());
                        getLat(objExploreNewLocationModel.getStrGoogleId(),
                                "AIzaSyBIunpLcs4hyqmvedemI2dcPPR64UpyBDg");


                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
            }
            return rowView;
        }

        protected void getLat(String strGoogleId, String string) {
            // TODO Auto-generated method stub

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(
                        "https://maps.googleapis.com/maps/api/place/details/json");
                sb.append("?placeid=" + strGoogleId);
                sb.append("&key=" + string);
                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            try {
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONObject jsonObjectResult = jsonObj.getJSONObject("result")
                        .getJSONObject("geometry").getJSONObject("location");
                editor = objSharedPreferences.edit();
                editor.putString("Latitude", jsonObjectResult.getString("lat"));
                editor.putString("Longitude", jsonObjectResult.getString("lng"));

                editor.commit();
                dialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception

            }

        }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
