package com.mtech.resident.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;


public class SecurityAlertDialog extends DialogFragment {

    ImageView fire, lift, animal, fight;
    Button raise_alarm;
    LinearLayout firell, liftll, animalll, fightll;
    String selected_value =null;
    Button raiseAlarm;
    String sosDetails = null;
    String uname = null, pass = null, buildingName = null, flatNo = null, wing = null,societyname=null,
            flat = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    java.sql.Date sqlDate, sql_startTime, sql_endTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.securityalert_dialog,container,false);
        fire = view.findViewById(R.id.fire);
        lift = view.findViewById(R.id.lift);
        animal = view.findViewById(R.id.animal);
        fight = view.findViewById(R.id.fight);
        firell = view.findViewById(R.id.ll1);
        liftll = view.findViewById(R.id.ll2);
        animalll = view.findViewById(R.id.ll3);
        fightll = view.findViewById(R.id.ll4);
        raiseAlarm = view.findViewById(R.id.freq);

        firell.setBackgroundColor(Color.LTGRAY);
        selected_value = "FIRE";
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        //========================================================================

        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        buildingName = sp.getString("buildingName", null);
        flatNo = sp.getString("FlatNo", null);
        societyname = sp.getString("societyname", null);

        //========================================================================

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firell.setBackgroundColor(Color.LTGRAY);
                liftll.setBackgroundColor(Color.WHITE);
                animalll.setBackgroundColor(Color.WHITE);
                fightll.setBackgroundColor(Color.WHITE);
                selected_value = "FIRE";
            }
        });
        lift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firell.setBackgroundColor(Color.WHITE);
                liftll.setBackgroundColor(Color.LTGRAY);
                animalll.setBackgroundColor(Color.WHITE);
                fightll.setBackgroundColor(Color.WHITE);
                selected_value = "LIFT";
            }
        });
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firell.setBackgroundColor(Color.WHITE);
                liftll.setBackgroundColor(Color.WHITE);
                animalll.setBackgroundColor(Color.LTGRAY);
                fightll.setBackgroundColor(Color.WHITE);
                selected_value = "ANIMAL";
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firell.setBackgroundColor(Color.WHITE);
                liftll.setBackgroundColor(Color.WHITE);
                animalll.setBackgroundColor(Color.WHITE);
                fightll.setBackgroundColor(Color.LTGRAY);
                selected_value = "FIGHT";
            }
        });

        raiseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sosDetails = wing + flatNo;
                new GetLongLat("E").execute();
            }
        });

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

                    if (flag.equalsIgnoreCase("E")) {
                        PreparedStatement stmt = conn.prepareStatement("insert into OwnerSOS" + "(OwnerDetails,BuildingName,SocietyName,Active, Category)" + " values(?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, sosDetails);
                        stmt.setString(2, buildingName);
                        stmt.setString(3, societyname);
                        stmt.setString(4, "0");
                        stmt.setString(5, selected_value);
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
            if (flag.equalsIgnoreCase("E")) {
                try {
                    Toast.makeText(getContext(), "Alarm Raised!", Toast.LENGTH_SHORT).show();

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
