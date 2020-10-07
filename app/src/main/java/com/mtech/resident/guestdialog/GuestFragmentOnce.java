package com.mtech.resident.guestdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mtech.resident.R;


public class GuestFragmentOnce extends Fragment {
    private String mText = "";

    public static GuestFragmentOnce createInstance(String txt)
    {
        GuestFragmentOnce fragment = new GuestFragmentOnce();
        fragment.mText = txt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.guest_once,container,false);
        ((TextView) v.findViewById(R.id.textView)).setText(mText);
        return v;
    }
}