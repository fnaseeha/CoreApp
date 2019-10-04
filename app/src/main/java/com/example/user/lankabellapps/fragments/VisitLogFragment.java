package com.example.user.lankabellapps.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.VisitLogAdapter;
import com.example.user.lankabellapps.helper.DateTimeSelectView;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.CusVisit;
import com.example.user.lankabellapps.models.SingleItemModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitLogFragment extends Fragment implements VisitLogAdapter.SingleItemClickListener{

    TextView mDate, mNoItems;
    RecyclerView mRecyclerView;
    View view;
    List<CusVisit> cusVisitList = new ArrayList<>();


    public VisitLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            mDate.setText(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(),"yyyy-MM-dd"));
            getVisitListByDate(mDate.getText().toString());
            setRecyclerVIew();
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_visi_log, container, false);

        mDate = (TextView) view.findViewById(R.id.tv_visitlog_date);
        mNoItems = (TextView) view.findViewById(R.id.tv_not_items_visit);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_visitlog_recyclerview);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToDateDialog(1, view);
            }
        });

        init();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    private void init() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

    }

    private void getVisitListByDate(String date) {
        CusVisit cusVisit = new CusVisit();
        cusVisitList = cusVisit.getVisitsByDate(date);
    }

    private void setRecyclerVIew(){
        VisitLogAdapter visitLogAdapter = new VisitLogAdapter(getActivity(),cusVisitList,this,"");
        mRecyclerView.setAdapter(visitLogAdapter);

        if(cusVisitList.isEmpty()){
            mNoItems.setText("No Visits for " + mDate.getText().toString());
            mNoItems.setVisibility(View.VISIBLE);
        }else{
            mNoItems.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClickSingleItem(SingleItemModel selectedItem, int position) {

    }


    public void CallToDateDialog(final int CallFrom, View view) {

        DateTimeSelectView dateTimeSelectView = new DateTimeSelectView(getActivity(), getActivity().getFragmentManager(), new DateTimeSelectView.DateTimeSelectListener() {
            @Override
            public void onSet(String Mdate, String Mmonth, String Myear, String Mhours, String Mminuts, String monthName, int CallFrom) {
                System.out.println("addedTime picked");
                //                String formattedDate = String.format("%03d",date);

                String timeDisplay;

                timeDisplay = Mdate + "-" + Mmonth + "-" + Myear + " " + Mhours + ":" + Mminuts;

                String timeString = Myear + "-" + Mmonth + "-" + Mdate + "T" + Mhours + ":" + Mminuts + ":" + "00";

                switch (CallFrom) {
                    case 1:
                        //selectedDate = Mdate + "/" + Mmonth + "/" + Myear;
                        mDate.setText(Myear + "-" + Mmonth + "-" + Mdate);
                        break;
                    case 3:



                }
                getVisitListByDate(mDate.getText().toString());
                setRecyclerVIew();
            }

            @Override
            public void onDateTimeSelectTextFocusOut(int position, String displayText) {

            }
        });

        switch (CallFrom) {
            case 1:
                dateTimeSelectView.ShowDialof(view, CallFrom, "");
                break;

        }
    }


}
