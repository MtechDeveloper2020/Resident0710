package com.mtech.resident.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mtech.resident.LoginActivity;
import com.mtech.resident.R;

public class VerifyOtpActivity extends AppCompatActivity {

    TextView mobileNo;
    EditText otp;
    Button verifyOtp;
    String pin =null, mob= null, enteredPin = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        Intent i = getIntent();
        pin = i.getStringExtra("pin");
        mob = i.getStringExtra("mobile");

        mobileNo = findViewById(R.id.mobileNo);
        otp = findViewById(R.id.otp);
        verifyOtp = findViewById(R.id.verifyOtp);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();

        mobileNo.setText(""+mob);
        verifyOtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                enteredPin = otp.getText().toString();
                if(!enteredPin.isEmpty()){
                    if(enteredPin.equalsIgnoreCase(""+pin)){
                        Toast.makeText(VerifyOtpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(VerifyOtpActivity.this, LoginActivity.class);
                        editor.putBoolean("otp", true);
                        editor.commit();
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(VerifyOtpActivity.this, "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VerifyOtpActivity.this, "Please enter OTP to verify!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}