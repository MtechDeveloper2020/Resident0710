package com.mtech.resident;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SPListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> purpose;
    private final ArrayList<String> srno;
    private final ArrayList<Bitmap> photo;
    Button btn;
//    private final Integer[] imgid;

    public SPListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle,
                         ArrayList<String> purpose, ArrayList<Bitmap> photo, ArrayList<String> srno){
        super(context, R.layout.splist, maintitle);

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.purpose = purpose;
        this.srno = srno;
        this.photo = photo;
//      this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.splist, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView enteredAt = (TextView) rowView.findViewById(R.id.date);

//        TextView purposee = (TextView) rowView.findViewById(R.id.purpose);
//        TextView srnoo = (TextView) rowView.findViewById(R.id.srno);
//        TextView markBtn = (TextView) rowView.findViewById(R.id.button);

        titleText.setText("" + maintitle.get(position));
        if(photo.get(position) != null){
            imageView.setImageBitmap(photo.get(position));
        }else{
            imageView.setBackgroundResource(R.drawable.profile);
        }

        subtitleText.setText("" + purpose.get(position));
//        enteredAt.setText(""+date.get(position));
//        purposee.setText(""+purpose.get(position));
//        srnoo.setText(""+srno.get(position));

        return rowView;
    }
}