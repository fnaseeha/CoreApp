package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.Reporting;

import java.util.ArrayList;

public class RepotingToAdapter extends RecyclerView.Adapter<RepotingToAdapter.MyViewHoler> {

    private Context context;
    private ArrayList<Reporting> ReportingData = new ArrayList<>();
    private AppDetailsAdapter.SingleItemClickListener mSingleItemClickListener;

    public RepotingToAdapter(Context context, ArrayList<Reporting> reportingData, AppDetailsAdapter.SingleItemClickListener mSingleItemClickListener) {
        this.context = context;
        ReportingData = reportingData;
        this.mSingleItemClickListener = mSingleItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.substitude_ic, viewGroup, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler myViewHoler, int i) {

    }

    @Override
    public int getItemCount() {
        return ReportingData.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {

        TextView empNo,name,date,more;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            empNo = (itemView).findViewById(R.id.EmpNo);
            empNo = (itemView).findViewById(R.id.EmpNo);
            empNo = (itemView).findViewById(R.id.EmpNo);
            empNo = (itemView).findViewById(R.id.EmpNo);

        }
    }
}
