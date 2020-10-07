package com.mtech.resident;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.mtech.resident.Settings.Notify;
import com.mtech.resident.Settings.Password;
import com.mtech.resident.Settings.PinChange;
import com.mtech.resident.Settings.Profile;


public class Setting extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    Button profile, notify, pwd, pin;

    public Setting() {
        // Required empty public constructor
    }

    public static Setting newInstance(String param1, String param2) {

        Setting fragment = new Setting();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        profile = (Button) view.findViewById(R.id.profile);
        pwd = (Button) view.findViewById(R.id.password);
        notify = (Button) view.findViewById(R.id.notification);
        pin = (Button) view.findViewById(R.id.pinChange);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Profile.class);
                startActivity(i);
            }
        });
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Password.class);
                startActivity(i);
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Notify.class);
                startActivity(i);
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PinChange.class);
                startActivity(i);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//-------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        ((SelectionActivity) getActivity())
                .setActionBarTitle("Settings");


    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
