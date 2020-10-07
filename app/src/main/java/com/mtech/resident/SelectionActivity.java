package com.mtech.resident;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mtech.resident.community.Community;
import com.mtech.resident.community.Community_fragment;
import com.mtech.resident.network.ConnectionDetector;
import com.mtech.resident.quickactions.QuickActionsMain;
import com.mtech.resident.share.Share;
import com.mtech.resident.signup.OtpSignup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SelectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Movie> movieList = new ArrayList<Movie>();

    ArrayList<String> rpoints;
    ArrayList<String> mob;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname = null, pass = null, buildingName = null, flatNo = null, wing = null,societyname=null;
    String sosDetails = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        cd = new ConnectionDetector(this);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();

        //------------------ Key Generation Call ----------------------------

//         if (!sp.getBoolean("otp", false)) {
//            Intent intent = new Intent(getApplicationContext(), OtpSignup.class);
//            startActivity(intent);
//            finish();
//        }
        if(!sp.getBoolean("sf", false)) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            startService(new Intent(this, VehicleMessageService.class));
        }
        //------------------------------------------------------------------

        uname = sp.getString("uname", null);
        pass = sp.getString("pass", null);
        buildingName = sp.getString("buildingName", null);
        flatNo = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        societyname = sp.getString("societyname", null);

        rpoints = new ArrayList<>();
        rpoints = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new QuickActionsMain();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_selection, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        TextView flatno = (TextView) hView.findViewById(R.id.FlatNo);
        TextView wingg = (TextView) hView.findViewById(R.id.wing);
        TextView buildingNamee = (TextView) hView.findViewById(R.id.buildingName);
        flatno.setText("Flat No. " + wing + flatNo);
        wingg.setText("Wing : " + wing + " WING");
        buildingNamee.setText(buildingName);
        Fragment f1 = new VisitorsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
//        Fragment f1 = new VisitorsFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        Fragment newFragment;
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (id == R.id.visitors){
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
                Fragment f1 = new VisitorsFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
            }else{
                Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
            }

            // Handle the camerac action
        } else if (id == R.id.serviceproviders){
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
                Fragment f1 = new ServiceProvider();
                getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
            }else{
                Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.searchVehicles) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
//            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment f1 = new SearchVehicles();
                getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
            }else{
                Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.know_your_maid) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()){

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            Fragment f1 = new KnowYourMaid();
            getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
        }else{
            Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
        }
        } else if (id == R.id.setting) {
            isInternetPresent = cd.isConnectingToInternet();
            if(isInternetPresent == true) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            Fragment f1 = new Setting();
            getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
    }}
        else if (id == R.id.share) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
                Fragment f1 = new Share();
                getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
            }}
            else if (id == R.id.community) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent == true) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }
                    Fragment f1 = new Community_fragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
                } else {
                    Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
                }

        } else if (id == R.id.logout) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent == true) {
            Fragment f1 = new Logout();
            getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
        }else{
        Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
        }
        } else if (id == R.id.predefineVisitors)

    {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent == true) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            Fragment f1 = new PreDefinedVisitors();
            getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
        } else {
            Toast.makeText(this, "Internet Connection Error !", Toast.LENGTH_SHORT).show();
        }
    }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            progressDialog = new ProgressDialog(SelectionActivity.this);
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
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
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
                    ret = "Error in connection with SQL server";
                } else {

                    if (flag.equalsIgnoreCase("E")) {
                        PreparedStatement stmt = conn.prepareStatement("insert into OwnerSOS" + "(OwnerDetails,BuildingName,SocietyName,Active)" + " values(?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, sosDetails);
                        stmt.setString(2, buildingName);
                        stmt.setString(3, societyname);
                        stmt.setString(4, "0");
                        int i = stmt.executeUpdate();
                        ret = "" + i;
                    }
                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag){

        if (flag.equalsIgnoreCase("E")){
            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("You will get help soon");
                dlgAlert.setTitle("Please Wait");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                //dismiss the dialog
                            }
                        });

                dlgAlert.create().show();
            } catch (Exception e) {
                e.toString();
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sos(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Need Help");
        alertDialogBuilder.setMessage("Raise an alarm with security?");
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sosDetails = wing + flatNo;
                new GetLongLat("E").execute();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(
                VehicleMessageService.BROADCAST_ACTION));

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        //stopService(intent);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}


