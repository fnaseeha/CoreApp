package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.SingleItemModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 5/2/2016.
 */
public class SingleListAdapter extends RecyclerView.Adapter<SingleListAdapter.Holder> {

    private Context mContext;
    private List<SingleItemModel> mSingleItemList;
    private SingleItemClickListener mSingleItemClickListener;
    private String callFrom;

    public SingleListAdapter(Context context, List<SingleItemModel> singleItemList, SingleItemClickListener singleItemClickListener, String callFrom) {
        mContext = context;
        mSingleItemList = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
        this.callFrom = callFrom;
    }

    public void addItem(SingleItemModel item) {
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
        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_single_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvItemValue.setText(mSingleItemList.get(position).itemText);
        holder.tvItemValue.setTag(mSingleItemList.get(position).itemKey);



    }

    @Override
    public int getItemCount() {
        return null != mSingleItemList ? mSingleItemList.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvItemValue;
        CheckBox checkBox;

        public Holder(View itemView) {
            super(itemView);
            tvItemValue = (TextView) itemView.findViewById(R.id.tv_single_item_value);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);

        }


        @Override
        public void onClick(View v) {
            if (!callFrom.equals("account")) {
                mSingleItemClickListener.onClickSingleItem(mSingleItemList.get(getAdapterPosition()), getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //buttonView.get(getAdapterPosition(),getAdapterPosition());
            mSingleItemClickListener.onCheckChange(mSingleItemList.get(getAdapterPosition()), getAdapterPosition(), mSingleItemList.get(getAdapterPosition()).itemKey);
            mSingleItemList.get(getAdapterPosition()).isChecked = isChecked;

        }
    }

    public interface SingleItemClickListener {
        void onClickSingleItem(SingleItemModel selectedItem, int position);

        void onCheckChange(SingleItemModel checkedChanged, int position, String itemKey);
    }


}
