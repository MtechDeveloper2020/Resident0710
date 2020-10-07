package com.mtech.resident.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.resident.R;
import com.mtech.resident.VehicleMessageService;


public class Notify extends AppCompatActivity {
    CheckBox nCheck,sCheck,vCheck;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String nAll=null,sound=null,vib=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //========================================================================
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        nAll= sp.getString("notify",null);
        sound= sp.getString("sound_notify",null);
        vib= sp.getString("vibrate_notify",null);
        //========================================================================

        nCheck= (CheckBox) findViewById(R.id.nCheck);
        sCheck= (CheckBox) findViewById(R.id.sCheck);
        vCheck= (CheckBox) findViewById(R.id.vCheck);
        check();
    }

    private void check() {
        if(nAll.equalsIgnoreCase("1")){
           nCheck.setChecked(true);
//           sCheck.setChecked(true);
//           vCheck.setChecked(true);
        }
        else{
            nCheck.setChecked(false);
        }
        if(sound.equalsIgnoreCase("1")){
            sCheck.setChecked(true);
        }
        else{
            sCheck.setChecked(false);
        }
        if(vib.equalsIgnoreCase("1")){
            vCheck.setChecked(true);
        }else{
            vCheck.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(!nCheck.isChecked()){
//            editor.putString("notify", "0");
//            editor.putString("sound_notify", "0");
//            editor.putString("vibrate_notify", "0");
//            editor.apply();
//        }
//        if(!sCheck.isChecked()){
//
//            editor.putString("sound_notify", "0");
//            editor.apply();
//        }
//        if(!vCheck.isChecked()){
//            editor.putString("vibrate_notify", "0");
//            editor.apply();
//        }



        finish();

    }

     public void saveChanges(View view){

         if(nCheck.isChecked() == false){
             editor.remove("notify");
             editor.remove("sound_notify");
             editor.remove("vibrate_notify");

            editor.putString("notify", "0");
            editor.putString("sound_notify", "0");
            editor.putString("vibrate_notify", "0");
             editor.apply();
        }else{
             editor.remove("notify");
             editor.remove("sound_notify");
             editor.remove("vibrate_notify");
                 editor.putString("notify", "1");
                 editor.putString("sound_notify", "1");
                 editor.putString("vibrate_notify", "1");
                 editor.apply();

         }
        if(sCheck.isChecked() == false){

            editor.remove("sound_notify");
            editor.putString("sound_notify", "0");
            editor.apply();
        }else{
            editor.remove("sound_notify");
            editor.putString("sound_notify", "1");
            editor.apply();
        }
        if(vCheck.isChecked() == false){
            editor.remove("vibrate_notify");
            editor.putString("vibrate_notify", "0");
            editor.apply();
        }else{
            editor.remove("vibrate_notify");
            editor.putString("vibrate_notify", "1");
            editor.apply();
        }
         Toast.makeText(this, "Changes saved successfully !", Toast.LENGTH_SHORT).show();

         stopService(new Intent(Notify.this, VehicleMessageService.class));
         startService(new Intent(Notify.this, VehicleMessageService.class));
//         Intent i = new Intent(this, Setting.class);
//         startActivity(i);
//         finish();
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

