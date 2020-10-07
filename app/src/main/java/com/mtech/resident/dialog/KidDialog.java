package com.mtech.resident.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class KidDialog extends DialogFragment {
    Spinner hours;
    ArrayAdapter<String> branch_adapter, company_adapter;
    Button kidok;
    String str_hours = null;
    int exit_hours = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String m, d, mf, df, f_date, f_time, t_time;
    String str_date = null, str_startTime = null, str_endTime = null, str_vehicleNo = null,
            str_company_name = null, str_category = "once", CreationDate = null;
    java.sql.Date sqlDate, sql_startTime, sql_endTime;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String buildingName = null, flatNo = null, societyname = null, flat = null, wing = null;
    String aa=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.kid_dialog, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();

        //========================================================================

        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        buildingName = sp.getString("buildingName", null);
        flatNo = sp.getString("FlatNo", null);
        societyname = sp.getString("societyname", null);

        //========================================================================

        hours = view.findViewById(R.id.hours);
        kidok = view.findViewById(R.id.okbtn);

        kidok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                String Date = dateFormat.format(new Date());
                String Time = dateFormat1.format(new Date());
                CreationDate = Date + " " + Time;

                str_hours = hours.getSelectedItem().toString();
                if (str_hours.equalsIgnoreCase("2 hours")) {
                    exit_hours = 2;
                }
                if (str_hours.equalsIgnoreCase("6 hours")) {
                    exit_hours = 6;
                }
                if (str_hours.equalsIgnoreCase("12 hours")) {
                    exit_hours = 12;
                }
                if (str_hours.equalsIgnoreCase("24 hours")) {
                    exit_hours = 24;
                }
                 aa = addHours(exit_hours);

                new GetLongLat("S").execute();
            }
        });
        ArrayList<String> purpose = new ArrayList<String>();
        purpose.add("2 hours");
        purpose.add("6 hours");
        purpose.add("12 hours");
        purpose.add("24 hours");

        branch_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, purpose);
        hours.setAdapter(branch_adapter);

        return view;
    }

    //================================================================================================

    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs = null, rs1 = null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag) {
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {

                showName(rs, rs1, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null) {
                    ret = "Internet Connection Error";
                } else {

                    if (flag.equalsIgnoreCase("S")) {

                        PreparedStatement stmt = conn.prepareStatement("insert into KidEntry" +
                                "(StartDate, ExpirationDate, StartTime, EndTime, Category, SocietyName, OwnerFlatNo," +
                                " OwnerBuildingName ,EntryAt)" + " values(?,?,?,?,?,?,?,?,?)");

                        stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                        stmt.setDate(2, sqlDate);
                        stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        stmt.setString(4, aa);
                        stmt.setString(5, "kids");
                        stmt.setString(6, societyname);
                        stmt.setString(7, wing + flat);
                        stmt.setString(8, buildingName);
                        stmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

                        int i = stmt.executeUpdate();
                        ret = "" + i;

                    }
                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {
            if (flag.equalsIgnoreCase("S")) {
                try {
                    Toast.makeText(getContext(), "Kid Entry Successful !", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //================================================================================================
    public String addHours(int exit_hours){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currentDateandTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);

        Toast.makeText(getContext(), ""+calendar.getTime(), Toast.LENGTH_SHORT).show();
        String addhourss = ""+calendar.getTime();
        return addhourss;
    }
}
