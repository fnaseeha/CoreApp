package com.example.user.lankabellapps.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.AppDetailsAdapter;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.models.TrackingData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileActivity extends AppCompatActivity implements AppDetailsAdapter.SingleItemClickListener{

    @Bind(R.id.tv_title)
    TextView mTitle;

    @Bind(R.id.iv_menu)
    ImageView mBack;

    @Bind(R.id.tv_myprofile_epf)
    TextView mEpf;

    @Bind(R.id.tv_myprofile_serverstatus)
    TextView mServer;

    @Bind(R.id.tv_myprofile_number_of_merchants)
    TextView mMerchants;

    @Bind(R.id.tv_myprofile_number_of_customers)
    TextView mCustomers;

    @Bind(R.id.tv_myprofile_locationpoints)
    TextView mLocationpoints;

    @Bind(R.id.rv_myprofile_app_details)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mTitle.setText("My Profile");
        mBack.setImageResource(R.drawable.back);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        setData();

    }

    private void setData() {
        TSRDetails tsrDetails = new TSRDetails();
        Merchants merchants = new Merchants();
        Customers customers = new Customers();

        try {
            mEpf.setText(tsrDetails.getAllData().get(0).EpfNo);

        } catch (Exception e) {
            System.out.println(e);
        }

        if (Constants.BaseUrlTOCoreApp.equals("119.235.1.88:8091/")) {//119.235.1.88:8091
            mServer.setText("Live");
        } else if (Constants.BaseUrlTOCoreApp.equals("119.235.1.59:4010/")) {//10.1.3.105:8091 //119.235.1.59:4010
            mServer.setText("Test");
        } else {
            mServer.setText("Unknown");
        }

        mMerchants.setText(merchants.getAllMerchants().size()+"");
        mCustomers.setText(customers.getAllData().size()+"");

        AvailableApps availableApps = new AvailableApps();

        AppDetailsAdapter appDetailsAdapter = new AppDetailsAdapter(this, availableApps.getAllApps(),this, "");
        mRecyclerView.setAdapter(appDetailsAdapter);

        TrackingData trackingData = new TrackingData();
        mLocationpoints.setText(trackingData.getAllLocationData().size()+"");

    }

    @OnClick(R.id.iv_menu)
    public void OnClickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onClickSingleItem(AvailableApps selectedItem, int position) {

    }

    @Override
    public void onCheckChange(AvailableApps checkedChanged, int position, String itemKey) {

    }
}
