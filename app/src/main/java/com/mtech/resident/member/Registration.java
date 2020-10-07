package com.mtech.resident.member;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.DBConnection;
import com.mtech.resident.LoginActivity;
import com.mtech.resident.R;
import com.mtech.resident.SelectionActivity;
import com.mtech.resident.VehicleMessageService;
import com.mtech.resident.network.ConnectionDetector;
import com.mtech.resident.sms.JSONParser_Post;
import com.mtech.resident.sms.Model_URIs;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;


public class Registration extends AppCompatActivity {

    EditText username, password,repassword,mobileno, wingName, location, flatno ;
    Button login;
    String uname = null, pass = null,otp=null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String buildingName = null, wing = null, FlatNo = null, email = null, societyname = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String bname=null,wng=null,sname=null,mobno=null,fno=null,upass=null,uid=null,pin=null;
    String TransactionLog_GetJSON;
    Model_URIs URLs = new Model_URIs();
    JSONParser_Post jsnp_post = new JSONParser_Post();
    Spinner society,building;
    ArrayAdapter<String> society_adapter,building_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        cd = new ConnectionDetector(Registration.this);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.ownerName);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.reenterPassword);
        mobileno = (EditText) findViewById(R.id.mobileNo);
        society = (Spinner) findViewById(R.id.societyName);
        building = (Spinner) findViewById(R.id.buildingName);
        wingName = (EditText) findViewById(R.id.wing);
        flatno = (EditText) findViewById(R.id.flat);

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();

        new GetLongLat("SB").execute();

    }
    public void login(View view) {
       Intent i = new Intent(this, LoginActivity.class);
       startActivity(i);
       finish();
    }
    public void register(View view) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent == true){
            uid = username.getText().toString();
            upass = password.getText().toString();
            bname = building.getSelectedItem().toString();
            wng = wingName.getText().toString();
            fno = flatno.getText().toString();
            mobno = mobileno.getText().toString();
            sname = society.getSelectedItem().toString();

            if (!uid.equalsIgnoreCase("")){
                if(!upass.equalsIgnoreCase("")){
                    Random r = new Random();
                    pin = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));

                    new GetLongLat("S").execute();
                    new GetLongLat("SS").execute();

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
            progressDialog = new ProgressDialog(Registration.this);
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
                Toast.makeText(Registration.this, res, Toast.LENGTH_SHORT).show();
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
                        String query = "Select BuildingName ,Wing, FlatNo,UserId,SocietyName from OwnerLoginDetails where (UserId='" + uname + "' OR" +
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
                   else if (flag.equalsIgnoreCase("S")){

                        PreparedStatement stmt = conn.prepareStatement("insert into OwnerLoginDetails" + "(BuildingName ,Wing, FlatNo,UserId,SocietyName,Password,MobileNo, AdminApprove, Pin)" + " values(?,?,?,?,?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, bname);
                        stmt.setString(2, wng);
                        stmt.setString(3, fno);
                        stmt.setString(4, uid);
                        stmt.setString(5, sname);
                        stmt.setString(6, upass);
                        stmt.setString(7, mobno);
                        stmt.setString(8, "0");
                        stmt.setString(9, pin);
                        int i = stmt.executeUpdate();
                        ret = "" + i;

                    }
                    else if (flag.equalsIgnoreCase("SS")){

                        PreparedStatement stmt = conn.prepareStatement("insert into OwnerDetails" + "(BuildingName ,Wing, FlatNo,OwnerName,SocietyName,OwnerType)" + " values(?,?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, bname);
                        stmt.setString(2, wng);
                        stmt.setString(3, fno);
                        stmt.setString(4, uid);
                        stmt.setString(5, sname);
                        stmt.setString(6, "Owner");

                        int i = stmt.executeUpdate();
                        ret = "" + i;

                    }

                   else if(flag.equalsIgnoreCase("SB")){

                        String query = "Select DISTINCT SocietyName from OwnerDetails";
                        String query1 = "Select DISTINCT BuildingName from OwnerDetails";
                        Statement stmt = conn.createStatement();
                        Statement stmt1 = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        rs1 = stmt1.executeQuery(query1);
                        ret = "1";
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
            if (flag.equalsIgnoreCase("A")){
                try {
                    while (rs.next()) {
                        buildingName = rs.getString("BuildingName");
                        wing = rs.getString("Wing");
                        FlatNo = rs.getString("FlatNo");
                        uname = rs.getString("UserId");
                        societyname = rs.getString("SocietyName");
                        status = true;
                    }
                    if (status){
                        Toast.makeText(Registration.this, "Owner Authentication Successful !", Toast.LENGTH_SHORT).show();
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
                        Intent i = new Intent(Registration.this, SelectionActivity.class);
                        i.putExtra("username", uname);
                        i.putExtra("password", pass);
                        i.putExtra("BuildingName", buildingName);
                        i.putExtra("FlatNo", FlatNo);
                        i.putExtra("societyname", societyname);
                        i.putExtra("Wing", wing);
                        startActivity(i);
                        finish();
                    } else{
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.toString();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            if (flag.equalsIgnoreCase("S")){
                try {
                    Toast.makeText(Registration.this, " Registration Successful !", Toast.LENGTH_SHORT).show();

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent == true){

                        JSONObject jsonObj = new JSONObject();

                        try {

                            jsonObj.put("msg", pin+" is your Owner verification code. ");
                            jsonObj.put("mobile", mobno);
                            jsonObj.put("Respose", "TRUE");

                            TransactionLog_GetJSON = jsonObj.toString();

                            new Perform_log(URLs.getPerform_URI(), TransactionLog_GetJSON).execute();
                            //  new Perform_log("", TransactionLog_GetJSON).execute();

                        }
                        catch (final Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run(){
                            Intent intent = new Intent(Registration.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);



//                    Intent i = new Intent(this,LoginActivity.class);
//                    startActivity(i);
//                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          else if (flag.equalsIgnoreCase("SB")){
                try {
                    ArrayList<String> purpose = new ArrayList<String>();
                    purpose.add("----Select Society----");
                    while (rs.next()){
                        purpose.add(rs.getString("SocietyName"));
                    }
                    society_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, purpose);
                    society.setAdapter(society_adapter);

                    ArrayList<String> build = new ArrayList<String>();
                    build.add("----Select Building----");
                    while (rs1.next()){
                        build.add(rs1.getString("BuildingName"));
                    }
                    building_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, build);
                    building.setAdapter(building_adapter);



//                    new GetLongLat("android_id").execute();
                } catch (Exception e){
                    e.toString();
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);
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

//-------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //======================================== Perform_log Task ==================================
    public class Perform_log extends AsyncTask<Void, Integer, Void> {
        //        ProgressDialog progressDialog;
        String final_out1 = "";
        String URL = "";
        String jsonString = "";

        public Perform_log(String url, String json){
            this.URL = url;
            this.jsonString = json;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                final_out1 = jsnp_post.makeHttpRequest(URL, "POST", jsonString);

            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getApplicationContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){

//            progressDialog.dismiss();
            try {
                if (final_out1.equalsIgnoreCase("")) {
                    Toast.makeText(Registration.this, "OTP not Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    //attribute success then save else error message
                    Toast.makeText( Registration.this, ""+final_out1, Toast.LENGTH_SHORT).show();
//                    new  GetLongLat("C").execute();
//                    verifyotpbutton.setVisibility(View.VISIBLE);
//                    verifyotp.setVisibility(View.VISIBLE);
//                    phone.setEnabled(false);
//                    mCbShowPwd.setVisibility(View.VISIBLE);
                }
//                    Toast.makeText(getApplicationContext(), final_out1, Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute(){
            // TODO Auto-generated method stub
//            progressDialog = new ProgressDialog( SavedScreen.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
            //super.onPreExecute();
        }
    }

//================   4-Digit Pin Code  =============================================================

}
