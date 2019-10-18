package com.example.user.lankabellapps.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.Reporting;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportingToAdapter extends RecyclerView.Adapter<ReportingToAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reporting> ReportingData;
    private ItemClickListener ItemClickListener;

    public ReportingToAdapter(Context context, ArrayList<Reporting> reportingData, ItemClickListener ItemClickListener) {
        this.context = context;
        ReportingData = reportingData;
        this.ItemClickListener = ItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.substitude_ic, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reporting reporting =  ReportingData.get(position);
        holder.date.setText(reporting.getDate());
        holder.empNo.setText("EMP NO "+reporting.getEmpNo());
        holder.name.setText(reporting.getName());
        holder.more.setOnClickListener(holder);
        holder.seq_no.setText(new DecimalFormat("00").format(position+1));
    }

    @Override
    public int getItemCount() {
        return ReportingData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView empNo,name,date,more,seq_no;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            seq_no = (itemView).findViewById(R.id.seq_no);
            empNo = (itemView).findViewById(R.id.EmpNo);
            name = (itemView).findViewById(R.id.tvName);
            date = (itemView).findViewById(R.id.tvDate);
            more = (itemView).findViewById(R.id.tvMore);

        }

        @Override
        public void onClick(View v) {
            ItemClickListener.onClickSingleItem(ReportingData.get(getAdapterPosition()),getAdapterPosition());
        }
    }
    public interface ItemClickListener {
        void onClickSingleItem(Reporting selectedItem, int position);

    }
}
