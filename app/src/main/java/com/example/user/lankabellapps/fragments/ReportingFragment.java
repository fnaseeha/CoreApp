package com.example.user.lankabellapps.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.ReportingToAdapter;
import com.example.user.lankabellapps.models.Reporting;

import java.util.ArrayList;

public class ReportingFragment extends Fragment implements ReportingToAdapter.ItemClickListener {

    private RecyclerView rvView;
    ReportingToAdapter adapter;
    private ArrayList<Reporting> ReportingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reporting_to_approval, container, false);
        rvView = (RecyclerView) view.findViewById(R.id.rvView);

        ReportingList = new ArrayList<>();
        setData();
         adapter = new ReportingToAdapter(getActivity(), ReportingList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(mLayoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());
        rvView.addItemDecoration(new DividerItemDecoration(rvView.getContext(), DividerItemDecoration.VERTICAL));
        rvView.setAdapter(adapter);
        rvView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();

        return view;
    }


    private void setData() {
        ReportingList.add(new Reporting("456312", "Mary", "2019/10/04 2:12:03 PM", "1", "Reesa", "Medical"));
        ReportingList.add(new Reporting("776330", "Nisa", "2019/10/05 8:12:26 AM", "1", "Lee", "Casual"));
        ReportingList.add(new Reporting("446375", "Taro", "2019/10/02 10:22:46 AM", "2", "Mary", "Annual"));
        ReportingList.add(new Reporting("467384", "Lee", "2019/10/01 4:12:08 PM", "0.5", "Ann", "Casual"));
        ReportingList.add(new Reporting("453299", "Ann", "2019/10/03 3:16:43 PM", "2", "Nisa", "Medical"));
    }

    @Override
    public void onClickSingleItem(Reporting selectedItem, int position) {
        showAlertDialog(R.layout.substitute_approve_detail, selectedItem);
    }

    private void showAlertDialog(int substitute_approve_detail, final Reporting selectedItem) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        View layoutView = getLayoutInflater().inflate(substitute_approve_detail, null);
        dialogBuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView emp_id = layoutView.findViewById(R.id.tv_emp_id);
        TextView name = layoutView.findViewById(R.id.tv_name);
        TextView leave_date = layoutView.findViewById(R.id.tv_leave_date);
        TextView applied_date = layoutView.findViewById(R.id.tv_applied_date);
        TextView type = layoutView.findViewById(R.id.tv_type);
        TextView no_of_days = layoutView.findViewById(R.id.tv_no_of_days);
        TextView sb_name = layoutView.findViewById(R.id.sb_name);
        LinearLayout ll_sb_name = layoutView.findViewById(R.id.ll_sb_name);
    //    LinearLayout ll_applied_date = layoutView.findViewById(R.id.ll_applied_date);

        ll_sb_name.setVisibility(View.VISIBLE);
   //     ll_applied_date.setVisibility(View.GONE);
        emp_id.setText(": "+selectedItem.getEmpNo());
        name.setText(": "+selectedItem.getName());
        leave_date.setText(": "+selectedItem.getDate());
        applied_date.setText(": "+selectedItem.getDate());
        type.setText(": "+selectedItem.getType());
        no_of_days.setText(": "+selectedItem.getNoOfDay());
        sb_name.setText(": "+selectedItem.getSubstitute());


        Button btn_ok = layoutView.findViewById(R.id.btn_ok);
        Button btn_cancel = layoutView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(layoutView);
        dialogBuilder.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportingList.remove(selectedItem);
                rvView.setAdapter(adapter);
                rvView.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();
                dialogBuilder.dismiss();
                Toast.makeText(getActivity(), "Approved", Toast.LENGTH_LONG).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
    }
}
