package com.mtech.resident;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    ListView list;
    byte[] vbyteimage = null;
    ImageView vPhoto;
    ArrayList<String> rpoints;
    ArrayList<String> mob;
    ArrayList<String> date;
    ArrayList<String> purpose;
    ArrayList<String> from;
    ArrayList<String> srno;
    ArrayList<Bitmap> photo;
    ArrayList<String> otp;

    MyListAdapter adapter=null;
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
    };

//    String[] subtitle ={
//            "Sub Title 1","Sub Title 2",
//            "Sub Title 3","Sub Title 4",
//            "Sub Title 5",
//    };
//
//    Integer[] imgid={
//            R.drawable.test,R.drawable.test,
//            R.drawable.test,R.drawable.test,
//            R.drawable.test,
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

//        vPhoto = (ImageView) findViewById(R.id.visitorPhoto);
        Intent intent = getIntent();
        rpoints = new ArrayList<>();
        mob = new ArrayList<>();
        date = new ArrayList<>();
        purpose = new ArrayList<>();
        from = new ArrayList<>();
        srno = new ArrayList<>();
        photo = new ArrayList<>();
        otp = new ArrayList<>();
//        rpoints = (ArrayList<String>) getIntent().getSerializableExtra("points");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        list=(ListView)findViewById(R.id.list);
//        adapter=new MyListAdapter(this, maintitle);

        new GetLongLat("E").execute();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
                }

                else if(position == 1) {
                    //code specific to 2nd list item
                    Toast.makeText(getApplicationContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
                }

                else if(position == 2) {

                    Toast.makeText(getApplicationContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 3) {

                    Toast.makeText(getApplicationContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 4) {

                    Toast.makeText(getApplicationContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs=null, rs1=null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(ListViewActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res){
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){

                showName(rs,rs1, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params){
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null){
                    ret = "Internet connection Error";
                } else {

                    if (flag.equalsIgnoreCase("E")){
                        String query = "SELECT top 10 * FROM VisitorDetails";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                }
            } catch (Exception ex){
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs,ResultSet rs1, String flag){
        try {
            if (flag.equalsIgnoreCase("E")) {
                try {

                    while (rs.next()) {
                    String a = rs.getString("VisitorFirstName");
                        rpoints.add(a);
                        String b = rs.getString("MobileNo");
                        mob.add(b);
                        String c = String.valueOf(rs.getDate("EnteredAt"));
                        date.add(c);
                        String d = rs.getString("Purpose");
                        purpose.add(d);
//                        String e = rs.getString("MobileNo");
//                        mob.add(e);
                        if (rs.getBytes("VisitorPhoto") != null) {
                            vbyteimage = rs.getBytes("VisitorPhoto");
                            Bitmap bmp = BitmapFactory.decodeByteArray(vbyteimage, 0, vbyteimage.length);
                            photo.add(bmp);
                        }
                    }

                    adapter=new MyListAdapter(this, rpoints,mob,date,purpose,photo,srno,srno,otp);
                    list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

                } catch (Exception e){
                    e.toString();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}