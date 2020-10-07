package com.mtech.resident.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Profile extends AppCompatActivity {
    TextView building,owner,flatNo,userid;
    EditText emailId;
    CheckBox useEmail;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname=null,buildingName=null,flat=null,wing=null,vehNo=null,email=null,emailid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //========================================================================

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        uname= sp.getString("uname",null);
        buildingName= sp.getString("buildingName",null);
        flat= sp.getString("FlatNo",null);
        wing= sp.getString("wing",null);
//        emailid= sp.getString("email",nu ll);
        //========================================================================
        building=(TextView)findViewById(R.id.buildingName);
        owner=(TextView)findViewById(R.id.owner);
        flatNo=(TextView)findViewById(R.id.flatNo);
        userid=(TextView)findViewById(R.id.userid);
        emailId=(EditText) findViewById(R.id.emailId);
        useEmail=(CheckBox) findViewById(R.id.useEmail);

        building.setText(uname);
        flatNo.setText(wing+flat);
        userid.setText(uname);
        building.setText(buildingName);

        new GetLongLat("A").execute();
    }
//========================================================================

    public void save(View view){
        email= emailId.getText().toString();
        new GetLongLat("U").execute();
    }

//========================================================================

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
            progressDialog = new ProgressDialog(Profile.this);
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
                Toast.makeText(Profile.this, res, Toast.LENGTH_SHORT).show();
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

                    if (flag.equalsIgnoreCase("A")){
                        String query = "Select OwnerName FROM OwnerDetails where FlatNo='"+flat+"' and Wing='"+wing+"' and BuildingName='"+buildingName+"' ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                    else if (flag.equalsIgnoreCase("U")){

                            String sql = "update OwnerLoginDetails set EmailId=?  where UserId=?";

                            PreparedStatement preparedStatement = conn.prepareStatement(sql);

                            preparedStatement.setString(1, email);
                            preparedStatement.setString(2, uname);


                            int i = preparedStatement.executeUpdate();
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
        String name=null;
        try {

            Boolean status = false;
            if (flag.equalsIgnoreCase("A")) {
                try {

                    while (rs.next()) {
                        name = rs.getString("OwnerName");

                    }
                    owner.setText(name);


                } catch (Exception e) {
                    e.toString();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                }
            }
               else if (flag.equalsIgnoreCase("U")) {
                    try {
                        Toast.makeText(this, "Saved Successfully !", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.toString();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                    }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
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

