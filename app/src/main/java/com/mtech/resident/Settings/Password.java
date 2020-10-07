package com.mtech.resident.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Password extends AppCompatActivity {
    EditText currentpass,newpass,confirmpass;
    String currentp=null,newp=null,confirmp=null,uname=null,pass=null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //========================================================================
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        pass= sp.getString("pass",null);
        uname= sp.getString("uname",null);

        //========================================================================
        currentpass=(EditText) findViewById(R.id.currentpass);
        newpass=(EditText) findViewById(R.id.newpass);
        confirmpass=(EditText) findViewById(R.id.confirmpass);
    }

    public void changepass(View view){
        currentp= currentpass.getText().toString();
        newp= newpass.getText().toString();
        confirmp= confirmpass.getText().toString();
        if(currentp.equalsIgnoreCase(pass) ) {
            if (newp.length() >= 6){
                if (newp.equalsIgnoreCase(confirmp)) {

                    new GetLongLat("U").execute();

                } else {
                    Toast.makeText(this, "Confirm Password did not match !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Password must contain atleast 6 characters !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Old Password is not correct !", Toast.LENGTH_SHORT).show();
        }

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
            progressDialog = new ProgressDialog(Password.this);
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
                Toast.makeText(Password.this, res, Toast.LENGTH_SHORT).show();
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


                    if (flag.equalsIgnoreCase("U")){

                        String sql = "update OwnerLoginDetails set Password=?  where UserId=?";

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);

                        preparedStatement.setString(1, newp);
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

        try {

            if (flag.equalsIgnoreCase("U")) {
                try {
                    Toast.makeText(this, "Saved Successfully !", Toast.LENGTH_SHORT).show();
                    currentpass.setText(null);
                    newpass.setText(null);
                    confirmpass.setText(null);
                    currentp=null;
                    newp=null;
                    confirmp=null;

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
