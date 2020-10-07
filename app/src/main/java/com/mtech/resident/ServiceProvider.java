package com.mtech.resident;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mtech.resident.network.ConnectionDetector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceProvider.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceProvider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceProvider extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Spinner provider;
    ArrayAdapter<String> country_adapter;
    RadioButton Quality, Price, Punctuality;
    RadioGroup radioGroup;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    String daytype = null, societyname=null;
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
    String radiobtn_selection = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    SPListAdapter adapter = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    public ServiceProvider(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceProvider.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceProvider newInstance(String param1, String param2){
        ServiceProvider fragment = new ServiceProvider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        societyname = sp.getString("societyname", null);
        cd = new ConnectionDetector(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rpoints = new ArrayList<>();
        mob = new ArrayList<>();
        date = new ArrayList<>();
        purpose = new ArrayList<>();
        from = new ArrayList<>();
        srno = new ArrayList<>();
        photo = new ArrayList<>();


        provider = (Spinner) view.findViewById(R.id.provider);
        list = (ListView) view.findViewById(R.id.list);
        Quality = (RadioButton) view.findViewById(R.id.quality);
        Price = (RadioButton) view.findViewById(R.id.price);
        Punctuality = (RadioButton) view.findViewById(R.id.punctuality);
        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() != -1) {
                    int id = group.getCheckedRadioButtonId();
                    View radioButton = group.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) group.getChildAt(radioId);
                    radiobtn_selection = (String) btn.getText();
//                    Toast.makeText(getContext(), ""+radiobtn_selection, Toast.LENGTH_SHORT).show();

                    if (radiobtn_selection.equalsIgnoreCase("Quality")){
                        if (provider.getSelectedItem() != "SELECT PROVIDER"){
                            adapter.clear();
                            new GetLongLat("Q").execute();
                        } else {
                            Toast.makeText(getContext(), "Please select Provider", Toast.LENGTH_SHORT).show();
                        }
                    } else if (radiobtn_selection.equalsIgnoreCase("Price")){
                        if (provider.getSelectedItem() != "SELECT PROVIDER"){
                            adapter.clear();
                            new GetLongLat("Q").execute();
                        } else {
                            Toast.makeText(getContext(), "Please select Provider", Toast.LENGTH_SHORT).show();
                        }
                    } else if (radiobtn_selection.equalsIgnoreCase("Punctuality")){
                        if (provider.getSelectedItem() != "SELECT PROVIDER"){
                            adapter.clear();
                            new GetLongLat("Q").execute();
                        } else {
                            Toast.makeText(getContext(), "Please select Provider", Toast.LENGTH_SHORT).show();
                        }
                    }

//                    if(selection.equalsIgnoreCase("AadhaarNo")) {
//                        AadhaarNumber.setHint("Enter  AadhaarNumber...");
//                    }
//                    else if(selection.equalsIgnoreCase("VirtualId")) {
//                        AadhaarNumber.setHint("Enter  VirtualID...");
//                    }
//                    else if(selection.equalsIgnoreCase("UID Token")) {
//                        AadhaarNumber.setHint("Enter UID Token...");
//                    }
                }
            }
        });

    //Spinner onclick================================
        provider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (provider != null && provider.getSelectedItem() != "SELECT PROVIDER") {
                    daytype = (String) provider.getSelectedItem();
//                     Toast.makeText(getContext(), ""+daytype, Toast.LENGTH_SHORT).show();
                    if (adapter != null) {
                        adapter.clear();
                    }

                    new GetLongLat("K").execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ProviderDetails.class);
                intent.putExtra("name", rpoints.get(position));
                intent.putExtra("mobile", mob.get(position));
                intent.putExtra("purpose", purpose.get(position));
