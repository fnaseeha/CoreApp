package com.example.user.lankabellapps.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.HanUnitAdapter;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.SQLiteBackup;
import com.example.user.lankabellapps.models.UnitHand;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.GetHandInUnit;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class UnitInHandActivity extends AppCompatActivity implements GetHandInUnit.GetHandEvents {

    LinearLayout backgroundLayout;
    RecyclerView rv_units;
    Button btn_Cdma,btn_Lte,btn_sim;
    List<UnitHand> UnitList = new ArrayList<>();
    HanUnitAdapter hanUnitAdapter;
    ImageView mBack,tv_sync;
    TextView mTitle;
    TextView no_data;
    LinearLayout ll_hand_in_unit;
    JSONArray unitHandObj;
    ColoredSnackbar coloredSnackbar;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_in_hand);
        init();
       // syncdata();
        setDBdata();
        setRecycleView(UnitList);

        btn_Cdma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitList = new UnitHand().getSelectedUnitHand("CDMA");
                setRecycleView(UnitList);
                hanUnitAdapter.notifyDataSetChanged();

            }
        });
        btn_Lte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitList = new UnitHand().getSelectedUnitHand("LTE");
                setRecycleView(UnitList);
                hanUnitAdapter.notifyDataSetChanged();
            }
        });
        btn_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitList = new UnitHand().getSelectedUnitHand("SIM");
                setRecycleView(UnitList);
                hanUnitAdapter.notifyDataSetChanged();
            }
        });


    }

    private void syncdata() {
        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        downloadUnitHand();
    }

    private void downloadUnitHand() {
        GetHandInUnit getHandInUnit = new GetHandInUnit(this);
        List<UserProfile> userProfileList = new ArrayList<>();
        UserProfile userProfile = new UserProfile();

        userProfileList  = userProfile.getAllUsers();
        getHandInUnit.GetUnits(userProfileList.get(0).getUserName());
    }
    private void setRecycleView(List<UnitHand> unitList) {

         hanUnitAdapter = new HanUnitAdapter(this,unitList);
        rv_units.setAdapter(hanUnitAdapter);
    }

    private void setDBdata() {
        UnitHand unitHand  =  new UnitHand();
        int cdmas = unitHand.getUnitCount("CDMA");
        int ltes = unitHand.getUnitCount("LTE");
        int sim = unitHand.getUnitCount("SIM");
        btn_Cdma.setText("CDMA ("+cdmas+")");
        btn_Lte.setText("LTE ("+ltes+")");
        btn_sim.setText("SIM ("+sim+")");
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
    private void init() {
        UnitHand unitHand  =  new UnitHand();
        mProgressDialog = new ProgressDialog(UnitInHandActivity.this);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        rv_units = findViewById(R.id.rv_units);
        btn_Lte = findViewById(R.id.btn_Lte);
        btn_Cdma = findViewById(R.id.btn_Cdma);
        no_data = findViewById(R.id.no_data);
        ll_hand_in_unit = findViewById(R.id.ll_hand_in_unit);
        UnitList = unitHand.getAlUnitHand();
        mBack = findViewById(R.id.iv_menu);
        mBack.setImageResource(R.drawable.back);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("Unit In Hand");
        btn_sim = findViewById(R.id.btn_sim);
        tv_sync = findViewById(R.id.tv_sync);

        rv_units.setHasFixedSize(true);
        rv_units.setLayoutManager(new LinearLayoutManager(this));
        rv_units.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        coloredSnackbar = new ColoredSnackbar(this);

        if(unitHand.getAllUnitCount()==0){
            no_data.setVisibility(View.VISIBLE);
            rv_units.setVisibility(View.GONE);
            ll_hand_in_unit.setVisibility(View.GONE);
            no_data.setText("No Unit in Hand Found");
        }else{
            no_data.setVisibility(View.GONE);
            ll_hand_in_unit.setVisibility(View.VISIBLE);
            rv_units.setVisibility(View.VISIBLE);
        }



    }

    public void SyncUnit(View view) {

        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
//        try {
//            new SQLiteBackup(UnitInHandActivity.this).exportDB();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        downloadUnitHand();


    }

    private class UnitHandSave extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<unitHandObj.length();i++){

                try {
                    JSONObject row = unitHandObj.getJSONObject(i);

                    UnitHand unitHand = new UnitHand();
                    unitHand.setIssuedBy(row.getString("IssuedBy"));
                    unitHand.setIssuedDate(row.getString("IssuedDate"));
                    unitHand.setIssueNo(row.getString("IssueNo"));
                    unitHand.setItemCode(row.getString("ItemCode"));
                    unitHand.setSerialNo(row.getString("SerialNo"));
                    unitHand.setStatus(row.getString("Status"));
                    unitHand.setType(row.getString("Type"));

                    unitHand.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                    snakBarCommon("Server Error while saving Unit Hands "+e.getMessage(), new ColoredSnackbar(getApplicationContext()).TYPE_ERROR, 2000);
                }
                if(i==unitHandObj.length()){
                    snakBarCommon("Successfully saved Unit Hands ", new ColoredSnackbar(getApplicationContext()).TYPE_OK, 2000);
                }
            }

