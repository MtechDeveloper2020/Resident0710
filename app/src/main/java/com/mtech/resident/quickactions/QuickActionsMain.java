package com.mtech.resident.quickactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.mtech.resident.CustomDialog;
import com.mtech.resident.R;
import com.mtech.resident.dialog.DeliveryDialog;
import com.mtech.resident.dialog.GuestDialog;
import com.mtech.resident.dialog.KidDialog;
import com.mtech.resident.dialog.SecurityAlertDialog;
import com.mtech.resident.dialog.SecurityMessageDialog;

public class QuickActionsMain extends Fragment {

    ImageView image1, image2, image3, image4, image5, image7, image8;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public QuickActionsMain() {
        // Required empty public constructor
    }

    public static QuickActionsMain newInstance(String param1, String param2) {
        QuickActionsMain fragment = new QuickActionsMain();
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
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_quick_actions_main, container, false);
        image1 = root.findViewById(R.id.image1);
        image2 = root.findViewById(R.id.image2);
        image3 = root.findViewById(R.id.image3);
        image4 = root.findViewById(R.id.image4);
        image7 = root.findViewById(R.id.image7);
        image8 = root.findViewById(R.id.image8);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CustomDialog custom = new CustomDialog();
                custom.show(fm,"");
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DeliveryDialog custom = new DeliveryDialog();
                custom.show(fm,"");
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                GuestDialog custom = new GuestDialog();
                custom.show(fm,"");
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                KidDialog custom = new KidDialog();
                custom.show(fm,"");
            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SecurityAlertDialog custom = new SecurityAlertDialog();
                custom.show(fm,"");
            }
        });
        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SecurityMessageDialog custom = new SecurityMessageDialog();
                custom.show(fm,"");
            }
        });

        return root;
    }
}