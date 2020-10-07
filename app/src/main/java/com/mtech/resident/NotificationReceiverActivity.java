package com.mtech.resident;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificationReceiverActivity extends Activity {
    String msgFrom = null, msgVeh = null, message = null, flat = null, wing = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        //==========================================================================================
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);

        //==========================================================================================

        Intent i = getIntent();
        msgFrom = i.getStringExtra("msgFrom");
        msgVeh = i.getStringExtra("msgVeh");
        message = i.getStringExtra("message");

        //==========================================================================================

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vehicle Alert");
        alertDialogBuilder.setMessage("Message From : " + msgFrom + "\n" +
                "Vehicle Number : " + msgVeh + "\n" +
                "Message : " + message);
//        alertDialogBuilder.setNegativeButton("CANCEL",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
//                onDestroy();

                new GetLongLat("E").execute();

//                Log.d(TAG, sharedPreferences.getString("password", ""));
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //==============================================================================================
    public class GetLongLat extends AsyncTask<String, String, String>{
        Connection conn;
        ResultSet rs = null, rs1 = null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";

        public GetLongLat(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(NotificationReceiverActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res){
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){

                showName(rs, rs1, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
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
                    ret = "Error in connection with SQL server";
                } else {
                    if (flag.equalsIgnoreCase("E")) {

                        String sql = "update MessageList set SR=?  where VehicleNumber=?  and Message=? and MessageFrom=? and MessageTo=?";

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "1");
                        preparedStatement.setString(2, msgVeh);
                        preparedStatement.setString(3, message);
                        preparedStatement.setString(4, msgFrom);
                        preparedStatement.setString(5, wing + flat);
                        int i = preparedStatement.executeUpdate();
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
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NotificationReceiverActivity.this, SelectionActivity.class);

                startActivity(i);
                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //==============================================================================================

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

}

//==================================================================================================