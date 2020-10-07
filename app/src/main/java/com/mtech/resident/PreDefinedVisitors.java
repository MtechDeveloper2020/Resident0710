package com.mtech.resident;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PreDefinedVisitors extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView vPhoto;
    private OnFragmentInteractionListener mListener;
    EditText phone, fname, lname, totalVisitors, address, purpose, date;
    String mob = null, f_name = null, flat = null, wing = null, l_name = null, totVisitors = null, place = null, purp = null, CreationDate = null, vn = null;
    String visitingDate = null, Vname=null;
    Spinner VPurpose;
    ArrayAdapter<String> purpose_adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String m, d, mf, df, f_date, f_time, t_time;
    Button save, check, clear;
    java.sql.Date sqlDate;
    byte[] bitmapdata = null;
    byte[] vbyteimage = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String buildingName = null, flatNo = null,societyname=null;


    public PreDefinedVisitors() {
        // Required empty public constructor
    }

    public static PreDefinedVisitors newInstance(String param1, String param2) {
        PreDefinedVisitors fragment = new PreDefinedVisitors();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_defined_visitors, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        //========================================================================

        flat = sp.getString("FlatNo", null);
        wing = sp.getString("wing", null);
        buildingName = sp.getString("buildingName", null);
        flatNo = sp.getString("FlatNo", null);
        societyname = sp.getString("societyname", null);



        //========================================================================

        phone = (EditText) view.findViewById(R.id.phone);
        fname = (EditText) view.findViewById(R.id.fname);
        lname = (EditText) view.findViewById(R.id.lname);
        totalVisitors = (EditText) view.findViewById(R.id.totalVisitors);
        address = (EditText) view.findViewById(R.id.from);
        vPhoto = (ImageView) view.findViewById(R.id.visitorPhoto);

        date = (EditText) view.findViewById(R.id.dov);
        VPurpose = (Spinner) view.findViewById(R.id.purpose);
        save = (Button) view.findViewById(R.id.save);
        check = (Button) view.findViewById(R.id.check);
        clear = (Button) view.findViewById(R.id.clear);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        new GetLongLat("E").execute();
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                SimpleDateFormat dd = new SimpleDateFormat("dd");
                                SimpleDateFormat dm = new SimpleDateFormat("MM");
                                SimpleDateFormat dy = new SimpleDateFormat("yyyy");

                                int day = Integer.parseInt(dd.format(c
                                        .getTime()));
                                int month = Integer.parseInt(dm.format(c
                                        .getTime()));
                                int yearr = Integer.parseInt(dy.format(c
                                        .getTime()));

                                if (dayOfMonth < 10) {
                                    df = "0" + dayOfMonth;
                                } else {
                                    df = String.valueOf(dayOfMonth);
                                }
                                if ((monthOfYear + 1 < 10)) {
                                    mf = "0" + (monthOfYear + 1);
                                } else {
                                    mf = String.valueOf(monthOfYear + 1);
                                }
                                String selected_date = df + "-" + mf + "-" + year;
                                Log.e("selected date", "" + selected_date);

                                String current_date = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(yearr);
                                Log.e("selected date", "" + current_date);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                Date cur = new Date();
                                Date sel = new Date();
                                try {
                                    cur = df.parse(current_date);
                                    sel = df.parse(selected_date);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
//                                date.setText(selected_date);
                                if (sel.after(cur) || cur.equals(sel)) {
                                    date.setText(selected_date);
                                } else {
                                    Toast.makeText(getContext(),
                                            "Selected Date Is Not Valid",
                                            Toast.LENGTH_SHORT).show();
                                    date.setText("");
                                    date.setHint("Date of Visit");
                                }
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });
        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mob = phone.getText().toString();
                f_name = fname.getText().toString();
                l_name = lname.getText().toString();
                totVisitors = totalVisitors.getText().toString();
                place = address.getText().toString();
                vn = f_name + " " + l_name;
                purp = VPurpose.getSelectedItem().toString();
                visitingDate = date.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                String Date = dateFormat.format(new Date());
                String Time = dateFormat1.format(new Date());
                CreationDate = Date + " " + Time;

                Date utilDate = null;
                try {
                    utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(visitingDate);
                    sqlDate = new java.sql.Date(utilDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!mob.equalsIgnoreCase("")) {
                    if (!f_name.equalsIgnoreCase("")) {
                        if (!l_name.equalsIgnoreCase("")) {
                            if (!totVisitors.equalsIgnoreCase("")) {
                                if (sqlDate != null) {
                                    if (!purp.equalsIgnoreCase("SELECT PURPOSE")) {
                                        new GetLongLat("S").execute();
                                    } else {
                                        Toast.makeText(getContext(), "Please Select Purpose", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Please Select Visit Date", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Enter Total no. of Visitors", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Enter LastName", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Enter FirstName", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mob = phone.getText().toString();
                if (mob.length() == 10) {
                    new GetLongLat("D").execute();
                } else {
                    Toast toast = Toast.makeText(getContext(), "   Please Enter Mobile No.    ", Toast.LENGTH_LONG);
                    View eview = toast.getView();
                    eview.setBackgroundResource(R.drawable.toast_bg);
                    TextView text = (TextView) eview.findViewById(android.R.id.message);
                    toast.show();


//                    Toast.makeText(getContext(), "Please Enter Mobile No.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname.setText(null);
                lname.setText(null);
                totalVisitors.setText(null);
                address.setText(null);
                date.setText(null);
                phone.setText(null);

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
    //================================================================================================

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
                    ret = "Internet Connection Error";
                } else {

                    if (flag.equalsIgnoreCase("E")) {
                        String query = "SELECT Purpose FROM VisitorPurpose ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";


                    } else if (flag.equalsIgnoreCase("Check")) {
                        String query = "Select Top 1 SPName from ServiceprovidersDetails where SPName='" + vn + "' and (Category = 'Carpenter' OR" +
                                "  Category = 'Electrician' OR  Category = 'Internet' OR  Category = 'Medical' OR  Category = 'Painting' OR  Category = 'Pest Control' OR  Category = 'Plumber')";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";

                    }  else if (flag.equalsIgnoreCase("S")){

                        PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(MobileNo,VisitorFirstName,VisitorLastName,VAddress,TotalVisitors,VisitorName, OwnerDetails, Purpose, EnteredAt,Active,PredefinedVisitor,VisitorPhoto,SocietyName,OwnerFlatNo,OwnerBuildingName)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                        stmt.setString(1, mob);
                        stmt.setString(2, f_name);
                        stmt.setString(3, l_name);
                        stmt.setString(4, place);
                        stmt.setString(5, totVisitors);
                        stmt.setString(6, vn);
                        stmt.setString(7, wing + flat);
                        stmt.setString(8, purp);
                        stmt.setDate(9, sqlDate);
                        stmt.setString(10, "0");
                        stmt.setString(11, "Y");
                        stmt.setBytes(12,vbyteimage);
                        stmt.setString(13,societyname);
                        stmt.setString(14,wing+flat);
                        stmt.setString(15,buildingName);

                        int i = stmt.executeUpdate();
                        ret = "" + i;

                    } else if (flag.equalsIgnoreCase("D")){
                        String query = "SELECT TOP 1 VisitorLastName,VisitorFirstName,VAddress,VisitorPhoto,Purpose FROM VisitorDetails WHERE MobileNo='" + mob + "'  ORDER BY SrNo DESC ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";

                } else if (flag.equalsIgnoreCase("SPDetails")) {

                    PreparedStatement stmt = conn.prepareStatement("insert into ServiceprovidersDetails" + "(SPName,Category, Quality, Price, Punctuality, SocietyRating, OverAllRating,SocietyName,BuildingName)" + " values(?,?,?,?,?,?,?,?,?)");
                    //  PreparedStatement stmt = conn.prepareStatement("insert into VisitorDetails" + "(VisitorName,OwnerDetails,VehicleNumber,Purpose)" + " values(?,?,?,?)");
                    stmt.setString(1, vn);
                    stmt.setString(2, purp);
                    stmt.setString(3, "5");
                    stmt.setString(4, "5");
                    stmt.setString(5, "5");
                    stmt.setString(6, "5");
                    stmt.setString(7, "5");
                    stmt.setString(8, societyname);
                    stmt.setString(9, buildingName);

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
        try {
            if (flag.equalsIgnoreCase("E")) {
                try {
                    ArrayList<String> purpose = new ArrayList<String>();
                    purpose.add("SELECT PURPOSE");
                    while (rs.next()) {
                        purpose.add(rs.getString("Purpose"));
                    }
                    purpose_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, purpose);
                    VPurpose.setAdapter(purpose_adapter);
                } catch (Exception e) {
                    e.toString();
                }
            } else if (flag.equalsIgnoreCase("S")) {
                try {
                    Toast.makeText(getContext(), "Visitor Added Successfully !", Toast.LENGTH_SHORT).show();
//                    getActivity().finish();
                    new GetLongLat("Check").execute();
                } catch (Exception e) {
                    e.toString();
                }
            }
        else if (flag.equalsIgnoreCase("Check")) {
                try {
                    if (!rs.next()) {
                        new GetLongLat("SPDetails").execute();
                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(PreDefinedVisitors.this).commit();
                        Fragment f1 = new PreDefinedVisitors();
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
//                        getActivity().finish();
                    }
                } catch (Exception e) {
                    e.toString();
                }
            }
            else if (flag.equalsIgnoreCase("SPDetails")) {
                try {

                    getActivity().getSupportFragmentManager().beginTransaction().remove(PreDefinedVisitors.this).commit();
                    Fragment f1 = new PreDefinedVisitors();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_selection, f1).commit();
//                    getActivity().finish();

                } catch (Exception e) {
                    e.toString();
                }
            }
            else if (flag.equalsIgnoreCase("D")){
                try {
                    if (rs.next()) {
                        f_name = rs.getString("VisitorFirstName");
                        l_name = rs.getString("VisitorLastName");
                        place = rs.getString("VAddress");
                        fname.setText("" + f_name);
                        lname.setText("" + l_name);
                        address.setText("" + place);
                        if (rs.getBytes("VisitorPhoto") != null) {
                            vbyteimage = rs.getBytes("VisitorPhoto");
                            Bitmap bmp = BitmapFactory.decodeByteArray(vbyteimage, 0, vbyteimage.length);
                            vPhoto.setImageBitmap(bmp);
                        }
                        String compareValue = rs.getString("Purpose");
                        VPurpose.setSelection(purpose_adapter.getPosition(compareValue));

                    } else {
                        fname.setText(null);
                        lname.setText(null);
                        address.setText(null);
                        vbyteimage = null;
                        vPhoto.setBackgroundResource(R.drawable.profile);
                    }
                }catch (Exception e){
                    e.toString();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //================================================================================================

//-------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        ((SelectionActivity) getActivity())
                .setActionBarTitle("Predefine Visitor");


    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
