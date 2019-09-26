package com.example.user.lankabellapps.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.activities.ViewCustomerActivity;
import com.example.user.lankabellapps.adapters.CustomerSearchAdapter;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.ItemClickSupport;
import com.example.user.lankabellapps.helper.ManualSync;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.StringLikes;
import com.example.user.lankabellapps.models.Account;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.MainMenuObject;
import com.example.user.lankabellapps.popups.AddNewVisitPopup;
import com.example.user.lankabellapps.popups.CustomerOptionsPopup;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


public class CustomerSearchFragment extends Fragment implements CustomerOptionsPopup.SingleItemPopupItemClickListener, AddNewVisitPopup.SingleItemPopupItemClickListener {
    // TODO: Rename parameter arguments, choose names that match

    TextView mTilte, mAvaialveNo, mNewApps, mNoResults;
    RecyclerView mRecycleView;
    ImageButton mMainOptions;
    RelativeLayout mSearchByLocation;
    LinearLayout mSearchLocation;
    ProgressBar mProgressBar;


    private Button mSave, mClear;
    private EditText mName, mNicBr, mAccount;

    List<Customers> filteredList;

    ColoredSnackbar coloredSnackbar;

    int searchByLocation = 1;

    View view;
    Customers currentCus;

    DrawerLayout drawer;

    FloatingActionMenu fab;

    com.github.clans.fab.FloatingActionButton fabAddNewVisit;


