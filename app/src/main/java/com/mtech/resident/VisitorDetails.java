package com.mtech.resident;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VisitorDetails extends AppCompatActivity {
    String name = null, mob = null, purpose = null, date = null, vehicle = null, from = null;
    TextView Vname, mobile, Purp, inTime, VNo, Place;
    Bitmap visphoto, idsphoto;
    ImageView vPhoto, iPhoto;
    ImageView btn;
    byte[] vbyteimage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_visitor_details);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mob = intent.getStringExtra("mobile");
        purpose = intent.getStringExtra("purpose");
        date = intent.getStringExtra("enteredAt");
        vehicle = intent.getStringExtra("vehicleNo");
        from = intent.getStringExtra("from");
        visphoto = (Bitmap) intent.getParcelableExtra("visitorphoto");
        idsphoto = (Bitmap) intent.getParcelableExtra("idphoto");

        Vname = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobileNo);
        Purp = (TextView) findViewById(R.id.purpose);
        inTime = (TextView) findViewById(R.id.inTime);
        VNo = (TextView) findViewById(R.id.vehicleNo);
        Place = (TextView) findViewById(R.id.From);
        vPhoto = (ImageView) findViewById(R.id.icon);
        iPhoto = (ImageView) findViewById(R.id.PhotoId);
        btn = (ImageView) findViewById(R.id.call);

        Vname.setText(name);
        mobile.setText(mob);
        Purp.setText(purpose);
        inTime.setText("In Time : " + date);
        if(vehicle == null){
            vehicle = "-";
        }

        VNo.setText("Vehicle No. : " + vehicle);
        Place.setText("From : " + from);

        if (visphoto != null) {
            vPhoto.setImageBitmap(visphoto);

//            Bitmap bitmap = ((BitmapDrawable) vPhoto.getDrawable()).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            vbyteimage = stream.toByteArray();
        }

        vPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                SendGetStartedNotification(ParseUser user);
            }
        });
        iPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                SendGetStartedNotification(ParseUser user);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + mob);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        //=======================================================
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
