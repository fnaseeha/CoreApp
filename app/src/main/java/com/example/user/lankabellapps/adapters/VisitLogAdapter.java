package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.CusVisit;
import com.example.user.lankabellapps.models.SingleItemModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Thejan Thrimanna on 2017-09-04.
 */

public class VisitLogAdapter extends RecyclerView.Adapter<VisitLogAdapter.Holder>{
    private Context mContext;
    private List<CusVisit> mSingleItemList;
    private SingleItemClickListener mSingleItemClickListener;
    private String callFrom;

    public VisitLogAdapter(Context context, List<CusVisit> singleItemList, SingleItemClickListener singleItemClickListener, String callFrom) {
        mContext = context;
        mSingleItemList = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
        this.callFrom = callFrom;
    }

    public void addItem(CusVisit item) {
        mSingleItemList.add(item);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        if (mSingleItemList.size() > 0) {
            mSingleItemList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_visit_log_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mName.setText(mSingleItemList.get(position).getCusName());
        holder.mAddress.setText(mSingleItemList.get(position).getAddress());
        holder.mPhone.setText(mSingleItemList.get(position).getContactNo());
        holder.mTime.setText(TimeFormatter.changeTimeFormat("yyyy-MM-dd HH:mm:ss","HH:mm:ss",mSingleItemList.get(position).getDatetime()));
        holder.mRemarks.setText(mSingleItemList.get(position).getRemarks());
    }

    @Override
    public int getItemCount() {
        return null != mSingleItemList ? mSingleItemList.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName, mTime, mAddress, mPhone, mRemarks;


        public Holder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_visit_log_name);
            mTime = (TextView) itemView.findViewById(R.id.tv_visit_log_time);
            mAddress = (TextView) itemView.findViewById(R.id.tv_visit_log_address);
            mPhone = (TextView) itemView.findViewById(R.id.tv_visit_log_phone);
            mRemarks = (TextView) itemView.findViewById(R.id.tv_visit_log_remarks);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }


    }

    public interface SingleItemClickListener {
        void onClickSingleItem(SingleItemModel selectedItem, int position);

    }


}

