package com.mtech.resident;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mtech.resident.network.ConnectionDetector;
import com.mtech.resident.sms.JSONParser_Post;
import com.mtech.resident.sms.Model_URIs;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VisitorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VisitorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView list;
    Button btn;
    byte[] vbyteimage = null, idbyteimage = null;
    ImageView vPhoto, idphoto;
    ArrayList<String> rpoints;
    ArrayList<String> mob;
    ArrayList<String> date;
    ArrayList<String> purpose;
    ArrayList<String> from;
    ArrayList<String> srno;
    ArrayList<String> vehicle;
    ArrayList<String> vaddress;
    ArrayList<Bitmap> photo;
    ArrayList<String> otp;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    MyListAdapter adapter = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String flatNo = null, wing = null, SrNo = null, societyname = null,sendMob = null,pin=null;
    ImageView imageview;

    String TransactionLog_GetJSON;
    Model_URIs URLs = new Model_URIs();
    JSONParser_Post jsnp_post = new JSONParser_Post();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public VisitorsFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitorsFragment newInstance(String param1, String param2) {
        VisitorsFragment fragment = new VisitorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        flatNo = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        societyname = sp.getString("societyname", null);
        cd = new ConnectionDetector(getActivity());
        if (getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitors, container, false);

        rpoints = new ArrayList<>();
        mob = new ArrayList<>();
        date = new ArrayList<>();
        purpose = new ArrayList<>();
        from = new ArrayList<>();
        srno = new ArrayList<>();
        vehicle = new ArrayList<>();
        vaddress = new ArrayList<>();
        photo = new ArrayList<>();
        otp = new ArrayList<>();

//        rpoints = (ArrayList<String>) getIntent().getSerializableExtra("points");
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        list = (ListView) view.findViewById(R.id.list);

//        adapter=new MyListAdapter(this, maintitle);

//        visible = !visible;
//        text.setVisibility(visible ? View.VISIBLE : View.GONE);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent == true){

            new GetLongLat("E").execute();

        }else{
            Toast.makeText(getActivity(), "Internet Connection error", Toast.LENGTH_SHORT).show();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
//                Bitmap listpoint = photo.get(position);
//                Toast.makeText(getContext(), ""+listpoint, Toast.LENGTH_SHORT).show();
                final ImageView i = (ImageView) view.findViewById(R.id.markVisitor);

                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "STill TO DO   "+position, Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                        builder.setCancelable(false)
//                                .setNegativeButton("Mark AS Incorrect", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                    SrNo= srno.get(position);
//                                       new GetLongLat("D").execute();
//                                    }
//                                });
//                        AlertDialog dialog = builder.create();
//                        dialog.show();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setMessage("MARK AS ");
                        alertDialogBuilder.setNegativeButton("CORRECT",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        SrNo = srno.get(position);
                                        sendMob = mob.get(position);
                                        Random r = new Random();
                                        pin = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
                                        new GetLongLat("C").execute();
                                    }
                                });

                        alertDialogBuilder.setPositiveButton("INCORRECT", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SrNo = srno.get(position);
                                new GetLongLat("D").execute();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
                    Intent intent = new Intent(getContext(), VisitorDetails.class);
                    intent.putExtra("name", rpoints.get(position));
                    intent.putExtra("mobile", mob.get(position));
                    intent.putExtra("purpose", purpose.get(position));
                    intent.putExtra("enteredAt", date.get(position));
                    intent.putExtra("vehicleNo", vehicle.get(position));
                    intent.putExtra("from", vaddress.get(position));
                    intent.putExtra("visitorphoto", photo.get(position));
                    startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {
            if (flag.equalsIgnoreCase("E")) {
                try {

                    while (rs.next()) {
                        String a = rs.getString("VisitorName");
                        rpoints.add(a);
                        String b = rs.getString("MobileNo");
                        mob.add(b);
                        String oo = rs.getString("OTP");
                        otp.add(oo);
//                        String c = String.valueOf(rs.getDate("EnteredAt"));
                        String datePattern = "dd MMM yyyy";
                        String timePattern = "HH:mm a";
                        Date today;
                        String dateOutput;
                        SimpleDateFormat simpleDateFormat, sp;
                        simpleDateFormat = new SimpleDateFormat(datePattern);
                        Time t;
                        String timeOutput;
                        sp = new SimpleDateFormat(timePattern);
                        t = rs.getTime("EnteredAt");
                        timeOutput = sp.format(t);
                        today = rs.getDate("EnteredAt");
                        dateOutput = simpleDateFormat.format(today);
//                        System.out.println(datePattern + " - " + dateOutput);
                        date.add(dateOutput + " at " + timeOutput);
                        String d = rs.getString("Purpose");
                        purpose.add(d);
                        String srNo = rs.getString("SrNo");
                        srno.add(srNo);
                        String e = rs.getString("VehicleNumber");
                        vehicle.add(e);
                        String f = rs.getString("VAddress");
                        vaddress.add(f);
                        if (rs.getBytes("VisitorPhoto") != null) {
                            vbyteimage = rs.getBytes("VisitorPhoto");
                            Bitmap bmp = BitmapFactory.decodeByteArray(vbyteimage, 0, vbyteimage.length);
                            photo.add(bmp);
                        } else {
                            photo.add(null);
                        }
                    }
                    adapter = new MyListAdapter(getActivity(), rpoints, mob, date, purpose, photo, srno, vaddress,otp);
                    list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

                } catch (Exception e) {

                    e.toString();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
            if (flag.equalsIgnoreCase("D")) {
                try {
                    Toast.makeText(getContext(), "Marked Incorrect !", Toast.LENGTH_SHORT).show();

                    Fragment fragment = new VisitorsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_selection, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
               else if (flag.equalsIgnoreCase("C")) {
                    try {
                        Toast.makeText(getContext(), "Marked Correct !", Toast.LENGTH_SHORT).show();


                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent == true){
                            JSONObject jsonObj = new JSONObject();

                            try {
                                jsonObj.put("msg", pin+" is your Owner verification code. ");
                                jsonObj.put("mobile", sendMob);
                                jsonObj.put("Respose", "TRUE");

                                TransactionLog_GetJSON = jsonObj.toString();

                                new Perform_log(URLs.getPerform_URI(), TransactionLog_GetJSON).execute();
                                //  new Perform_log("", TransactionLog_GetJSON).execute();

                            }
                            catch (final Exception e)
                            {
                                e.printStackTrace();
                            }
                        }


                        Fragment fragment = new VisitorsFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_selection, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.icon:
                //Do it when myimage is clicked
                Toast.makeText(getContext(), "CLICKED!", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ((SelectionActivity) getActivity())
                .setActionBarTitle("Visitors");

    }

    @Override
    public void onPause() {
        super.onPause();
    }
//-------------------------------------------------------------------------------------------

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

                    if (flag.equalsIgnoreCase("E")) {
                        String query = "SELECT SrNo,VisitorName,MobileNo,EnteredAt,Purpose,VisitorPhoto,VehicleNumber,VAddress,OTP FROM VisitorDetails WHERE OwnerDetails='" + wing + flatNo + "' AND ( Active='0' OR Active='4') AND " +
                                "SocietyName= '" + societyname + "'  ORDER BY SrNo DESC";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    } else if (flag.equalsIgnoreCase("D")) {
                        String sql = "UPDATE dbo.VisitorDetails SET Active = ?, IncorrectService=? WHERE SrNo = ? ";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "2");
                        preparedStatement.setString(2, "Y");
                        preparedStatement.setString(3, SrNo);

                        int i = preparedStatement.executeUpdate();
                        ret = "" + i;
                    }
                    else if (flag.equalsIgnoreCase("C")) {
                        String sql = "UPDATE dbo.VisitorDetails SET Active = ?,OTP=? WHERE SrNo = ? ";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "4");
                        preparedStatement.setString(2, pin);
                        preparedStatement.setString(3, SrNo);

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

    //======================================== Perform_log Task ==================================
    public class Perform_log extends AsyncTask<Void, Integer, Void> {
        //        ProgressDialog progressDialog;
        String final_out1 = "";
        String URL = "";
        String jsonString = "";

        public Perform_log(String url, String json){
            this.URL = url;
            this.jsonString = json;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                final_out1 = jsnp_post.makeHttpRequest(URL, "POST", jsonString);

            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){

//            progressDialog.dismiss();
            try {
                if (final_out1.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "OTP not Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    //attribute success then save else error message
                    Toast.makeText( getContext(), ""+final_out1, Toast.LENGTH_SHORT).show();
//                    new  GetLongLat("C").execute();
//                    verifyotpbutton.setVisibility(View.VISIBLE);
//                    verifyotp.setVisibility(View.VISIBLE);
//                    phone.setEnabled(false);
//                    mCbShowPwd.setVisibility(View.VISIBLE);
                }
//                    Toast.makeText(getApplicationContext(), final_out1, Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute(){
            // TODO Auto-generated method stub
//            progressDialog = new ProgressDialog( SavedScreen.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.show();
            //super.onPreExecute();
        }
    }

//================   4-Digit Pin Code  =============================================================



}
