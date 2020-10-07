package com.mtech.resident.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.mtech.resident.R;
import com.mtech.resident.guestdialog.GuestTabbedDialog;

public class GuestDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.guest_dialog,container,false);
        Button oncebtn = (Button) view.findViewById(R.id.oncebtn);
        oncebtn.setOnClickListener(doneAction);
        Button freq = (Button) view.findViewById(R.id.freqbtn);
        freq.setOnClickListener(freqAction);
        return view;
    }

    View.OnClickListener doneAction = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            GuestTabbedDialog custom = new GuestTabbedDialog();
            custom.show(fm,"");
        }
    };

    View.OnClickListener freqAction = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            GuestTabbedDialog custom = new GuestTabbedDialog();
            custom.show(fm,"");
        }
    };
}