    public CustomerSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_navigations, container, false);


        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_customer_search);
        mMainOptions = (ImageButton) view.findViewById(R.id.ib_main_options);
        mAvaialveNo = (TextView) view.findViewById(R.id.tv_no_of_apps);
        mNewApps = (TextView) view.findViewById(R.id.tv_new_apps);
        mSearchByLocation = (RelativeLayout) view.findViewById(R.id.rl_search_by_location);
        mSearchLocation = (LinearLayout) view.findViewById(R.id.ll_search_location);
        mNoResults = (TextView) view.findViewById(R.id.tv_no_search_result);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_downloadingProgress);


        mSave = (Button) view.findViewById(R.id.btn_search);
        mClear = (Button) view.findViewById(R.id.btn_clear);

        mName = (EditText) view.findViewById(R.id.etv_name);
        mNicBr = (EditText) view.findViewById(R.id.etv_nic_no);
        mAccount = (EditText) view.findViewById(R.id.etv_acc_no);

        fab = (FloatingActionMenu) view.findViewById(R.id.menu1);
        fabAddNewVisit = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.new_visit);


        //setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);


        init();
        buttonClick();

        syncConfigClickListner();

        return view;
    }

    private void buttonClick() {
        mSearchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view1 = (ImageView) view.findViewById(R.id.iv_search_location_arrow);
                switch (searchByLocation) {
                    case 1:
                        mSearchLocation.setVisibility(View.VISIBLE);
                        mSearchByLocation.setBackgroundResource(R.drawable.search_item_selected);
                        searchByLocation = 2;
                        view1.animate().rotation(90).start();
                        break;

                    case 2:
                        mSearchLocation.setVisibility(View.GONE);
                        mSearchByLocation.setBackgroundResource(R.drawable.serach_item_not_selected);
                        searchByLocation = 1;
                        view1.animate().rotation(270).start();
                        break;
                }
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName.setText("");
                mNicBr.setText("");
                mAccount.setText("");
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                loadTable();
            }
        });

        fabAddNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                AddNewVisitPopup addNewVisit = new AddNewVisitPopup(getActivity(), CustomerSearchFragment.this, layoutInflater, "");
                addNewVisit.makeDialog("", null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mNewApps.setText("New Notifications");

        new CheckInternet().execute("");
        loadTable();

    }

    private void init() {
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hideMenu(true);
                else if (dy < 0)
                    fab.showMenu(true);
            }
        });
    }

    public void loadTable() {

        Customers customers = new Customers();
        List<Customers> list = new ArrayList();
        list = customers.getAllData();
        filteredList = new ArrayList();
        filteredList = filterTheList(list);

        if (mAccount.getText().toString().isEmpty() && mNicBr.getText().toString().isEmpty() && mName.getText().toString().isEmpty()) {
            //mNoResults.setVisibility(View.VISIBLE);
            //mRecycleView.setVisibility(View.GONE);

            CustomerSearchAdapter customerSearchAdapter = new CustomerSearchAdapter(filteredList, getActivity(), Constants.CUSTOMER_FRAGMENT);
            mRecycleView.setAdapter(customerSearchAdapter);


        } else {

            if (filteredList.isEmpty()) {

                mNoResults.setVisibility(View.VISIBLE);
                mRecycleView.setVisibility(View.GONE);
            } else {
                mNoResults.setVisibility(View.GONE);
                mRecycleView.setVisibility(View.VISIBLE);
            }
            CustomerSearchAdapter customerSearchAdapter = new CustomerSearchAdapter(filteredList, getActivity(), Constants.CUSTOMER_FRAGMENT);
            mRecycleView.setAdapter(customerSearchAdapter);
        }


    }


    private List<Customers> filterTheList(List<Customers> list) {
        List<Customers> fileredList = new ArrayList<>();
        List<Customers> fileredList1 = new ArrayList<>();
        List<Customers> fileredList2 = new ArrayList<>();
        List<Customers> fileredList3 = new ArrayList<>();


        for (Customers customers : list) {

            int x1, x3 = 0, x4;
            x1 = !mName.getText().toString().equals("") ? (StringLikes.like(customers.getNamem(), mName.getText().toString()) ? 1 : 0) : 1;
            //x2 = !mContact.getText().toString().equals("") ? (StringLikes.like(customers.getContactNo(), mContact.getText().toString()) ? 1 : 0) : 1;

            if (mAccount.getText().toString().trim().equals("")) {
                x3 = 1;
            } else {
                Account account = new Account();
                List<Account> accountList = new ArrayList<>();

                accountList = account.getAccountsByCompanyCode(customers.getCompanyCode());

                for (Account ac : accountList) {
                    if (StringLikes.like(ac.getAccountCode(), mAccount.getText().toString())) {
                        x3 = 1;
                    }
                }
            }

            // x3 = !mAccountNo.getText().toString().equals("") ? (StringLikes.like(customers.getAccountNo(), mAccountNo.getText().toString()) ? 1 : 0) : 1;


            x4 = !mNicBr.getText().toString().equals("") ? (StringLikes.like(customers.getCompanyCode(), mNicBr.getText().toString()) ? 1 : 0) : 1;
            // x5 = !mProvince.getText().toString().equals("Province") ? (StringLikes.like(customers.getProvince(), mProvince.getText().toString()) ? 1 : 0) : 1;
            // x6 = !mDistrict.getText().toString().equals("District") ? (StringLikes.like(customers.getDistrict(), mDistrict.getText().toString()) ? 1 : 0) : 1;
            // x7 = !mCity.getText().toString().equals("City") ? (StringLikes.like(customers.getCity(), mCity.getText().toString()) ? 1 : 0) : 1;

            if (x1 == 1 && x3 == 1 && x4 == 1) {
                fileredList.add(customers);
            }

        }
        return fileredList;
    }


    private void syncConfigClickListner() {
        ItemClickSupport.addTo(mRecycleView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                currentCus = filteredList.get(position);
                dialogResult(1);
            }
        });

        ItemClickSupport.addTo(mRecycleView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                CustomerOptionsPopup customerOptionsPopup = new CustomerOptionsPopup(getActivity(), CustomerSearchFragment.this, layoutInflater, "");
                customerOptionsPopup.makeDialog("", filteredList.get(position));

                return true;
            }
        });
    }

    public void dialogResult(int from) {
        switch (from) {
            case 1:
                Intent intent = new Intent(getActivity(), ViewCustomerActivity.class);

                Bundle bundle1 = new Bundle();
                bundle1.putString("id", currentCus.getCompanyCode());
                intent.putExtras(bundle1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    @Override
    public void onClickSingleItem(int selectedOption, Customers selectedCus) {

        switch (selectedOption) {
            case 1:

                final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                AddNewVisitPopup addNewVisit = new AddNewVisitPopup(getActivity(), CustomerSearchFragment.this, layoutInflater, "");
                addNewVisit.makeDialog("", selectedCus);

                break;
        }

    }

    @Override
    public void onClickOk(Customers selectedCus) {

    }

    private class CheckInternet extends AsyncTask<String, Void, String> {

        boolean check = false;

        @Override
        protected String doInBackground(String... params) {

            check = new NetworkCheck().isInternetAvailable();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            //mPleaseWaitDialog.dismiss();
            runService(check);
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mPleaseWaitDialog.show();
        }

    }

    public void runService(boolean check) {
        if (check) {
            ManualSync manualSync = new ManualSync();
            manualSync.syncAll();
        } else {
            System.out.println("No Internet Connection");
            //((MainActivity) getActivity()).snakBarCommon("Check Internet Connection", new ColoredSnackbar(getActivity()).TYPE_ERROR, 1500);
        }
    }
    //     });

    // }


}