//                intent.putExtra("enteredAt",date.get(position));
//                intent.putExtra("vehicleNo",vehicle.get(position));
//                intent.putExtra("from",vaddress.get(position));
                intent.putExtra("visitorphoto", photo.get(position));
                startActivity(intent);


            }
        });
        //Spinner onclick===============================

        new GetLongLat("E").execute();

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
                        String query = "SELECT * FROM Serviceproviders";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    } else if (flag.equalsIgnoreCase("K")) {
//                        String query = "SELECT VisitorName,Purpose,VisitorPhoto,MobileNo FROM VisitorDetails where Purpose='"+daytype+"'";
//
                        String query = "SELECT distinct VisitorName,Purpose,CAST([VisitorPhoto] AS VARBINARY(8000)) AS VisitorPhoto,MobileNo \n" +
                                "FROM VisitorDetails WHERE SocietyName= '"+societyname+"'  AND Purpose='" + daytype + "'   AND\n" +
                                " VisitorPhoto IS NOT NULL ";
//                        String query = "SELECT distinct  VD.VisitorName,VD.Purpose,CAST([VisitorPhoto] AS VARBINARY(8000)) AS VisitorPhoto,VD.MobileNo\n" +
//                                ", SPD.Quality,SPD.Price, SPD.Punctuality,SPD.SocietyRating, SPD.OverAllRating\n" +
//                                "FROM VisitorDetails AS VD INNER JOIN \n" +
//                                "ServiceprovidersDetails AS SPD ON VD.VisitorName=SPD.SPName WHERE VD.Purpose='"+daytype+"'";
                        Statement stmt = conn.createStatement();
//                        Statement stmt1 = conn.createStatement();
                        rs = stmt.executeQuery(query);
//                        rs1 = stmt.executeQuery(query1);
                        ret = "1";
                    } else if (flag.equalsIgnoreCase("Q")) {
//                        String query = "SELECT VisitorName,Purpose,VisitorPhoto,MobileNo,SPD."+radiobtn_selection+" FROM VisitorDetails VD " +
//                                "INNER JOIN ServiceprovidersDetails SPD ON VD.VisitorName = SPD.SPName " +
//                                " where Purpose='"+daytype+"' ORDER BY SPD."+radiobtn_selection+" DESC  ";

                        String query = "SELECT  DISTINCT VD.VisitorName,VD.Purpose,CAST([VisitorPhoto] AS VARBINARY(8000)) AS VisitorPhoto,VD.MobileNo" +
                                ", SPD.Quality,SPD.Price, SPD.Punctuality,SPD.SocietyRating, SPD.OverAllRating\n" +
                                "FROM VisitorDetails AS VD \n" +
                                "INNER JOIN \n" +
                                "ServiceprovidersDetails AS SPD ON VD.VisitorName=SPD.SPName WHERE VD.SocietyName= '"+societyname+"'  AND VD.Purpose='" + daytype + "'   AND\n" +
                                " VisitorPhoto IS NOT NULL ";
                        Statement stmt = conn.createStatement();
//                        Statement stmt1 = conn.createStatement();
                        rs = stmt.executeQuery(query);
//                        rs1 = stmt.executeQuery(query1);
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
            ArrayList<String> country = new ArrayList<>();
            country.add("SELECT PROVIDER");
            if (flag.equalsIgnoreCase("E")) {
                while (rs.next()) {
                    country.add(rs.getString("Category"));
                }
                country_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, country);
                provider.setAdapter(country_adapter);


//                    provider_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sprovider);
//                    provider.setAdapter(provider_adapter);

            }

            if (flag.equalsIgnoreCase("K")) {
                while (rs.next()) {
                    String a = rs.getString("VisitorName");
                    rpoints.add(a);
                    String b = rs.getString("MobileNo");
                    mob.add(b);
//                    String c = String.valueOf(rs.getDate("EnteredAt"));
//                    date.add(c);
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

                adapter = new SPListAdapter(getActivity(), rpoints, mob, purpose, photo, srno);
                list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

            }
            if (flag.equalsIgnoreCase("Q")){
                while (rs.next()){
                    String a = rs.getString("VisitorName");
                    rpoints.add(a);
                    String b = rs.getString("MobileNo");
                    mob.add(b);
//                    String c = String.valueOf(rs.getDate("EnteredAt"));
//                    date.add(c);
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

                adapter = new SPListAdapter(getActivity(), rpoints, mob, purpose, photo, srno);
                list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
//-------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        ((SelectionActivity) getActivity())
                .setActionBarTitle("Service Provider");

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}