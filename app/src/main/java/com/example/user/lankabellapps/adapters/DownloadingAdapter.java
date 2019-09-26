package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.models.DownloadItemModel;

import java.util.List;

/**
 * Created by user on 2017-04-05.
 */

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.Holder> {

    private Context mContext;
    private List<DownloadItemModel> mDownloadItemModel;
    private SingleItemClickListener mSingleItemClickListener;

    public DownloadingAdapter(Context context, List<DownloadItemModel> singleItemList, SingleItemClickListener singleItemClickListener) {
        mContext = context;
        mDownloadItemModel = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
    }

    public void addItem(DownloadItemModel item) {
        mDownloadItemModel.add(item);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        if (mDownloadItemModel.size()>0) {
            mDownloadItemModel.clear();
            notifyDataSetChanged();
        }
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_download_item, parent, false);
        return new Holder(row);

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.icon.setBackgroundResource(mDownloadItemModel.get(position).icon);
        holder.name.setText(mDownloadItemModel.get(position).itemText);

    }

    @Override
    public int getItemCount() {
        return null != mDownloadItemModel ? mDownloadItemModel.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView name;
        Button install;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_app_icon_dialog);
            name = (TextView) itemView.findViewById(R.id.tv_app_name_dialog);
            install = (Button) itemView.findViewById(R.id.btn_install_dialog);
            //itemView.setOnClickListener(this);
            itemView.findViewById(R.id.btn_install_dialog).setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mSingleItemClickListener.onClickSingleItem(mDownloadItemModel.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface SingleItemClickListener {
        void onClickSingleItem(DownloadItemModel selectedItem, int position);
    }


}
