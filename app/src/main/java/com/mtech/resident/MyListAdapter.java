package com.mtech.resident;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getColor;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> date;
    private final ArrayList<String> purpose;
    private final ArrayList<String> srno;
    private final ArrayList<String> address;
    private final ArrayList<Bitmap> photo;
    private final ArrayList<String> otpp;
    LinearLayout ll;
    Button btn;
    String SrNo = null;
//    private final Integer[] imgid;

    public MyListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle,
                         ArrayList<String> date, ArrayList<String> purpose, ArrayList<Bitmap> photo, ArrayList<String> srno, ArrayList<String> address, ArrayList<String> otp) {
        super(context, R.layout.mylist, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.date = date;
        this.purpose = purpose;
        this.srno = srno;
        this.address = address;
        this.photo = photo;
        this.otpp = otp;
//        this.imgid=imgid;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView enteredAt = (TextView) rowView.findViewById(R.id.date);
        TextView purposee = (TextView) rowView.findViewById(R.id.purpose);
        TextView srnoo = (TextView) rowView.findViewById(R.id.srno);
        TextView markBtn = (TextView) rowView.findViewById(R.id.button);
        TextView place = (TextView) rowView.findViewById(R.id.from);
        ImageView i = (ImageView) rowView.findViewById(R.id.markVisitor);
        TextView otp = (TextView) rowView.findViewById(R.id.otp);
        LinearLayout fadell = (LinearLayout) rowView.findViewById(R.id.fadelayout);

        titleText.setText("" + maintitle.get(position));
        if (photo.get(position) != null) {
            imageView.setImageBitmap(photo.get(position));
        } else {
            imageView.setBackgroundResource(R.drawable.profile);
        }

        subtitleText.setText("" + subtitle.get(position));
        enteredAt.setText("" + date.get(position));
        purposee.setText("" + purpose.get(position));
        place.setText("" + address.get(position));
        srnoo.setText("" + srno.get(position));
        otp.setText("" + otpp.get(position));

         String oyp="tanuja";
         oyp = otpp.get(position);

        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.reverse);

        markBtn.startAnimation(animShake);


        if(!TextUtils.isEmpty(oyp) && oyp!=null) {
            markBtn.setEnabled(false);
            fadell.setBackgroundColor(getColor(getContext(), R.color.timestamp));
            fadell.setEnabled(false);
            imageView.setEnabled(false);
            markBtn.clearAnimation();
            markBtn.setVisibility(View.GONE);
            i.setVisibility(View.GONE);
        }else{
            otp.setText("");

        }

//        btn = (Button) rowView.findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "YES/NO", Toast.LENGTH_SHORT).show();
//            }
//        });


//        i.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                        Toast.makeText(getContext(), "STill TO DO   "+position, Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setCancelable(false)
//                        .setNegativeButton("Mark AS Incorrect", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                SrNo= srno.get(position);
//                                Toast.makeText(getContext(), "hjh"+SrNo, Toast.LENGTH_SHORT).show();
//                                new GetLongLat("D").execute();
//                            }
//                        });
//                AlertDialog dialog = builder.create();
//                dialog.show();

//            goToFragment();

//            }
//        });


        return rowView;

    }

    private void goToFragment() {

    }

    ;

    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs = null, rs1 = null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag) {
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
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
                Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null) {
                    ret = "Internet connection Error";
                } else {
                    if (flag.equalsIgnoreCase("D")) {
                        String sql = "UPDATE dbo.VisitorDetails SET Active = ? WHERE SrNo = ? ";

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "2");
                        preparedStatement.setString(2, SrNo);
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

            if (flag.equalsIgnoreCase("D")) {
                try {
                    Toast.makeText(getContext(), "Marked Incorrect !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), VisitorsFragment.class);
                    getContext().startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}