package com.mtech.resident.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;
import com.mtech.resident.SelectionActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;


public class PinChange extends AppCompatActivity {

    Button generatePin,save;
    EditText editTextPin;
    TextView curPin;
    String pin=null;
    String uname = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_change);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        uname= sp.getString("uname",null);

        generatePin = (Button) findViewById(R.id.generatePin);
        save= (Button) findViewById(R.id.save);
        editTextPin=(EditText) findViewById(R.id.editTextPin);
        curPin=(TextView) findViewById(R.id.curPin);
        new GetLongLat("E").execute();
        generatePin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Random r = new Random();
                pin = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
                editTextPin.setText(""+pin);
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pin= editTextPin.getText().toString();
                new GetLongLat("U").execute();
            }
        });
    }

    //--------------------------------------------------------------------------------------------------

    public class GetLongLat extends AsyncTask<String, String, String> {
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
            progressDialog = new ProgressDialog(PinChange.this);
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
                Toast.makeText(PinChange.this, res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }
        @Override
        protected String doInBackground(String... params){
            try {
                conn = dbConnection.connectionclass(); //Connect to database

                if(conn == null){
                    ret = "Internet connection Error";
                } else{

                    if(flag.equalsIgnoreCase("U")){

                        String sql = "update OwnerLoginDetails set Pin=?  where UserId=?";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, pin);
                        preparedStatement.setString(2, uname);
                        int i = preparedStatement.executeUpdate();
                        ret = "" + i;
                    }
                   else if (flag.equalsIgnoreCase("E")){
                        String query = "Select * from OwnerLoginDetails where UserId='"+uname+"' ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
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

            if (flag.equalsIgnoreCase("U")){

                try {
                    Toast.makeText(PinChange.this, "Pin Ready to access !", Toast.LENGTH_SHORT).show();
                    editTextPin.setText("");
                    pin = null;
                    Intent i = new Intent(this, SelectionActivity.class);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          else if(flag.equalsIgnoreCase("E")){
                String s="0000";
                try {
                    while(rs.next()){
                      s= rs.getString("Pin");
                      curPin.setText(""+s);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


//--------------------------------------------------------------------------------------------------
}
