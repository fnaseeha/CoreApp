package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.Merchants;

import java.util.List;

/**
 * Created by Thejan on 5/28/2017.
 */

public class CusAdapter extends RecyclerView.Adapter<CusAdapter.Holder> {

    private Context mContext;
    private List<Merchants> mSingleItemList;
    private SingleItemClickListener mSingleItemClickListener;
    private String callFrom;

    public CusAdapter(Context context, List<Merchants> singleItemList, SingleItemClickListener singleItemClickListener, String callFrom) {
        mContext = context;
        mSingleItemList = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
        this.callFrom = callFrom;
    }

    public void addItem(Merchants item) {
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
    public CusAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_cus_item, parent, false);

        return new CusAdapter.Holder(row);
    }

    @Override
    public void onBindViewHolder(CusAdapter.Holder holder, int position) {
        holder.mName.setText(mSingleItemList.get(position).getMerchantName());

        if(mSingleItemList.get(position).getAgrimentStatus().equals("0")){
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }else{
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.green));
        }

        if(mSingleItemList.get(position).getIsSynced().equals("false")){
            holder.mBackground.setBackgroundResource(R.drawable.red_cureved);
        }else{
            holder.mBackground.setBackgroundResource(R.drawable.green_cureved);
        }

        if(!mSingleItemList.get(position).getMcity().equals("null")) {
            holder.mCity.setText(mSingleItemList.get(position).getMcity());
        }else{
            holder.mCity.setText("N/A");
        }
        holder.mId.setText(mSingleItemList.get(position).getMerchantId());
        holder.mAddress.setText(mSingleItemList.get(position).getAddress());
        holder.mPhoneNumber.setText(mSingleItemList.get(position).getTelephone());
    }

    @Override
    public int getItemCount() {
        return null != mSingleItemList ? mSingleItemList.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView mName, mCity, mId, mPhoneNumber, mAddress;
        LinearLayout mBackground;


        public Holder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_cus_item_name);
            mCity = (TextView) itemView.findViewById(R.id.tv_cus_item_city);
            mId = (TextView) itemView.findViewById(R.id.tv_cus_item_id);
            mPhoneNumber = (TextView) itemView.findViewById(R.id.tv_cus_item_phone);
            mAddress = (TextView) itemView.findViewById(R.id.tv_cus_item_address);
            mBackground = (LinearLayout) itemView.findViewById(R.id.ll_cus_item_background);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mSingleItemClickListener.onClickSingleItem(mSingleItemList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }

    public interface SingleItemClickListener {
        void onClickSingleItem(Merchants selectedItem, int position);

        void onCheckChange(Merchants checkedChanged, int position, String itemKey);
    }


}

