package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.DownloadingAdapter;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.MainMenuObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 2017-08-30.
 */

public class CustomerOptionsPopup {
    private LayoutInflater mLayoutInflater;
    private View mDialogView;
    private Dialog mListDialog;
    private Context mContext;
    private RecyclerView mRvSingleList;
    private EditText mSearchView;
    private TextView mHeaderTitle;
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private RelativeLayout mRelSearchContainer;
    private DownloadingAdapter mAdapter;
    private SingleItemPopupItemClickListener mListener;
    private List<DownloadItemModel> mSingleList = new ArrayList<>();
    private String mBtnType;
    private ImageButton btnOk;
    private int checkStatus = 0;
    private String callActivity;

    TextView mAddVisit;

    Customers mSelected;

    File sdcard;
    //DownloadTask downloadTask;

    private String currentAppName = "", currentPackageName = "", currentUrl = "";
    DownloadManager manager;
    long downloadId;
    DownloadManager.Request request;


    public CustomerOptionsPopup(Context context, SingleItemPopupItemClickListener listener, LayoutInflater inflater, String callActivity) {
        mLayoutInflater = inflater;
        mContext = context;
        mListener = listener;
        this.callActivity = callActivity;
        configView();
    }

    public void makeDialog(String btnType, Customers selected) {

        mBtnType = btnType;
        mSelected = selected;

    }

    private void configView() {
        mListDialog = new Dialog(mContext, R.style.CustomDialog);
        mListDialog.setCancelable(true);
        mDialogView = mLayoutInflater.inflate(R.layout.customer_optios_popup_layout, null);

        mAddVisit = (TextView) mDialogView.findViewById(R.id.tv_longpress_customer_options_add_visit);

        mAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDialog.dismiss();
                mListener.onClickSingleItem(1, mSelected);
            }
        });

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 30;
        float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 80;


        final float x, y;
        DisplayPixelCalculator converter = new DisplayPixelCalculator();
        x = converter.dipToPixels(mContext, dpWidth);
        y = converter.dipToPixels(mContext, dpHeight);

        mListDialog.setContentView(mDialogView);
        mListDialog.show();
        mListDialog.setCanceledOnTouchOutside(false);
        mListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mListDialog.getWindow().setLayout((int) x, (int) y);
    }


    public interface SingleItemPopupItemClickListener {
        void onClickSingleItem(int selectedOption, Customers selectedCus);

    }






}


