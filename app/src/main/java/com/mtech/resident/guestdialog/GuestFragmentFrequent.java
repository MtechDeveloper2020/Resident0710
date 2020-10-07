package com.mtech.resident.guestdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mtech.resident.R;

public class GuestFragmentFrequent extends Fragment {
    private String mText = "";

    public static GuestFragmentFrequent createInstance(String txt)
    {

        GuestFragmentFrequent fragment = new GuestFragmentFrequent();
//        fragment.mText = txt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.guest_frequent,container,false);
//        ((TextView) v.findViewById(R.id.textView)).setText(mText);
        return v;
    }
}