//            hanUnitAdapter = new HanUnitAdapter(getApplicationContext(),UnitList);
//            rv_units.setAdapter(hanUnitAdapter);
//           // setRecycleView(UnitList);
//            hanUnitAdapter.notifyDataSetChanged();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {

            UnitList = new UnitHand().getAlUnitHand();

            hanUnitAdapter = new HanUnitAdapter(UnitInHandActivity.this,UnitList);
            rv_units.setHasFixedSize(true);
            rv_units.setLayoutManager(new LinearLayoutManager(UnitInHandActivity.this));
            rv_units.setRecycledViewPool(new RecyclerView.RecycledViewPool());
            rv_units.setAdapter(hanUnitAdapter);

            hanUnitAdapter.notifyDataSetChanged();

        }
    }

    private void setToAdapter(List<UnitHand> unitList) {
        /*cusAdapter = new CusAdapter(getActivity(), currentList, this, "");
	mRecyclerView.setAdapter(cusAdapter);*/
        hanUnitAdapter = new HanUnitAdapter(getApplicationContext(),unitList);
        rv_units.setAdapter(hanUnitAdapter);
    }


    @Override
    public void GetHandSuccess(GetCommonResponseModel update) {

        mProgressDialog.dismiss();
        try {
            if(update.getId().equals("1")){
                unitHandObj = new JSONArray(update.getData());
                UnitHand unitHand = new UnitHand();
                unitHand.clearTable();

                new UnitHandSave().execute("");
                snakBarCommon("Successfully saved Unit Hands ", new ColoredSnackbar(getApplicationContext()).TYPE_OK, 2000);
//                UnitList = unitHand.getAlUnitHand();
//                hanUnitAdapter.notifyDataSetChanged();
              /*  try {

                    UnitList = unitHand.getAlUnitHand();
                    rv_units.setHasFixedSize(true);
                    rv_units.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_units.setRecycledViewPool(new RecyclerView.RecycledViewPool());
                    setToAdapter(UnitList);
                }catch (Exception e){
                    e.printStackTrace();
                }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
            snakBarCommon("Server Error while downloading Unit Hands "+e.getMessage(), new ColoredSnackbar(this).TYPE_ERROR, 2000);
        }
    }
    public void snakBarCommon(String message, String x, int l) {
        coloredSnackbar.showSnackBar(message, x, l);

        //bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
    }
    @Override
    public void GetHandError(String message) {
        mProgressDialog.dismiss();
        snakBarCommon("Error while downloading Unit Hands Timeout Error", new ColoredSnackbar(this).TYPE_ERROR, 2000);
        System.out.println("Error while downloading Unit Hands"+message);
    }
}
