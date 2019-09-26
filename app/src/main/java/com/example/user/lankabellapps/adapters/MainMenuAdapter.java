package com.example.user.lankabellapps.adapters;

/**
 * Created by user on 2017-03-27.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.MainMenuObject;
import com.example.user.lankabellapps.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.Holder> {

    private Context mContext;
    private List<MainMenuObject> mSingleItemList;
    private SingleItemClickListener mSingleItemClickListener;
    private String callFrom;

    public MainMenuAdapter(Context context, List<MainMenuObject> singleItemList, SingleItemClickListener singleItemClickListener, String callFrom) {
        mContext = context;
        mSingleItemList = singleItemList;
        mSingleItemClickListener = singleItemClickListener;
        this.callFrom = callFrom;
    }

    public void addItem(MainMenuObject item) {
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
        View row = LayoutInflater.from(mContext).inflate(R.layout.widget_recycleview_item, parent, false);

        return new Holder(row);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvItemValue.setText(mSingleItemList.get(position).itemText);
        holder.tvItemValue.setTag(mSingleItemList.get(position).itemKey);
        //holder.icon.setBackgroundResource(mSingleItemList.get(position).icon);

        if (mSingleItemList.get(position).itemKey.equals("2")) {
            //holder.iconBacoground.setBackgroundResource(R.drawable.attendence_in_background);

        } else if (mSingleItemList.get(position).itemKey.equals("1")) {
            //AvailableApps availableApps = new AvailableApps();
            //List<AvailableApps> availableAppses = availableApps.getAllApps();
            holder.icon.setImageResource(R.drawable.attendence_icon);
        } else {
            holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
            int x = holder.icon.getMaxWidth();
            int y = holder.icon.getMaxHeight();

            try {
                //Picasso.with(mContext).load(mSingleItemList.get(position).iconUrl).resize(x, y).into(holder.icon);

//                Picasso
//                        .with(mContext)
//                        .load(mSingleItemList.get(position).iconUrl)
//                        .resize(x, y)
//                        .onlyScaleDown()
//                        .into(holder.icon);

                Picasso.with(mContext)
                        .load("http://"+mSingleItemList.get(position).iconUrl)
                        .resize(x, y)
                        .onlyScaleDown()
                        .placeholder(R.drawable.common_icon)
                        .into(holder.icon);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (mSingleItemList.get(position).updateAvailable == 1) {
            holder.mUpdateAvailableImage.setVisibility(View.VISIBLE);
        } else {
            holder.mUpdateAvailableImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return null != mSingleItemList ? mSingleItemList.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvItemValue;
        ImageView icon, mUpdateAvailableImage;
        FrameLayout iconBacoground;

        public Holder(View itemView) {
            super(itemView);
            tvItemValue = (TextView) itemView.findViewById(R.id.tv_app_name);
            icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            iconBacoground = (FrameLayout) itemView.findViewById(R.id.rl_background);
            mUpdateAvailableImage = (ImageView) itemView.findViewById(R.id.iv_update_awailable_icon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            try {
                mSingleItemClickListener.onClickSingleItem(mSingleItemList.get(getAdapterPosition()), getAdapterPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public boolean onLongClick(View v) {
            System.out.println("long clicking");
            try {
                mSingleItemClickListener.onLongClickSingleItem(mSingleItemList.get(getAdapterPosition()), getAdapterPosition());
            } catch (Exception e) {
                System.out.println(e);
            }

            return true;
        }
    }

    public interface SingleItemClickListener {
        void onClickSingleItem(MainMenuObject selectedItem, int position);

        void onLongClickSingleItem(MainMenuObject selectedItem, int position);

    }


}