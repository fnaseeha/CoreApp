package com.example.user.lankabellapps.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.lankabellapps.R;

public class ReportingFragment extends Fragment {

    private RecyclerView rvView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reporting_to_approval,container,false);
        rvView = (RecyclerView) view.findViewById(R.id.rvView);

        rvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING activity_reporting_to_approval CLICK 1",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
