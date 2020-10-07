package com.mtech.resident.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;
import com.mtech.resident.SelectionActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchVehicle extends Fragment {

    EditText vehicleNo,message;
    Button search,sendMsg;
    String vehNo=null,flat=null,wing=null,ownerName=null,vType=null,ON,FN,VN,VT,W,messageTo;
    TextView flatNo,displayVN,vehType,title,alterText;
    LinearLayout LL1,LL3;
    String CreationDate=null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    EditText et_name;
    public SearchVehicle(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_vehiclesearch, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        //========================================================================

        flat= sp.getString("FlatNo",null);
        wing= sp.getString("wing",null);
        //========================================================================

        vehicleNo = (EditText) view.findViewById(R.id.vehicleNo);
        flatNo = (TextView) view.findViewById(R.id.flatNo);
        displayVN = (TextView) view.findViewById(R.id.displayVN);
        vehType = (TextView) view.findViewById(R.id.vType);
        alterText = (TextView) view.findViewById(R.id.alterText);
        title = (TextView) view.findViewById(R.id.title);
        LL1 = (LinearLayout) view.findViewById(R.id.LL1);
        LL3 = (LinearLayout) view.findViewById(R.id.ll3);

        search = (Button) view.findViewById(R.id.search);
        sendMsg = (Button) view.findViewById(R.id.sendMsg);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicle = vehicleNo.getText().toString();
                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(vehicle);
                Pattern pattern1 = Pattern.compile("(^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$)");
                Matcher matcher1 = pattern1.matcher(vehicle);
                Pattern pattern2 = Pattern.compile("(^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{3}$)");
                Matcher matcher2 = pattern2.matcher(vehicle);
                if (matcher.find() || matcher1.find() || matcher2.find()) {
//                    Toast.makeText(getContext(), "STiLL TO Do", Toast.LENGTH_SHORT).show();
                    vehNo = vehicleNo.getText().toString();
                    new GetLongLat("E").execute();
                } else {
                    Toast.makeText(getContext(), "Not MatcheD !", Toast.LENGTH_SHORT).show();
                }

//                if (matcher.find()) {
//                    Toast.makeText(getContext(), "1: "+matcher.group(1), Toast.LENGTH_SHORT).show();
//                    System.out.println(matcher.group(1));
//                }

            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                et_name = (EditText) dialogView.findViewById(R.id.et_name);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                // Set positive/yes button click listener
                btn_positive.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        // Dismiss the alert dialog
                        dialog.cancel();
                        String name = et_name.getText().toString();
                        messageTo= et_name.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                        String Date = dateFormat.format(new Date());
                        String Time = dateFormat1.format(new Date());
                        CreationDate = Date + " " + Time;


                        new GetLongLat("D").execute();
                        // Say hello to the submitter
//                        tv_message.setText("Hello " + name + "!");
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
//                        Toast.makeText(getContext(),
//                                "No button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                // Display the custom alert dialog on interface
                dialog.show();
            }
        });
        return view;
    }

    private void showName(ResultSet rs,ResultSet rs1, String flag){
        try {
            if (flag.equalsIgnoreCase("E")) {
                try {
                    if (rs.next()){
                        try{
                            title.setVisibility(View.VISIBLE);
                            LL1.setVisibility(View.VISIBLE);
                            LL3.setVisibility(View.GONE);
                            ON = rs.getString("OwnerName");
                            FN = rs.getString("FlatNo");
                             W= rs.getString("Wing");
                             VN= rs.getString("VehicleNumber");
                             VT= rs.getString("VehicleType");

                            flatNo.setText("Flat No : "+W+FN+"-"+ ON+"(Owner)");
                            displayVN.setText("Vehicle Number : "+VN);
                            if(VT.equalsIgnoreCase("2")){
                                vehType.setBackgroundResource(R.drawable.bike);
                            }else{
                                vehType.setBackgroundResource(R.drawable.car);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        alterText.setText("Oops No result found for vehicle Number '"+vehNo+"'");
                        LL3.setVisibility(View.VISIBLE);
                        LL1.setVisibility(View.GONE);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          else if (flag.equalsIgnoreCase("D")) {
                try {

                    Toast.makeText(getContext(), "Notification Sent Successfully !", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SelectionActivity) getActivity())
                .setActionBarTitle("Search Vehicle");


    }

    //==============================================================================================

//-------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();
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
                      if (flag.equalsIgnoreCase("E")){
                        String query = "SELECT od.OwnerName,vl.FlatNo,vl.Wing,vl.VehicleNumber,vl.VehicleType FROM OwnerDetails AS od  " +
                                "INNER JOIN VehicleList AS vl " +
                                "ON od.FlatNo = vl.FlatNo AND od.Wing = vl.Wing WHERE vl.VehicleNumber LIKE '%"+vehNo+"%' ";

                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }
                   else if (flag.equalsIgnoreCase("D")){
                        PreparedStatement stmt = conn.prepareStatement("insert into MessageList" + "(MessageFrom,MessageTo, Message, SendOn, SR, VehicleNumber)" + " values(?,?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, wing+flat);
                        stmt.setString(2, W+FN);
                        stmt.setString(3, messageTo);
                        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(CreationDate));
                        stmt.setString(5, "0");
                        stmt.setString(6, vehNo);

                        int i = stmt.executeUpdate();
                        ret = "" + i;
                    }

                }
            } catch (Exception ex){
                ret = ex.getMessage();
            }
            return ret;
        }
    }
}
