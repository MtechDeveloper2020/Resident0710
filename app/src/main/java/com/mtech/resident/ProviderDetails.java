package com.mtech.resident;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProviderDetails extends AppCompatActivity implements View.OnClickListener {
    String name = null, mob = null, purpose = null, date = null, vehicle = null, from = null,societyname=null;
    TextView Vname, mobile, Purp, inTime, VNo, Place;
    Bitmap visphoto, idsphoto;
    ImageView vPhoto, iPhoto, call;
    byte[] vbyteimage = null;
    Button GiveRatings;
    RatingBar quality, price,punctuality;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_provider_details);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        societyname = sp.getString("societyname",null);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mob = intent.getStringExtra("mobile");
        purpose = intent.getStringExtra("purpose");
        visphoto = (Bitmap) intent.getParcelableExtra("visitorphoto");
        new ActiveStatus("S").execute();

        Vname = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobileNo);
        Purp = (TextView) findViewById(R.id.purpose);
        vPhoto = (ImageView) findViewById(R.id.icon);
        call = (ImageView) findViewById(R.id.call);
        quality = (RatingBar) findViewById(R.id.quality);
        price = (RatingBar) findViewById(R.id.price);
        punctuality = (RatingBar) findViewById(R.id.punctuality);

        GiveRatings = (Button) findViewById(R.id.giveRating);
        GiveRatings.setOnClickListener(this);

        Vname.setText(name);
        mobile.setText(mob);
        Purp.setText(purpose);


        if (visphoto != null){
            vPhoto.setImageBitmap(visphoto);

//            Bitmap bitmap = ((BitmapDrawable) vPhoto.getDrawable()).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            vbyteimage = stream.toByteArray();
        }

        vPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                SendGetStartedNotification(ParseUser user);
            }
        });
        call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Uri number = Uri.parse("tel:" + mob);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        //---------------------------------------------------------------------------------
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

    @Override
    public void onClick(View view){
        Intent i = new Intent(this,OwnerRatings.class);
        i.putExtra("name",name);
        i.putExtra("purpose", purpose);
        startActivity(i);
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
            progressDialog = new ProgressDialog(ProviderDetails.this);
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
                Toast.makeText(ProviderDetails.this, res, Toast.LENGTH_SHORT).show();
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

                    if (flag.equalsIgnoreCase("S")){

                        String query = "SELECT TOP 1 * FROM ServiceprovidersDetails WHERE SPName='"+name+"' AND SocietyName='"+societyname+"' AND Category='"+purpose+"' " ;
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

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {
            String q=null, pr=null, pun=null,ownercount=null;
            int qua=0, prc=0,punc=0, oc =0;
            if (flag.equalsIgnoreCase("S")) {
                try {

                    while(rs.next()){
                        q= rs.getString("Quality");
                        pr= rs.getString("Price");
                        pun= rs.getString("Punctuality");
                        ownercount= rs.getString("OwnerCount");

                    }
                    qua= Integer.parseInt(q);
                    prc = Integer.parseInt(pr);
                    punc= Integer.parseInt(pun);
                    oc = Integer.parseInt(ownercount);
                    int qq= qua/oc;
                    int pp= prc/oc;
                    int ppu= punc/oc;
                   quality.setRating(qq);
                   price.setRating(pp);
                   punctuality.setRating(ppu);
//                   Toast.makeText(this, ""+totq+ totpr+totpun, Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.toString();
                    Toast.makeText(ProviderDetails.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ProviderDetails.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }



}
