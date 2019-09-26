package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.DownloadingAdapter;
import com.example.user.lankabellapps.helper.CommonHelperClass;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.DownloadItemModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thejan on 5/31/2016.
 */
public class CustomerSearchLongPressPopup implements DownloadingAdapter.SingleItemClickListener {

    private Context mActivity;
    private int mPosition;
    private String mMessage;
    private ConfirmationCallback mDelegate;

    public CustomerSearchLongPressPopup(Context context){
        this.mActivity = context;
    }


    public void show(String Hname) {
        final Dialog longpressDialog = new Dialog(mActivity, R.style.CustomDialog);
        //LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View msgView = inflater.inflate(R.layout.dialog_confirmation, null);

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        final View msgView = inflater.inflate(R.layout.option_popu, null);

        Button addToSchedule = (Button) msgView.findViewById(R.id.btn_add_to_schedule);


        TextView mTitle = (TextView) msgView.findViewById(R.id.tv_dialog_header_title);

        ImageButton close = (ImageButton) msgView.findViewById(R.id.img_btn_close);



        DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
        float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 57;
        float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 90;

        final float x, y;
        DisplayPixelCalculator converter = new DisplayPixelCalculator();
        x = converter.dipToPixels(mActivity, dpWidth);
        y = converter.dipToPixels(mActivity, dpHeight);

        longpressDialog.setContentView(msgView);
        longpressDialog.show();
        longpressDialog.setCanceledOnTouchOutside(false);
        longpressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        longpressDialog.getWindow().setLayout((int) x, (int) y);

        mTitle.setText("Options");




        addToSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDelegate.onClickConfirmation(mMessage,mPosition,longpressDialog);
                ((MainActivity)mActivity).dialogResult(1);


            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDelegate.onClickConfirmation(mMessage,mPosition,longpressDialog);
                longpressDialog.dismiss();
            }
        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                longpressDialog.dismiss();
//            }
//        });

//        longpressDialog.setContentView(msgView);
//        longpressDialog.show();
//        longpressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClickSingleItem(DownloadItemModel selectedItem, int position) {

    }

    public interface ConfirmationCallback {
        void onClickConfirmation(String message, int position, Dialog dialogInstance);
    }






}
