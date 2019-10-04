package com.example.user.lankabellapps.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.DateTimeSelectView;

import static android.R.layout.simple_spinner_dropdown_item;

public class LeaveFragment extends Fragment  {

    Spinner sp_leave_type, spHalf, sp_Substitue;
    TextView fromDate, fromTime, ToDate, ToTime;
    LinearLayout llhalfDay;
    CheckBox btnCheck;

    String[] arraySpinner;
    String[] arraySpinner2;
    String[] arraySpinner3;

    private Button btnTEST;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.leave, container, false);

        sp_leave_type = (Spinner) view.findViewById(R.id.sp_leave_type);
        sp_Substitue = (Spinner) view.findViewById(R.id.sp_Substitue);
        btnCheck = view.findViewById(R.id.btnCheck);
        spHalf = view.findViewById(R.id.spHalf);
        llhalfDay = view.findViewById(R.id.llhalfDay);
        fromDate = view.findViewById(R.id.fromDate);
        fromTime = view.findViewById(R.id.fromTime);
        ToDate = view.findViewById(R.id.ToDate);
        ToTime = view.findViewById(R.id.ToTime);

        arraySpinner = new String[]{"Annual", "Medical", "Casual", "Special Medical"};
        arraySpinner2 = new String[]{"First Half", "Second Half"};
        arraySpinner3 = new String[]{"ABC","DES", "DNSH", "NML", "ISR", "ZFR"};

        ArrayAdapter<String> adapter = null;

        adapter = new ArrayAdapter<String>((getContext()),
                simple_spinner_dropdown_item, arraySpinner);

        sp_leave_type.setPrompt("Select Leave Type");
        sp_leave_type.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = null;

        adapter2 = new ArrayAdapter<String>((getContext()),
                simple_spinner_dropdown_item, arraySpinner2);

        spHalf.setPrompt("Half Day Type");
        spHalf.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = null;

        adapter3 = new ArrayAdapter<String>((getContext()),
                simple_spinner_dropdown_item, arraySpinner3);

        sp_Substitue.setPrompt("Select a Substitute");
        sp_Substitue.setAdapter(adapter3);

        btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llhalfDay.setVisibility(View.VISIBLE);
                } else {
                    llhalfDay.setVisibility(View.INVISIBLE);
                }

            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToDateDialog(1, view, fromDate);
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToDateDialog(2,view,fromTime);
            }
        });

// Pick Date

        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToDateDialog(1, view, ToDate);
            }
        });

        ToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToDateDialog(2,view,ToTime);
            }
        });


        btnTEST = (Button) view.findViewById(R.id.btnSubmit);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public void CallToDateDialog(final int CallFrom, View view, final TextView fromDate) {

        DateTimeSelectView dateTimeSelectView = new DateTimeSelectView(getActivity(), getActivity().getFragmentManager(),
                new DateTimeSelectView.DateTimeSelectListener() {
            @Override
            public void onSet(String Mdate, String Mmonth, String Myear, String Mhours, String Mminuts,
                              String monthName, int CallFrom) {

                switch (CallFrom) {
                    case 1:
                        fromDate.setText(Myear + "-" + Mmonth + "-" + Mdate);
                        break;
                    case 2:
                        int hrs = Integer.parseInt(Mhours);
                        String AorP = "AM";
                        try {
                            if (Integer.parseInt(Mhours) > 12) {
                                hrs = Integer.parseInt(Mhours)-12;
                                AorP = "PM";
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(hrs == 0){
                            hrs = 12;
                        }
                        fromDate.setText(hrs + ":" + Mminuts+" "+AorP );
                        break;
                }
            }

            @Override
            public void onDateTimeSelectTextFocusOut(int position, String displayText) {

            }
        });

        switch (CallFrom) {
            case 1:
                dateTimeSelectView.ShowDialof(view, CallFrom, "");
                break;
            case 2:
                dateTimeSelectView.ShowDialof(view, CallFrom, "");
                break;

        }
    }

}
