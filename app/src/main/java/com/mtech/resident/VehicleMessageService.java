package com.mtech.resident;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class VehicleMessageService extends Service {
    MediaPlayer myPlayer;
    Handler refreshHandler;
    Runnable runnable;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String flat = null, wing = null, notify = null, sound = null, vibrate = null;
    public static final String BROADCAST_ACTION = "mtech.com.ownerapp";
    private Intent intent;
    Boolean isInternetPresent;
    Handler admin_handler;
    int notificationID = 1;
    String msgFrom = null, msgVeh = null, message = null;

    public VehicleMessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        intent = new Intent(BROADCAST_ACTION);

        //========================================================================
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        notify = sp.getString("notify", null);
        sound = sp.getString("sound_notify", null);
        vibrate = sp.getString("vibrate_notify", null);
        //========================================================================


//        refreshHandler = new Handler();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "service running");
//                new GetLongLat("E").execute();
//                refreshHandler.postDelayed(this, 10 * 1000);
//            }
//        };
//        refreshHandler.postDelayed(runnable, 10 * 1000);


//        Toast.makeText(this, "Started", Toast.LENGTH_LONG).show();


    }

    Runnable admin_notification_runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

			/*
             * notification_count = getSharedPreferences("notification_count",
			 * Context.MODE_PRIVATE); Editor editor = notification_count.edit();
			 */

            try {


                new Thread(new Runnable() {
                    public void run() {
                        new GetLongLat("E").execute();
//                            admin_user = Admin_User.getString("user", "0");
//                            if (admin_user.equalsIgnoreCase("0")) {
//                                Log.e("admin", "logout");
//                            } else {
//                                new admin_notify().execute();
//                                Log.e("admin", "login");
//                            }

                    }
                }).start();


            } catch (Exception e) {

            }
            admin_handler.postDelayed(admin_notification_runnable, 50000);
        }
    };

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
//            progressDialog = new ProgressDialog(BackgroundService.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
//            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {

                showName(rs, rs1, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(VehicleMessageService.this, res, Toast.LENGTH_SHORT).show();
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

                    if (flag.equalsIgnoreCase("E")) {
                        String query = "SELECT TOP 1 * from MessageList where MessageTo = '" + wing + flat + "' AND SR='0'";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        boolean status = false;
        String SrNo = null, OwnerDetails = null;

        try {
            if (flag.equalsIgnoreCase("E")) {

                while (rs.next()) {
                    msgFrom = rs.getString("MessageFrom");
                    msgVeh = rs.getString("VehicleNumber");
                    message = rs.getString("Message");
                    notify1();
                }

//                        Toast.makeText(this, "n "+notify, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "s "+sound, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "v "+vibrate, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void notify1() {


        if (notify.equalsIgnoreCase("1")) {
            Intent intent = new Intent(this, NotificationReceiverActivity.class);
            intent.putExtra("msgFrom", msgFrom);
            intent.putExtra("msgVeh", msgVeh);
            intent.putExtra("message", message);
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

            // Build notification
            // Actions are just fake
            Notification noti = new Notification.Builder(this)
                    .setContentTitle("Vehicle Alert")
                    .setContentText("Message From: " + msgFrom + "\n" +
                            "VehNo: " + msgVeh).setSmallIcon(R.drawable.car)
                    .setContentIntent(pIntent)

                    .addAction(R.drawable.bike, "Message : " + message, pIntent).build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;
//            Toast.makeText(this, "s"+sound, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "v"+vibrate, Toast.LENGTH_SHORT).show();
            if (sound.equalsIgnoreCase("1")) {
                noti.defaults |= Notification.DEFAULT_SOUND;
            }
            if (vibrate.equalsIgnoreCase("1")) {
                noti.defaults |= Notification.DEFAULT_VIBRATE;
            }

            notificationManager.notify(0, noti);
        }
    }

    @Override
    public void onStart(Intent i, int startId) {


        admin_handler = new Handler();
        admin_handler.postDelayed(admin_notification_runnable, 10);

        Log.e("log", "Inside onStart of Service");

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
        stopSelf();
    }
}


