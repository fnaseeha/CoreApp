package com.example.user.lankabellapps.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Browser;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.BankList;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.MerchantsCities;
import com.example.user.lankabellapps.models.SingleItemModel;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.example.user.lankabellapps.popups.SingleItemSearchAndSelectPopup;
import com.example.user.lankabellapps.services.sync.MerchantSync;
import com.example.user.lankabellapps.services.sync.MerchantUpdateSync;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMerchantsActivity extends AppCompatActivity implements MerchantSync.MerchantSyncEvents, SingleItemSearchAndSelectPopup.SingleItemPopupItemClickListener, MerchantUpdateSync.updateMerchantsList{

    @Bind(R.id.tv_title)
    TextView mTitle;

    @Bind(R.id.iv_menu)
    ImageView mMenu;

    @Bind(R.id.btnClear)
    Button mClear;

    @Bind(R.id.btnAddMerchant)
    Button mAddMerchants;

    @Bind(R.id.etvmName)
    EditText mName;

    @Bind(R.id.etvmAddress)
    EditText mAddress;

    @Bind(R.id.etvmContactNo)
    EditText mContactNo;

    @Bind(R.id.etvmAccountNumber)
    EditText mAccouyntNumber;

    @Bind(R.id.etvmBank)
    TextView mBank;

    @Bind(R.id.etvmBankName)
    TextView mBankName;

    @Bind(R.id.etvmNic)
    EditText mNic;

    @Bind(R.id.tvmCity)
    EditText mCity;

    @Bind(R.id.tvLocation)
    TextView mLocation;

    //ImageView mMenu;
    ColoredSnackbar coloredSnackbar;

    int saveStatus;

    String currentMerchantsId;
    Merchants currentMerchants;

    String epf;
    List<TSRDetails> tsrDetailsList;
    LocationManager locationManager;
    //Button mClear, mAdd;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchants);

        ButterKnife.bind(this);
        mMenu.setImageResource(R.drawable.back);

        coloredSnackbar = new ColoredSnackbar(this);

        saveStatus = getIntent().getExtras().getInt("status");

        if (saveStatus == 1) {
            mAddMerchants.setText("ADD");
            mTitle.setText("Add Merchant");

            //mClear.setVisibility(View.VISIBLE);
            mClear.setText("Clear");
        } else {
            mAddMerchants.setText("UPDATE");
            mTitle.setText("Update Merchant");
            //mClear.setVisibility(View.GONE);

            mClear.setText("Agreement");

            currentMerchantsId = getIntent().getExtras().getString("id");

            Merchants merchants = new Merchants();
            currentMerchants = merchants.getMerchantById(currentMerchantsId).get(0);

            setDataValues();
        }

        init();

    }

    private void setDataValues() {

        mName.setText(currentMerchants.getMerchantName());
        mName.setEnabled(false);
        mName.setTextColor(getResources().getColor(R.color.disablecolor));

        mAddress.setText(currentMerchants.getAddress());
        //mAddress.setEnabled(false);

        mContactNo.setText(currentMerchants.getTelephone());
        //mContactNo.setEnabled(false);

        mCity.setText(currentMerchants.getMcity());
        mCity.setEnabled(false);
        mCity.setTextColor(getResources().getColor(R.color.disablecolor));

        mAccouyntNumber.setText(currentMerchants.getBankAccId());
        if(!mAccouyntNumber.getText().toString().isEmpty()) {
            mAccouyntNumber.setEnabled(false);
            mAccouyntNumber.setTextColor(getResources().getColor(R.color.disablecolor));
        }


        mBank.setText(currentMerchants.getBank());
        if(!mBank.getText().toString().isEmpty()) {
            mBank.setEnabled(false);
            mBank.setTextColor(getResources().getColor(R.color.disablecolor));
        }

        mBankName.setText(currentMerchants.getBankAccName());
        if(!mBankName.getText().toString().isEmpty()) {
            mBankName.setEnabled(false);
            mBankName.setTextColor(getResources().getColor(R.color.disablecolor));
        }


        mNic.setText(currentMerchants.getNic());
        if(!mNic.getText().toString().isEmpty()) {
            mNic.setEnabled(false);
            mNic.setTextColor(getResources().getColor(R.color.disablecolor));
        }


        if(mAccouyntNumber.getText().toString().isEmpty() || mBank.getText().toString().isEmpty() || mNic.getText().toString().isEmpty()){
            mClear.setVisibility(View.GONE);
        }else{
            if(currentMerchants.getAgrimentStatus().equals("0")) {
                mClear.setVisibility(View.VISIBLE);
            }else{
                mClear.setVisibility(View.GONE);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {

        TSRDetails tsrDetails = new TSRDetails();
        tsrDetailsList = new ArrayList<>();

        tsrDetailsList = tsrDetails.getAllData();

        epf = tsrDetailsList.get(0).getEpfNo();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mCity.setShowSoftInputOnFocus(false);
        mCity.setCursorVisible(false);
        mCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    onClickofCity();
                }
            }
        });

        mBank.setShowSoftInputOnFocus(false);
        mBank.setCursorVisible(false);
        mBank.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    onClickBank();
                }
            }
        });



    }

    private void onClickBank() {

        BankList merchantsCities = new BankList();
        List<BankList> merchantsCitiesList = new ArrayList<>();

        final List<SingleItemModel> list1 = new ArrayList<>();

        merchantsCitiesList = merchantsCities.getAllBanks();

        for (BankList m : merchantsCitiesList) {
            SingleItemModel s = new SingleItemModel();

            s.itemText = m.getName();
            s.itemKey = m.getCode();

            list1.add(s);
        }

        final LayoutInflater layoutInflater1 = getLayoutInflater();

        SingleItemSearchAndSelectPopup dialog1 = new SingleItemSearchAndSelectPopup(this, this, layoutInflater1, "");
        dialog1.makeDialog("Select a Bank", true, list1, "2");


    }

    @OnClick(R.id.iv_menu)
    public void OnClickBack(View view) {
        onBackPressed();
    }

    @OnClick(R.id.btnClear)
    public void OnClearClearButton(View view) {
        if(saveStatus == 1) {
            clearFields();
        }else{
            addAgriment();
        }
    }

    @OnClick(R.id.btnAddMerchant)
    public void OnClickAddMerchant(View view) {
        if (saveStatus == 1) {
            new AppResetConfirmation(this).ShowConfirmation(7, "Options");
        } else {
            new AppResetConfirmation(this).ShowConfirmation(8, "Options");
        }
    }

    @OnClick(R.id.tvmCity)
    public void OnClickCityText(View view) {

        onClickofCity();
    }

    private void onClickofCity() {
        MerchantsCities merchantsCities = new MerchantsCities();
        List<MerchantsCities> merchantsCitiesList = new ArrayList<>();

        final List<SingleItemModel> list1 = new ArrayList<>();

        merchantsCitiesList = merchantsCities.getAllMerchantsCities();

        for (MerchantsCities m : merchantsCitiesList) {
            SingleItemModel s = new SingleItemModel();

            s.itemText = m.getCity();
            s.itemKey = m.getPostalCode();

            list1.add(s);
        }

        final LayoutInflater layoutInflater1 = getLayoutInflater();

        SingleItemSearchAndSelectPopup dialog1 = new SingleItemSearchAndSelectPopup(this, this, layoutInflater1, "");
        dialog1.makeDialog("Select a City", true, list1, "1");
    }

    @OnClick(R.id.etvmBank)
    public void OnClickBankText(View view) {
       onClickBank();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private void clearFields() {
        mName.setText("");
        mAddress.setText("");
        mContactNo.setText("");
        mCity.setText("");
        mAccouyntNumber.setText("");
        mNic.setText("");
        mAccouyntNumber.setText("");
        mBankName.setText("");
        mBank.setText("");
        mNic.setText("");
    }

    // public void ma

    private void addAgriment(){
        try {
            String url = Constants.imageUrl + currentMerchantsId;

            //Test http://119.235.1.59:8090/ImageUpload.aspx?MerchantID=
            //Live http://119.235.1.88:8092/ImageUpload.aspx?MerchantID=
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);

            UserProfile userProfile = new UserProfile();

            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW, Uri.parse(url));
            Bundle bundle = new Bundle();
            bundle.putString("un_1542qwds", userProfile.getAllUsers().get(0).getUserName());
            bundle.putString("pass_7859zdcs", userProfile.getAllUsers().get(0).getPassword());
            browserIntent.putExtra(Browser.EXTRA_HEADERS, bundle);
            startActivity(browserIntent);

        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void checkforUpdate() {
        if (mName.getText().toString().trim().isEmpty() || mAddress.getText().toString().trim().isEmpty() || mContactNo.getText().toString().trim().isEmpty() || mAccouyntNumber.getText().toString().trim().isEmpty() || mBank.getText().toString().trim().isEmpty() || mNic.getText().toString().trim().isEmpty()|| mBankName.getText().toString().trim().isEmpty()) {
            coloredSnackbar.showSnackBar("Fill all the fields...", coloredSnackbar.TYPE_ERROR, 1000);

        } else {
            new Merchants().updateMerchantsNewAddedData(mAddress.getText().toString(), mContactNo.getText().toString(),currentMerchantsId, mBank.getText().toString(), mAccouyntNumber.getText().toString(), mNic.getText().toString(),mBankName.getText().toString());
            coloredSnackbar.showSnackBar("Updated...", coloredSnackbar.TYPE_OK, 2000);

            syncUpdatedMerchants();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if(currentMerchants.getAgrimentStatus().equals("0")){
                        addAgriment();
                    }

                    finish();
                }
            }, 2000);
        }
    }


    private void checkFieldsValidation() {

        if (mName.getText().toString().trim().isEmpty() || mAddress.getText().toString().trim().isEmpty() || mContactNo.getText().toString().trim().isEmpty() || mAccouyntNumber.getText().toString().trim().isEmpty() || mBank.getText().toString().trim().isEmpty() || mNic.getText().toString().trim().isEmpty()) {
            coloredSnackbar.showSnackBar("Fill all the fields...", coloredSnackbar.TYPE_ERROR, 1000);

        } else {

            if (mCity.getText().toString().trim().isEmpty()) {
                coloredSnackbar.showSnackBar("Fill all the fields...", coloredSnackbar.TYPE_ERROR, 1000);
            } else {

                int nextMerchantId = Integer.parseInt(tsrDetailsList.get(0).getNextMerchantNo()) + 1;

                Merchants merchants = new Merchants();
                merchants.setMerchantId(nextMerchantId + epf);
                merchants.setMerchantName(mName.getText().toString());
                merchants.setAddress(mAddress.getText().toString());
                merchants.setTelephone(mContactNo.getText().toString());
                merchants.setMcity(mCity.getText().toString());
                merchants.setBankAccId(mAccouyntNumber.getText().toString());
                merchants.setBank(mBank.getText().toString());
                merchants.setBankAccName(mBankName.getText().toString());
                merchants.setNic(mNic.getText().toString());
                merchants.setIsSynced("false");
                merchants.setAgrimentStatus("0");
                merchants.save();

                new TSRDetails().updateIsSynced(nextMerchantId + "");

                clearFields();

                syncMerchants();

                coloredSnackbar.showSnackBar("Saved...", coloredSnackbar.TYPE_OK, 2000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        finish();
                    }
                }, 2000);


            }
        }

    }

    private void syncMerchants() {
        Merchants merchants1 = new Merchants();
        List<Merchants> merchantsList = new ArrayList<>();

        merchantsList = merchants1.getAllNotSyncMerchants();

        for (Merchants m : merchantsList) {
            MerchantSync merchantSync = new MerchantSync(this);
            merchantSync.setMerchants(epf, m.getMerchantId(), m.getMerchantName(), m.getAddress(), m.getTelephone(), m.getMcity(), m.getBankAccId(), m.getBank(), m.getNic(),m.getBankAccName());
        }
    }

    private void syncUpdatedMerchants(){
        Merchants merchants1 = new Merchants();
        List<Merchants> merchantsList = new ArrayList<>();

        merchantsList = merchants1.getUpdatedMerchants();

        for (Merchants m : merchantsList) {
            MerchantUpdateSync merchantSync = new MerchantUpdateSync(this);
            merchantSync.updateMerchants(epf, m.getMerchantId(), m.getMerchantName(), m.getAddress(), m.getTelephone(), m.getMcity(), m.getBankAccId(), m.getBank(), m.getNic(),m.getBankAccName());
        }
    }


    @Override
    public void getMerchantSyncSuccess(String update, String merchantId) {
        if (update.equals("1")) {
            new Merchants().updateIsSynced(merchantId);
        }
    }

    @Override
    public void getMerchantSynceFaile(String message) {

    }

    @Override
    public void onClickSingleItem(SingleItemModel selectedItem, String btnType, int position) {
        switch (btnType){
            case "1":
                mCity.setText(selectedItem.itemText);
                break;

            case "2":
                mBank.setText(selectedItem.itemKey + "-" + selectedItem.itemText);
                break;
        }

    }

    @Override
    public void onClickSelectOk(String selectedItemID) {

    }


    @Override
    public void updateMerchantsListSuccess(String update, String merchantId) {
        if (update.equals("1")) {
            new Merchants().updateIsSynced(merchantId);
        }
    }

    @Override
    public void updateerchantsListFaile(String message) {

    }

    public void merchantUpdate(int i) {
        switch (i){
            case 1:
                checkFieldsValidation();
                break;

            case 2:
                checkforUpdate();
                break;
        }
    }
}
