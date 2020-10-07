package com.mtech.resident.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mtech.resident.AddVehicleAdapter;
import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AddVehicle extends Fragment {
    ListView list;
    EditText vehicleNo;
    Button search;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname=null,buildingName=null,flatNo=null,wing=null,vehNo=null,vType="4",vnn=null;
    ToggleButton tbutton;
    ArrayList<String> VehicleType;
    ArrayList<String> vNo;
    AddVehicleAdapter vehicleAdapter = null;
    public AddVehicle() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_vehicleadd, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();


        vNo = new ArrayList<>();
        VehicleType = new ArrayList<>();
        //========================================================================
        uname= sp.getString("uname",null);
        buildingName= sp.getString("buildingName",null);
        flatNo= sp.getString("FlatNo",null);
        wing= sp.getString("wing",null);
        //========================================================================
        vehicleNo = (EditText) view.findViewById(R.id.vehicleNo);
        tbutton = (ToggleButton)view.findViewById(R.id.toggleButton1);
        search = (Button) view.findViewById(R.id.addVehicle);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicle= vehicleNo.getText().toString();
                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(vehicle);
                Pattern pattern1 = Pattern.compile("(^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$)");
                Matcher matcher1 = pattern1.matcher(vehicle);
                Pattern pattern2 = Pattern.compile("(^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{3}$)");
                Matcher matcher2 = pattern2.matcher(vehicle);
                if(matcher.find() || matcher1.find() || matcher2.find()){
//                    Toast.makeText(getContext(), "STiLL TO Do", Toast.LENGTH_SHORT).show();
                    vehNo=vehicleNo.getText().toString();

                    new GetLongLat("S").execute();

                }else{
                    Toast.makeText(getContext(), "Not MatcheD !", Toast.LENGTH_SHORT).show();
                }

//                if (matcher.find()) {
//                    Toast.makeText(getContext(), "1: "+matcher.group(1), Toast.LENGTH_SHORT).show();
//                    System.out.println(matcher.group(1));
//                }

            }
        });
        list = (ListView) view.findViewById(R.id.list);
        new GetLongLat("E").execute();
        tbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(tbutton.isChecked())
                {
                   vType="2";
                }
                else {
                    vType="4";
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		
            @Override
            public void onItemClick(AdapterView<?> parent, View rview, final int position, long id) {
                // TODO Auto-generated method stub
//                Bitmap listpoint = photo.get(position);
//                Toast.makeText(getContext(), ""+listpoint, Toast.LENGTH_SHORT).show();
                final ImageView i = (ImageView) rview.findViewById(R.id.delete);

                i.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        vnn= vNo.get(position);
                        new GetLongLat("A").execute();

                    }
                });
            }
        });
        return view;
    }

//==================================================================================================
    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs=null, rs1=null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res){
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){

                showName(rs,rs1, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params){
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null){
                    ret = "Internet connection Error";
                } else {
                     if (flag.equalsIgnoreCase("S")){

                        PreparedStatement stmt = conn.prepareStatement("insert into VehicleList" + "(OwnerName,FlatNo, VehicleType, Wing, BuildingName, OwnerType, VehicleNumber)" + " values(?,?,?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, uname);
                        stmt.setString(2, flatNo);
                        stmt.setString(3, vType);
                        stmt.setString(4, wing);
                        stmt.setString(5, buildingName);
                        stmt.setString(6, "O");
                        stmt.setString(7, vehNo);

                        int i = stmt.executeUpdate();
                        ret = "" + i;

                    }
                    else  if (flag.equalsIgnoreCase("E")){
                         String query = "SELECT VehicleType, VehicleNumber FROM VehicleList WHERE Wing='"+wing+"' AND FlatNo = '"+flatNo+"' ";
                         Statement stmt = conn.createStatement();
                         rs = stmt.executeQuery(query);
                         ret = "1";
                     }

                  else  if (flag.equalsIgnoreCase("A")){
                        String sql = "DELETE FROM VehicleList WHERE VehicleNumber='"+vnn+"'";

                        Statement  stmt   = conn.createStatement();

                        int i = stmt.executeUpdate(sql);
                        ret = "" + i;
                    }
                }

            } catch (Exception ex){
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs,ResultSet rs1, String flag){
        try {

            if (flag.equalsIgnoreCase("S")){
                try {
                    Toast.makeText(getContext(), "Saved !", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(AddVehicle.this).attach(AddVehicle.this).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           else if (flag.equalsIgnoreCase("E")) {
                try {
                    while (rs.next()){
                        try{
                            String b=rs.getString("VehicleNumber");
                            String a= rs.getString("VehicleType");
                            vNo.add(b);
                            VehicleType.add(a);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    vehicleAdapter=new AddVehicleAdapter(getActivity(), vNo,VehicleType);
                    list.setAdapter(vehicleAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (flag.equalsIgnoreCase("A")){
                try {
                    Toast.makeText(getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(AddVehicle.this).attach(AddVehicle.this).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //-------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
