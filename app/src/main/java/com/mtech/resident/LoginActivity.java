package com.mtech.resident;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.member.Registration;
import com.mtech.resident.network.ConnectionDetector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    String uname = null, pass = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String buildingName = null, wing = null, FlatNo = null, email = null, societyname = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy =
             new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        cd = new ConnectionDetector(LoginActivity.this);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    public void register(View view) {
       Intent i = new Intent(this, Registration.class);
       startActivity(i);
       finish();
    }
    public void Login(View view) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent == true){
            uname = username.getText().toString();
            pass = password.getText().toString();
            email = username.getText().toString();

            if (!uname.equalsIgnoreCase("")){
                if(!pass.equalsIgnoreCase("")){
                    new GetLongLat("A").execute();
                } else {
                    Toast.makeText(this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Username ", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Internet Connection Errror !", Toast.LENGTH_SHORT).show();
        }
    }
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
            progressDialog = new ProgressDialog(LoginActivity.this);
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
                Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }
        @Override
        protected String doInBackground(String... params){
            try {
                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null) {
                    ret = "Internet connection Error";
                } else {
                    if (flag.equalsIgnoreCase("A")){
                        String query = "Select BuildingName ,Wing, FlatNo,UserId,SocietyName, AccountActive, AdminApprove from OwnerLoginDetails where (UserId='" + uname + "' OR" +
                                " EmailId='" + email + "' ) and password='" + pass + "' ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }
                    else if (flag.equalsIgnoreCase("Active")){

                        String sql = "update OwnerLoginDetails set AccountActive=?  where UserId=?";

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);

                        preparedStatement.setString(1, "1");
                        preparedStatement.setString(2, uname);

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

    private void showName(ResultSet rs, ResultSet rs1, String flag){
        try {

            Boolean status = false;
            String adminApprove=null;
            if (flag.equalsIgnoreCase("A")){
                try {
                    while (rs.next()) {
                        buildingName = rs.getString("BuildingName");
                        wing = rs.getString("Wing");
                        FlatNo = rs.getString("FlatNo");
                        uname = rs.getString("UserId");
                        societyname = rs.getString("SocietyName");
                        adminApprove = rs.getString("AdminApprove");
                        if(adminApprove.equalsIgnoreCase("1")) {
                            status = true;
                        } else {
                            status= false;
                        }
                    }
                    if (status){
                        Toast.makeText(LoginActivity.this, "Owner Authentication Successful !", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("sf", true);
                        editor.putString("uname", uname);
                        editor.putString("email", email);
                        editor.putString("pass", pass);
                        editor.putString("buildingName", buildingName);
                        editor.putString("FlatNo", FlatNo);
                        editor.putString("wing", wing);
                        editor.putString("societyname", societyname);
                        editor.putString("notify", "1");
                        editor.putString("sound_notify", "1");
                        editor.putString("vibrate_notify", "1");
                        editor.commit();
                        startService(new Intent(this, VehicleMessageService.class));
                        new GetLongLat("Active").execute();
                        Intent i = new Intent(LoginActivity.this, SelectionActivity.class);
                        i.putExtra("username", uname);
                        i.putExtra("password", pass);
                        i.putExtra("BuildingName", buildingName);
                        i.putExtra("FlatNo", FlatNo);
                        i.putExtra("societyname", societyname);
                        i.putExtra("Wing", wing);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.toString();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setMessage("Are you sure exit from application?");
        alertDialogBuilder.setPositiveButton("YES, EXIT",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//==================================================================================================
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
