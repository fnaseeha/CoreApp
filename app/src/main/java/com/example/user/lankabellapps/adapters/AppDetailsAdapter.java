package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.AvailableApps;

import java.util.List;

/**
 * Created by Thejan on 7/28/2017.
 */

public class AppDetailsAdapter extends RecyclerView.Adapter<AppDetailsAdapter.Holder> {

    private Context mContext;
    private List<AvailableApps> mSingleItemList;
    private SingleItemClickListener mSingleItemClickListener;
    private String callFrom;

    public AppDetailsAdapter(Context context, List<AvailableApps> singleItemList, SingleItemClickListener singleItemClickListener, String callFrom) {
        mContext = context;
        mSingleItemList = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
        this.callFrom = callFrom;
    }

    public void addItem(AvailableApps item) {
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
        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_app_details_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mName.setText(mSingleItemList.get(position).getAppName());

        holder.mVersion.setText(mSingleItemList.get(position).getVersion());


    }

    @Override
    public int getItemCount() {
        return null != mSingleItemList ? mSingleItemList.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView mName, mVersion;


        public Holder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_app_details_name);
            mVersion = (TextView) itemView.findViewById(R.id.tv_app_details_version);
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
        void onClickSingleItem(AvailableApps selectedItem, int position);

        void onCheckChange(AvailableApps checkedChanged, int position, String itemKey);
    }


}


