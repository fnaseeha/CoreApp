package com.example.user.lankabellapps.popups;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.DownloadingAdapter;
import com.example.user.lankabellapps.helper.CommonHelperClass;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.helper.FilterSingleItem;
import com.example.user.lankabellapps.helper.StringEmptyCheck;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.MainMenuObject;
import com.example.user.lankabellapps.models.SingleItemModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 8/7/2017.
 */

public class MainMenuOptionspopup {
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

    TextView mOpen, mUninstall, mDetails;

    MainMenuObject mSelected;

    File sdcard;
    //DownloadTask downloadTask;

    private String currentAppName = "", currentPackageName = "", currentUrl = "";
    DownloadManager manager;
    long downloadId;
    DownloadManager.Request request;


    public MainMenuOptionspopup(Context context, SingleItemPopupItemClickListener listener, LayoutInflater inflater, String callActivity) {
        mLayoutInflater = inflater;
        mContext = context;
        mListener = listener;
        this.callActivity = callActivity;
        configView();
    }

    public void makeDialog(String btnType, MainMenuObject selected) {

        mBtnType = btnType;
        mSelected = selected;
    }

    private void configView() {
        mListDialog = new Dialog(mContext, R.style.CustomDialog);
        mDialogView = mLayoutInflater.inflate(R.layout.main_menu_options_popup_layout, null);

        mOpen = (TextView) mDialogView.findViewById(R.id.tv_longpress_option_main_menu_Open);
        mDetails = (TextView) mDialogView.findViewById(R.id.tv_longpress_option_main_menu_Details);
        mUninstall = (TextView) mDialogView.findViewById(R.id.tv_longpress_option_main_menu_Uninstall);


//        mRvSingleList.setLayoutManager(new LinearLayoutManager(mContext));
//        mRvSingleList.setHasFixedSize(true);

//        mOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListDialog.dismiss();
//                mListener.onClickSingleItem(mSelected, String.valueOf(mOpen.getTag()),0);
//            }
//        });

        mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDialog.dismiss();
                mListener.onClickSingleItem(mSelected, String.valueOf(mDetails.getTag()),0);
            }
        });

        mUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDialog.dismiss();
                mListener.onClickSingleItem(mSelected, String.valueOf(mUninstall.getTag()),0);
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
        void onClickSingleItem(MainMenuObject selectedItem, String tag, int position);

    }

}

