package com.mtech.resident;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cd02407 on 12/12/2018.
 */

  public class OwnerRatings extends Activity implements View.OnClickListener {
    RatingBar price,quality,punctuality;
    Button button;
    String name=null,purpose=null,societyname=null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView ratingName;
    String price1=null,punctuality1=null,quality1=null, addprice = null, addpunctuality = null, addquality = null, addsociety=null
            , addoverall= null, addownercount=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_owner_ratings);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();

        societyname = sp.getString("societyname",null);
        price=(RatingBar)findViewById(R.id.price);
        quality=(RatingBar)findViewById(R.id.quality);
        punctuality=(RatingBar)findViewById(R.id.punctuality);
        button=(Button)findViewById(R.id.giveRating);
        ratingName=(TextView)findViewById(R.id.ratingName);
        button.setOnClickListener(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        purpose = intent.getStringExtra("purpose");
        ratingName.setText("Rating for "+name);
    }

    @Override
    public void onClick(View view) {
         price1 = String.valueOf(price.getRating());
         quality1 = String.valueOf(quality.getRating());
         punctuality1 = String.valueOf(punctuality.getRating());
        new ActiveStatus("Rate").execute();
//        Toast.makeText(getApplicationContext(),quality1 + price1 +punctuality1, Toast.LENGTH_LONG).show();
    }

    public class ActiveStatus extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs = null, rs1 = null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public ActiveStatus(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(OwnerRatings.this);
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
                Toast.makeText(OwnerRatings.this, res, Toast.LENGTH_SHORT).show();
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

                    if (flag.equalsIgnoreCase("Rate")){

                        String query = "SELECT TOP 1 * FROM ServiceprovidersDetails WHERE SPName='"+name+"' AND SocietyName='"+societyname+"' AND Category='"+purpose+"' " ;
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";

                } else if (flag.equalsIgnoreCase("Update")) {
                    String sql = "UPDATE dbo.ServiceprovidersDetails SET Quality = ?, Price=?,Punctuality=?, SocietyRating=?," +
                            " OverAllRating=? , OwnerCount= ? WHERE SPName = '"+name+"'  AND SocietyName='"+societyname+"' AND Category='"+purpose+"' " ;
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,addquality);
                    preparedStatement.setString(2, addprice);
                    preparedStatement.setString(3,addpunctuality);
                    preparedStatement.setString(4,"4");
                    preparedStatement.setString(5,"4");
                    preparedStatement.setString(6,addownercount);

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
            String q=null, pr=null, pun=null, societyr=null,overallr=null,ownercount=null;
            int totq=0,totpr=0,totpun=0;
            if (flag.equalsIgnoreCase("Rate")) {
                try {

                    while(rs.next()){
                       q= rs.getString("Quality");
                         pr= rs.getString("Price");
                         pun= rs.getString("Punctuality");
                         societyr= rs.getString("SocietyRating");
                         overallr= rs.getString("OverallRating");
                         ownercount= rs.getString("OwnerCount");
                    }
                    if(ownercount != null){

                    }else{
                        ownercount = "1";
                    }
                    int add =Integer.parseInt(ownercount)+1;
                    addownercount= String.valueOf(add);

//                    Toast.makeText(this, ""+q+pr+pun+societyr+overallr, Toast.LENGTH_SHORT).show();
                    float a = quality.getRating();
                    float aa= price.getRating();
                    float aaa = punctuality.getRating();
//                    Toast.makeText(this, ""+a+aa+aaa, Toast.LENGTH_SHORT).show();

                    totq= Integer.parseInt(q) + Math.round(a);
                    totpr= Integer.parseInt(pr) + Math.round(aa);
                    totpun= Integer.parseInt(pun) + Math.round(aaa);
                    addprice= String.valueOf(totpr);
                    addquality= String.valueOf(totq);
                    addpunctuality= String.valueOf(totpun);

//                    int adds= Integer.parseInt(societyr)+totq+totpr+totpun;

                    new ActiveStatus("Update").execute();

//                   Toast.makeText(this, ""+totq+ totpr+totpun, Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.toString();
                    Toast.makeText(OwnerRatings.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            else if(flag.equalsIgnoreCase("Update")){
                try {
                    Toast.makeText(this, "Ratings Submitted !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,ServiceProvider.class);
                    startActivity(i);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OwnerRatings.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
