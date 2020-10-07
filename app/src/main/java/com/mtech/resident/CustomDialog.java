package com.mtech.resident;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.mtech.resident.dialogoncefreq.TabbedDialog;

public class CustomDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_dialog,container,false);
        Button oncebtn = (Button) view.findViewById(R.id.oncebtn);
        oncebtn.setOnClickListener(doneAction);
        Button freq = (Button) view.findViewById(R.id.freqbtn);
        freq.setOnClickListener(freqAction);
        return view;
   }

    View.OnClickListener doneAction = new View.OnClickListener() {

        @Override
         public void onClick(View v) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            TabbedDialog custom = new TabbedDialog();
            custom.show(fm,"");
         }
    };

    View.OnClickListener freqAction = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            TabbedDialog custom = new TabbedDialog();
            custom.show(fm,"");
        }
    };
}
