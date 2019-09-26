package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.adapters.DownloadingAdapter;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.CusVisit;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.SingleShotLocationProvider;
import com.example.user.lankabellapps.services.sync.VisitLogSync;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thejan on 2017-08-29.
 */

public class AddNewVisitPopup implements VisitLogSync.VisitSyncEvents{

    private LayoutInflater mLayoutInflater;
    private View mDialogView;
    private Dialog mListDialog;
    private Context mContext;
    private RecyclerView mRvSingleList;
    private EditText mSearchView, mName, mAddress, mContactNumber, mNicOrBrCode, mRemarks;
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

    private ImageButton mOk, mCancel;

    double currentLongi, currentLati;

    TextView mAddVisit;

    Customers mSelected;

    File sdcard;
    //DownloadTask downloadTask;

    private String currentAppName = "", currentPackageName = "", currentUrl = "";
    DownloadManager manager;
    long downloadId;
    DownloadManager.Request request;


    public AddNewVisitPopup(Context context, SingleItemPopupItemClickListener listener, LayoutInflater inflater, String callActivity) {
        mLayoutInflater = inflater;
        mContext = context;
        mListener = listener;
        this.callActivity = callActivity;
        configView();
    }

    public void makeDialog(String btnType, Customers selected) {

        mBtnType = btnType;
        mSelected = selected;

        if (mSelected != null) {

            mName.setText(mSelected.getNamem());
            mName.setEnabled(false);

            mNicOrBrCode.setText(mSelected.getCompanyCode());
            mNicOrBrCode.setEnabled(false);

            if (mSelected.getContactNo() != null) {
                if (!mSelected.getContactNo().trim().equals("null")) {
                    mContactNumber.setText(mSelected.getContactNo());
                }
            }

            String address = "", address1 = "", address2 = "", address3 = "";


            if (!mSelected.getAddress_line_1().equals("null")) {
                address1 = mSelected.getAddress_line_1();
            }

            if (!mSelected.getAddress_line_2().equals("null")) {
                address2 = mSelected.getAddress_line_2().trim();
            }


            if (!mSelected.getAddress_line_3().equals("null")) {
                address3 = mSelected.getAddress_line_3();
            }

            address = address1 + " " + address2 + " " + address3;

            mAddress.setText(address);

        }
//        else{
////            mNicOrBrCode.setText("NEW CUSTOMER");
////            mNicOrBrCode.setEnabled(false);
//
//        }


    }

    private void configView() {
        mListDialog = new Dialog(mContext, R.style.CustomDialog);
        mListDialog.setCancelable(true);
        mDialogView = mLayoutInflater.inflate(R.layout.add_visit_layout, null);
        mOk = (ImageButton) mDialogView.findViewById(R.id.ibtn_add_visit_ok);
        mCancel = (ImageButton) mDialogView.findViewById(R.id.ibtn_add_visit_cancel);

        mName = (EditText) mDialogView.findViewById(R.id.etv_add_visit_name);
        mAddress = (EditText) mDialogView.findViewById(R.id.etv_add_visit_address);
        mContactNumber = (EditText) mDialogView.findViewById(R.id.etv_add_visit_contact_number);
        mNicOrBrCode = (EditText) mDialogView.findViewById(R.id.etv_add_visit_nic_br);
        mRemarks = (EditText) mDialogView.findViewById(R.id.etv_add_visit_remark);


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDialog.dismiss();
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNicOrBrCode.getText().toString().trim().isEmpty() || mName.getText().toString().trim().isEmpty() || mAddress.getText().toString().trim().isEmpty() ||
                        mContactNumber.getText().toString().trim().isEmpty() || mRemarks.getText().toString().trim().isEmpty()) {
                    ((MainActivity) mContext).snakBarCommon("Fill all the fileds...", new ColoredSnackbar(mContext).TYPE_ERROR, 2000);
                } else {
                    mListDialog.dismiss();
                    mListener.onClickOk(mSelected);

                    ((MainActivity) mContext).progressBar("Geting current location...",1);

                    singleLocationRequest(mContext);

                }
            }
        });


        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 75;
        float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 92;


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

    public void singleLocationRequest(final Context context) {
        // when you need location
        // if inside activity context = this;

        SingleShotLocationProvider.requestSingleUpdate(context, new SingleShotLocationProvider.LocationCallback() {
            @Override
            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {

                Log.d("Location", "my location is " + location.toString());
                currentLongi = location.latitude;
                currentLati = location.longitude;


                ((MainActivity) mContext).progressBar("",0);

                CusVisit cusVisit = new CusVisit();
                cusVisit.setCusId(mNicOrBrCode.getText().toString());
                cusVisit.setAddress(mAddress.getText().toString());
                cusVisit.setContactNo(mContactNumber.getText().toString());
                cusVisit.setCusName(mName.getText().toString());
                cusVisit.setRemarks(mRemarks.getText().toString());
                cusVisit.setLati(String.valueOf(currentLati));
                cusVisit.setLongi(String.valueOf(currentLongi));
                cusVisit.setIsSycned("0");

                String timeDate = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(),"yyyy-MM-dd HH:mm:ss");

                cusVisit.setDatetime(timeDate);
                cusVisit.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(),"yyyy-MM-dd"));

                cusVisit.save();

                UserProfile userProfile = new UserProfile();

//                String result = mAddress.getText().toString().replaceAll("[|?*<\":>+\\[\\]/']", " ");
//                System.out.println(result);

                VisitLogSync visitLogSync = new VisitLogSync(AddNewVisitPopup.this);

/*timeDate 2019-03-01 11:33:21
    currentLati 6.9081166
    currentLongi 79.8504965
    getCusId - nic
    */
                visitLogSync.Visits(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword(),
                        mName.getText().toString().replaceAll("[|?*<\":>+\\[\\]/']", " "),
                        mContactNumber.getText().toString(), mRemarks.getText().toString().replaceAll("[|?*<\":>+\\[\\]/']", " "),
                        timeDate, String.valueOf(currentLati),
                        String.valueOf(currentLongi),mAddress.getText().toString().replaceAll("[|?*<\":>+\\[\\]/']", " "),
                        cusVisit.getCusId());

            }

            @Override
            public void notLocationAvailable(String msg) {
//                coloredSnackbar.dismissSnacBar();
//                coloredSnackbar.showSnackBar(msg, coloredSnackbar.TYPE_ERROR, 2000);
                ((MainActivity) mContext).snakBarCommon("Can not get a location...", new ColoredSnackbar(mContext).TYPE_ERROR, 2000);
            }
        });
    }

    @Override
    public void onVisitSuccess(GetCommonResponseModel update, String addedDate) {
        ((MainActivity) mContext).snakBarCommon("Visit added successfuly and Synced...", new ColoredSnackbar(mContext).TYPE_OK, 2000);


        CusVisit cusVisit = new CusVisit();
        cusVisit.updateIsSynced(addedDate,"1");
    }

    @Override
    public void onVisitFailed(String message) {
        ((MainActivity) mContext).snakBarCommon("Visit added successfuly not synced...", new ColoredSnackbar(mContext).TYPE_WARING, 2000);
    }


    public interface SingleItemPopupItemClickListener {
        void onClickOk(Customers selectedCus);

    }

}



