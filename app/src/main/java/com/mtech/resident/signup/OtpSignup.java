package com.mtech.resident.signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mtech.resident.R;
import com.mtech.resident.network.ConnectionDetector;
import com.mtech.resident.sms.JSONParser_Post;
import com.mtech.resident.sms.Model_URIs;
import org.json.JSONObject;

/**
 * Created by cd02407 on 4/5/2018.
 */

public class OtpSignup extends Activity {

    String TransactionLog_GetJSON;
    Model_URIs URLs = new Model_URIs();
    JSONParser_Post jsnp_post = new JSONParser_Post();
    Button sendotp;
    EditText phone;
    ConnectionDetector cd;
    Boolean isInternetPresent;
    String mobile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_signup);
        sendotp = (Button) findViewById(R.id.requestOtp);
        phone = (EditText) findViewById(R.id.mobileNo);

        cd = new ConnectionDetector(OtpSignup.this);
    }
    public void SENDOTP(View v){
        authenticate();
    }

    private void authenticate() {
        int randomPin   =(int)(Math.random()*900000)+100000;
        String otp  = String.valueOf(randomPin);
        mobile = phone.getText().toString();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent == true) {

            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("msg", "Your Verification Passcode is "+otp);
                jsonObj.put("mobile", mobile);      ;
                jsonObj.put("Respose", "TRUE");

                TransactionLog_GetJSON = jsonObj.toString();
                new Perform_log(URLs.getPerform_URI(), TransactionLog_GetJSON).execute();

            }catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
    }
//======================================== Perform_log Task ==================================

    public class Perform_log extends AsyncTask<Void, Integer, Void> {
        ProgressDialog progressDialog;
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
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            progressDialog.dismiss();
            try {
                if (final_out1.equalsIgnoreCase("")) {
                    Toast.makeText(OtpSignup.this, "Blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    //attribute success then save else error message
                    Toast.makeText(getApplicationContext(), final_out1, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getApplicationContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog = new ProgressDialog(OtpSignup.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            //super.onPreExecute();
        }
    }
}
