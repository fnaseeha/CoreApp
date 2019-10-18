package com.example.user.lankabellapps.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.DateTimeSelectView;
import com.example.user.lankabellapps.popups.UpdateAppsPopup;

import static android.R.layout.browser_link_context_header;
import static android.R.layout.simple_spinner_dropdown_item;

public class LeaveFragment extends Fragment  {

    Spinner sp_leave_type, spHalf, sp_Substitue;
    TextView fromDate, fromTime, ToDate, ToTime;
    LinearLayout llhalfDay;
    CheckBox btnCheck;

    String[] arraySpinner;
    String[] arraySpinner2;
    String[] arraySpinner3;

    private Button btnSubmit,btnClear;
    EditText etReason;
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
        etReason = view.findViewById(R.id.etReason);

        arraySpinner = new String[]{"Annual", "Medical", "Casual", "Special Medical"};
        arraySpinner2 = new String[]{"First Half ", "Second Half "};
        arraySpinner3 = new String[]{"Ann","Taro", "Lee", "Roosa", "Moosa", "Nisa"};

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


        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmation(1);


            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmation(2);
            }
        });


        return view;
    }

    private void showConfirmation(int num) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setPositiveButton("OK", null);


       /* final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        LayoutInflater i = LayoutInflater.from(getContext());
        View view = i.inflate(R.layout.dialog_confirmation, null);

        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView message = (TextView) view.findViewById(R.id.tv_message);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);*/

        switch(num) {
            case 1: {
                builder.setMessage("Do you want to Request a Leave?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        clear();
                        Toast.makeText(getActivity(),"Submitted",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        });
                builder.show();
                break;
            }
            case 2: {
                builder.setMessage("Do you want to Clear?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        clear();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                builder.show();
                break;
            }
        }
    }

    private void clear() {
        fromDate.setText("");
        ToDate.setText("");
        fromTime.setText("");
        ToTime.setText("");
        etReason.setText("");
        btnCheck.setChecked(false);
        llhalfDay.setVisibility(View.INVISIBLE);
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
