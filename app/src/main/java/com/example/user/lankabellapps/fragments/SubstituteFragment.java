package com.example.user.lankabellapps.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.SubstitudeAdapter;
import com.example.user.lankabellapps.models.Substitute;

import java.util.ArrayList;

public class SubstituteFragment extends Fragment implements SubstitudeAdapter.ItemClickListener {

    private RecyclerView rvView;
    private ArrayList<Substitute> SubstitueList;
    private SubstitudeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_substitute_approval, container, false);
        rvView = (RecyclerView) view.findViewById(R.id.rvView);
        SubstitueList = new ArrayList<>();
        setData();
        adapter = new SubstitudeAdapter(getActivity(), SubstitueList, this);

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
        SubstitueList.add(new Substitute("4563", "Ann", "2019/10/04 2:12:03 PM","2019/10/02 4:12:03 PM", "1", "Medical"));
        SubstitueList.add(new Substitute("7763", "Nisa", "2019/10/05 8:12:26 AM","2019/10/03 8:12:26 AM", "1", "Casual"));
        SubstitueList.add(new Substitute("4463", "Moosa", "2019/10/02 10:22:46 AM","2019/10/01 11:22:46 AM", "2", "Annual"));
        SubstitueList.add(new Substitute("4673", "Lee", "2019/10/01 4:12:08 PM","2019/10/01 3:12:08 PM", "0.5", "Casual"));
        SubstitueList.add(new Substitute("4532", "Taro", "2019/10/03 3:16:43 PM","2019/10/02 10:16:43 AM", "2", "Medical"));
        SubstitueList.add(new Substitute("4538", "Nisa", "2019/10/03 3:16:43 PM","2019/10/02 9:16:43 AM", "2", "Medical"));
    }

    @Override
    public void onClickSingleItem(Substitute selectedItem, int position) {
        showAlertDialog(R.layout.substitute_approve_detail, selectedItem);
    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialog(int substitute_approve_detail, final Substitute selectedItem) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        View layoutView = getLayoutInflater().inflate(substitute_approve_detail, null);
        dialogBuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView emp_id = layoutView.findViewById(R.id.tv_emp_id);
        TextView name = layoutView.findViewById(R.id.tv_name);
        TextView leave_date = layoutView.findViewById(R.id.tv_leave_date);
        TextView applied_date = layoutView.findViewById(R.id.tv_applied_date);
        TextView type = layoutView.findViewById(R.id.tv_type);
        TextView no_of_days = layoutView.findViewById(R.id.tv_no_of_days);

        emp_id.setText(": "+selectedItem.getEmpNo());
        name.setText(": "+selectedItem.getName());
        leave_date.setText(": "+selectedItem.getLeavedate());
        applied_date.setText(": "+selectedItem.getAppliedDate());
        type.setText(": "+selectedItem.getType());
        no_of_days.setText(": "+selectedItem.getNoOfDay());

        Button btn_ok = layoutView.findViewById(R.id.btn_ok);
        Button btn_cancel = layoutView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(layoutView);
        dialogBuilder.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubstitueList.remove(selectedItem);
                rvView.setAdapter(adapter);
                rvView.setNestedScrollingEnabled(false);
                adapter.notifyDataSetChanged();
                dialogBuilder.dismiss();
                Toast.makeText(getActivity(), "Confimed", Toast.LENGTH_LONG).show();
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
