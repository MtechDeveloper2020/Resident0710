package com.mtech.resident;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HMListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> date;
    private final ArrayList<String> purpose;
    private final ArrayList<String> srno;
    private final ArrayList<String> od;
    private final ArrayList<String> available;
    private final ArrayList<Bitmap> photo;
    ImageView btn;
//    private final Integer[] imgid;

     public HMListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle,
                         ArrayList<String> date, ArrayList<String> purpose, ArrayList<Bitmap> photo, ArrayList<String> srno, ArrayList<String> od, ArrayList<String> available) {
        super(context, R.layout.hmlist, maintitle);

        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.date = date;
        this.purpose = purpose;
        this.srno = srno;
        this.od = od;
        this.available = available;
        this.photo = photo;
//        this.imgid=imgid;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.hmlist, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView enteredAt = (TextView) rowView.findViewById(R.id.date);
        TextView mobile = (TextView) rowView.findViewById(R.id.mob);
        TextView odetails = (TextView) rowView.findViewById(R.id.flatNo);
        TextView availability = (TextView) rowView.findViewById(R.id.availabilty);
        ImageView call = (ImageView) rowView.findViewById(R.id.call);

        titleText.setText("" + maintitle.get(position));
        if (photo.get(position) != null) {
            imageView.setImageBitmap(photo.get(position));

        } else {
            imageView.setBackgroundResource(R.drawable.profile);
        }
        subtitleText.setText("" + purpose.get(position));
        mobile.setText("" + subtitle.get(position));
        odetails.setText("Flat No.  " + od.get(position));

        if (available.get(position).equalsIgnoreCase("null")) {
            availability.setText("Available in Society");
            enteredAt.setText("From : " + date.get(position));
        }
        else
            {
//            availability.setText(available.get(position));
            availability.setText("Last visit in society");
            enteredAt.setText("" + available.get(position));
        }
//        purposee.setText(""+purpose.get(position));
//        srnoo.setText(""+srno.get(position));

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + subtitle.get(position));
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                getContext().startActivity(callIntent);

            }
        });
        return rowView;
    }
    ;
}