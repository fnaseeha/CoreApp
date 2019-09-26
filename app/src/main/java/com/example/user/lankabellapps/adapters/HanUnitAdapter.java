package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.UnitHand;

import java.util.List;

public class HanUnitAdapter extends RecyclerView.Adapter<HanUnitAdapter.Holder> {
    private Context context;
    private List<UnitHand> UnitHandList;

    public HanUnitAdapter(Context context, List<UnitHand> unitHandList) {
        this.context = context;
        UnitHandList = unitHandList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(context).inflate(R.layout.widget_hand_unit,viewGroup,false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        UnitHand unitHand = UnitHandList.get(i);
        holder.serial_no.setText(unitHand.getSerialNo());
        holder.item_code.setText(unitHand.getItemCode());
        holder.issue_date.setText(unitHand.getIssuedDate());
        holder.status.setText(unitHand.getStatus());


    }

    @Override
    public int getItemCount() {
        if(UnitHandList.size()>0){
            return UnitHandList.size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView serial_no,item_code,issue_date,status;
        public Holder(View itemView) {
            super(itemView);
            serial_no = itemView.findViewById(R.id.serial_no);
            item_code = itemView.findViewById(R.id.item_code);
            issue_date = itemView.findViewById(R.id.issue_date);
            status = itemView.findViewById(R.id.status);
        }

    }
}
