package com.mtech.resident.dialogoncefreq;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class FeqcabFragment extends Fragment {
    private String mText = "";
    EditText   timeSlot, selectValidity;
    Button ok;
    Spinner daysOfWeek,companyName;
    ArrayAdapter<String> branch_adapter,company_adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String m, d, mf, df, f_date, f_time, t_time;
    String str_date=null, str_startTime=null, str_endTime=null, str_vehicleNo = null,
            str_company_name=null, str_category ="frequent", CreationDate =null, ExpirationDate=null;
    java.sql.Date sqlDate, sql_startTime, sql_endTime;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String buildingName = null, flatNo = null,societyname=null, flat = null, wing = null;

    public static FeqcabFragment createInstance(String txt)
    {

        FeqcabFragment fragment = new FeqcabFragment();
//        fragment.mText = txt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cab_frequent,container,false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        //========================================================================

        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        buildingName = sp.getString("buildingName", null);
        flatNo = sp.getString("FlatNo", null);
        societyname = sp.getString("societyname", null);

        //========================================================================


//        daysOfWeek = v.findViewById(R.id.daysOfWeek);
        selectValidity = v.findViewById(R.id.selectValidity);
        timeSlot = v.findViewById(R.id.timeSlot);
        companyName = v.findViewById(R.id.companyName);
        ok = v.findViewById(R.id.okbtn);

        selectValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                SimpleDateFormat dd = new SimpleDateFormat("dd");
                                SimpleDateFormat dm = new SimpleDateFormat("MM");
                                SimpleDateFormat dy = new SimpleDateFormat("yyyy");

                                int day = Integer.parseInt(dd.format(c
                                        .getTime()));
                                int month = Integer.parseInt(dm.format(c
                                        .getTime()));
                                int yearr = Integer.parseInt(dy.format(c
                                        .getTime()));

                                if (dayOfMonth < 10) {
                                    df = "0" + dayOfMonth;
                                } else {
                                    df = String.valueOf(dayOfMonth);
                                }
                                if ((monthOfYear + 1 < 10)) {
                                    mf = "0" + (monthOfYear + 1);
                                } else {
                                    mf = String.valueOf(monthOfYear + 1);
                                }
                                String selected_date = df + "-" + mf + "-" + year;
                                Log.e("selected date", "" + selected_date);

                                String current_date = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(yearr);
                                Log.e("selected date", "" + current_date);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                Date cur = new Date();
                                Date sel = new Date();
                                try {
                                    cur = df.parse(current_date);
                                    sel = df.parse(selected_date);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
//                                date.setText(selected_date);
                                if (sel.after(cur) || cur.equals(sel)) {
                                    selectValidity.setText(selected_date);
                                } else {
                                    Toast.makeText(getContext(),
                                            "Selected Date Is Not Valid",
                                            Toast.LENGTH_SHORT).show();
                                    selectValidity.setText("");
                                }
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });


        ArrayList<String> purpose1 = new ArrayList<String>();
        purpose1.add("--Select Company Name--");
        purpose1.add("Ola");
        purpose1.add("Uber");
        purpose1.add("Others");

        company_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, purpose1);
        companyName.setAdapter(company_adapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExpirationDate = selectValidity.getText().toString().trim();
                str_company_name = companyName.getSelectedItem().toString().trim();



                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                String Date = dateFormat.format(new Date());
                String Time = dateFormat1.format(new Date());
                CreationDate = Date + " " + Time;

//                Date utilDate = null, date_startTime = null, date_endTime=null ;
//                try {
//                    utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(Date);
//                    sqlDate = new java.sql.Date(utilDate.getTime());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                if(!ExpirationDate.equalsIgnoreCase("")){
                                if(!str_company_name.equalsIgnoreCase("Select Company name")) {
                                    new GetLongLat("S").execute();
                                }else{
                                    Toast.makeText(getContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "Select validity", Toast.LENGTH_SHORT).show();
                            }

            }
        });


        return v;
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

                    if (flag.equalsIgnoreCase("S")){

                        PreparedStatement stmt = conn.prepareStatement("insert into CabEntry" +
                                "(StartDate, ExpirationDate, StartTime, EndTime, VehicleNo," +
                                " CompanyName, Category, SocietyName, OwnerFlatNo, OwnerBuildingName," +
                                " EntryAt)" + " values(?,?,?,?,?,?,?,?,?,?,?)");

                        stmt.setString(1,CreationDate );
                        stmt.setString(2, ExpirationDate);
                        stmt.setString(3, CreationDate);
                        stmt.setString(4, CreationDate);
                        stmt.setString(5, str_vehicleNo);
                        stmt.setString(6, str_company_name);
                        stmt.setString(7, str_category);
                        stmt.setString(8, societyname);
                        stmt.setString(9, wing+flat);
                        stmt.setString(10, buildingName);
                        stmt.setString(11,CreationDate );

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

    private void showName(ResultSet rs, ResultSet rs1, String flag){
        try {
            if (flag.equalsIgnoreCase("S")) {
                try {
                    Toast.makeText(getContext(), "Cab Entry Successfully !", Toast.LENGTH_SHORT).show();

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

//-------------------------------------------------------------------------------------------



}