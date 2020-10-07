package com.mtech.resident;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AddVehicleAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;

    Button btn;
    String SrNo = null, vNo = null;
//    private final Integer[] imgid;

    public AddVehicleAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle) {
        super(context, R.layout.vehiclelist, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
//        this.imgid=imgid;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.vehiclelist, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.vehNo);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.vehType);

        titleText.setText("" + maintitle.get(position));
        if (subtitle.get(position).equalsIgnoreCase("2")) {
            subtitleText.setBackgroundResource(R.drawable.bike);
        } else {
            subtitleText.setBackgroundResource(R.drawable.car);
        }
//        subtitleText.setText(""+subtitle.get(position));

        ImageView a = (ImageView) rowView.findViewById(R.id.delete);
//        btn = (Button) rowView.findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "YES/NO", Toast.LENGTH_SHORT).show();
//            }
//        });


//        a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////            Toast.makeText(getContext(), "STill TO DO   "+position, Toast.LENGTH_SHORT).show();
//                vNo= maintitle.get(position);
//                new GetLongLat("A").execute();
//            }
//        });


        return rowView;

    }


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
                    if (flag.equalsIgnoreCase("A")) {
                        String sql = "DELETE FROM VehicleList WHERE VehicleNumber='" + vNo + "'";

                        Statement stmt = conn.createStatement();

                        int i = stmt.executeUpdate(sql);
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

            if (flag.equalsIgnoreCase("A")) {
                try {
                    Toast.makeText(getContext(), "Deleted !", Toast.LENGTH_SHORT).show();


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