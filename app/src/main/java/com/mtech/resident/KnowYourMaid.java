package com.mtech.resident;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KnowYourMaid.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KnowYourMaid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KnowYourMaid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView list;
    Button btn;
    byte[] vbyteimage = null;
    ArrayList<String> rpoints;
    ArrayList<String> mob;
    ArrayList<String> date;
    ArrayList<String> purpose;
    ArrayList<String> from;
    ArrayList<String> srno;
    ArrayList<String> od;
    ArrayList<String> availability;
    ArrayList<String> avail;
    ArrayList<Bitmap> photo;
    String[] kym_types = {"All", "Cook", "House Maid"};
    ArrayAdapter kym_adapter;
    HMListAdapter adapter = null;
    Spinner kym;
    String flatNo = null, wing = null, SrNo = null, societyname=null;
    String f = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private OnFragmentInteractionListener mListener;

    public KnowYourMaid() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KnowYourMaid.
     */
    // TODO: Rename and change types and number of parameters
    public static KnowYourMaid newInstance(String param1, String param2) {
        KnowYourMaid fragment = new KnowYourMaid();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_know_your_maid, container, false);
        kym = (Spinner) view.findViewById(R.id.kym);
        rpoints = new ArrayList<>();
        mob = new ArrayList<>();
        date = new ArrayList<>();
        purpose = new ArrayList<>();
        from = new ArrayList<>();
        srno = new ArrayList<>();
        od = new ArrayList<>();
        availability = new ArrayList<>();
        avail = new ArrayList<>();
        photo = new ArrayList<>();
//        rpoints = (ArrayList<String>) getIntent().getSerializableExtra("points");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        kym_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, kym_types);
        kym.setAdapter(kym_adapter);

        list = (ListView) view.findViewById(R.id.list);

//        adapter=new MyListAdapter(this, maintitle);

//        visible = !visible;
//        text.setVisibility(visible ? View.VISIBLE : View.GONE);
        new GetLongLat("E").execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
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
                        String query = "SELECT VisitorName,Purpose,VisitorPhoto,MobileNo,EnteredAt,OwnerDetails,LeaveAt FROM VisitorDetails where " +
                                "SocietyName='"+societyname+"' AND Purpose='House Maid'";
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

        try {
            if (flag.equalsIgnoreCase("E")) {
                try {

                    while (rs.next()) {
                        String a = rs.getString("VisitorName");
                        rpoints.add(a);
                        String b = rs.getString("MobileNo");
                        mob.add(b);
//                        String c = String.valueOf(rs.getDate("EnteredAt"));
                        String timePattern1 = "HH:mm a";
                        SimpleDateFormat sp1;
                        Time t1;
                        String timeOutput1 = null;
                        sp1 = new SimpleDateFormat(timePattern1);
                        t1 = rs.getTime("EnteredAt");
                        timeOutput1 = sp1.format(t1);
                        date.add(timeOutput1);

                        String d = rs.getString("Purpose");
                        purpose.add(d);
                        String e = rs.getString("OwnerDetails");
                        od.add(e);

                        //====

                        f = String.valueOf(rs.getDate("LeaveAt"));
                        String dateOutput = null, timeOutput = null;
//
//                        System.out.println(datePattern + " - " + dateOutput);

                        if (!f.equalsIgnoreCase("null")) {
//
                            Log.e("stt", f);
                            String datePattern = "dd MMM yyyy";
                            String timePattern = "HH:mm a";
                            Date today;

                            SimpleDateFormat simpleDateFormat, sp;
                            simpleDateFormat = new SimpleDateFormat(datePattern);
                            Time t;

                            sp = new SimpleDateFormat(timePattern);
                            t = rs.getTime("LeaveAt");
                            timeOutput = sp.format(t);
                            today = rs.getDate("LeaveAt");
                            dateOutput = simpleDateFormat.format(today);
                            avail.add(dateOutput + " at " + timeOutput);
                        } else {
                            avail.add(f);
                        }
                        //====

//                        String f =String.valueOf(rs.getDate("LeaveAt"));
//                        availability.add(f);
                        if (rs.getBytes("VisitorPhoto") != null) {
                            vbyteimage = rs.getBytes("VisitorPhoto");
                            Bitmap bmp = BitmapFactory.decodeByteArray(vbyteimage, 0, vbyteimage.length);
                            photo.add(bmp);
                        } else {
                            photo.add(null);
                        }
                    }
                    //------

//                    for(int i=0;i<availability.size();i++){
//                        String a= null;
//                        a=availability.get(i);
//                        if(!a.equalsIgnoreCase(null)) {
//
//                            Log.e("stt", f);
//                            String datePattern = "dd MMM yyyy";
//                            String timePattern = "HH:mm a";
//                            Date today;
//
//                            SimpleDateFormat simpleDateFormat, sp;
//                            simpleDateFormat = new SimpleDateFormat(datePattern);
//                            Time t;
//                                String timeOutput=null,dateOutput=null;
//                            sp = new SimpleDateFormat(timePattern);
//                            t = rs.getTime("LeaveAt");
//                            timeOutput = sp.format(t);
//                            today = rs.getDate("LeaveAt");
//                            dateOutput = simpleDateFormat.format(today);
//                            avail.add(dateOutput + " at " + timeOutput);
//                        }
//                        else{
//                            avail.add(availability.get(i));
//                        }
//                    }

                    //------
                    adapter = new HMListAdapter(getActivity(), rpoints, mob, date, purpose, photo, srno, od, avail);
                    list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

                } catch (Exception e) {
                    e.toString();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
//-------------------------------------------------------------------------------------------

    @Override
    public void onResume(){
        super.onResume();

        ((SelectionActivity) getActivity())
                .setActionBarTitle("Know Your Maid");
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
