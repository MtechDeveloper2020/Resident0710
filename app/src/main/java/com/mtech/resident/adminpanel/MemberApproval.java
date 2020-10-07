package com.mtech.resident.adminpanel;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mtech.resident.DBConnection;
import com.mtech.resident.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class MemberApproval extends AppCompatActivity {
        ArrayList<String> userId = new ArrayList<>();
        ArrayList<String> flatNo = new ArrayList<>();
        ArrayList<String> buildingName = new ArrayList<>();
        MyListData[] myListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_approval);
        new GetLongLat("A").execute();

//        MyListData[] myListData = new MyListData[] {
//                new MyListData("Email", android.R.drawable.ic_dialog_email),
//                new MyListData("Info", android.R.drawable.ic_dialog_info),
//                new MyListData("Delete", android.R.drawable.ic_delete),
//                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
//                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
//                new MyListData("Map", android.R.drawable.ic_dialog_map),
//                new MyListData("Email", android.R.drawable.ic_dialog_email),
//                new MyListData("Info", android.R.drawable.ic_dialog_info),
//                new MyListData("Delete", android.R.drawable.ic_delete),
//                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
//                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
//                new MyListData("Map", android.R.drawable.ic_dialog_map),
//        };

    }

    //----------------------------------------------------------------------------------------------
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
            progressDialog = new ProgressDialog(MemberApproval.this);
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
                Toast.makeText(MemberApproval.this, res, Toast.LENGTH_SHORT).show();
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
                    if (flag.equalsIgnoreCase("A")){
                        String query = "Select UserId, FlatNo, BuildingName from OwnerLoginDetails where AdminApprove='0'";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                    else if (flag.equalsIgnoreCase("U")){

                    }
                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag){
        try {

            if (flag.equalsIgnoreCase("A")){
                try {
                    while(rs.next()){
                       String a = rs.getString("UserId");
                       String b = rs.getString("FlatNo");
                       String c = rs.getString("BuildingName");
                            userId.add(a);
                            flatNo.add(b);
                            buildingName.add(c);

                    }

                myListData = new MyListData[] {
                new MyListData("sddsd", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
        };


                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    MyListAdapter adapter = new MyListAdapter(myListData);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //----------------------------------------------------------------------------------------------



}

