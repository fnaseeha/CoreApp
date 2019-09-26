package com.example.user.lankabellapps.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.MerchantsCities;
import com.example.user.lankabellapps.models.SingleItemModel;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.popups.SingleItemSearchAndSelectPopup;
import com.example.user.lankabellapps.services.sync.MerchantSync;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MerchantsAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MerchantsAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MerchantsAddFragment extends Fragment implements SingleItemSearchAndSelectPopup.SingleItemPopupItemClickListener, MerchantSync.MerchantSyncEvents{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ImageView mMenu;
    DrawerLayout drawer;
    EditText mName, mAddress, mContactNo, mAccouyntNumber, mBank, mNic;
    TextView mCity;
    Button mClear, mAdd;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView mTilte;
    View view;

    public MerchantsAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MerchantsAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchantsAddFragment newInstance(String param1, String param2) {
        MerchantsAddFragment fragment = new MerchantsAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_merchants_add, container, false);

        mTilte = (TextView) view.findViewById(R.id.tv_title);
        mMenu = (ImageView) view.findViewById(R.id.iv_menu);

        mName = (EditText) view.findViewById(R.id.etvmName);
        mAddress = (EditText) view.findViewById(R.id.etvmAddress);
        mContactNo = (EditText) view.findViewById(R.id.etvmContactNo);
        mCity = (TextView) view.findViewById(R.id.tvmCity);
        mAccouyntNumber = (EditText) view.findViewById(R.id.etvmAccountNumber);
        mNic = (EditText) view.findViewById(R.id.etvmNic);
        mBank = (EditText) view.findViewById(R.id.etvmBank);
        FrameLayout mToolBarSearch = (FrameLayout) view.findViewById(R.id.ll_toolbar_search);

        mToolBarSearch.setVisibility(View.VISIBLE);

        mClear = (Button) view.findViewById(R.id.btnClear);
        mAdd = (Button) view.findViewById(R.id.btnAddMerchant);

        mMenu.setVisibility(View.VISIBLE);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START, true);
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();

            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldsValidation();
            }
        });

        mCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MerchantsCities merchantsCities = new MerchantsCities();
                List<MerchantsCities> merchantsCitiesList = new ArrayList<>();

                final List<SingleItemModel> list1 = new ArrayList<>();

                merchantsCitiesList = merchantsCities.getAllMerchantsCities();

                for(MerchantsCities m : merchantsCitiesList){
                    SingleItemModel s = new SingleItemModel();

                    s.itemText = m.getCity();
                    s.itemKey = m.getPostalCode();

                    list1.add(s);
                }

                final LayoutInflater layoutInflater1 = getActivity().getLayoutInflater();

                SingleItemSearchAndSelectPopup dialog1 = new SingleItemSearchAndSelectPopup(getActivity(), MerchantsAddFragment.this, layoutInflater1, "");
                dialog1.makeDialog("Select a City", true, list1, "district");
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.background_color));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        init();

        return view;
    }

    private void clearFields() {
        mName.setText("");
        mAddress.setText("");
        mContactNo.setText("");
        mCity.setText("");
        mAccouyntNumber.setText("");
        mNic.setText("");
        mAccouyntNumber.setText("");
        mBank.setText("");
        mNic.setText("");
    }

    private void checkFieldsValidation() {

        if (mName.getText().toString().trim().isEmpty() || mAddress.getText().toString().trim().isEmpty() || mContactNo.getText().toString().trim().isEmpty() || mAccouyntNumber.getText().toString().trim().isEmpty() || mBank.getText().toString().trim().isEmpty() || mNic.getText().toString().trim().isEmpty()) {
            ((MainActivity) getActivity()).snakBarCommon("Fill all the fields...", new ColoredSnackbar(getActivity()).TYPE_ERROR, 1000);

        } else {

            if (mCity.getText().toString().trim().isEmpty()) {
                ((MainActivity) getActivity()).snakBarCommon("Fill all the fields...", new ColoredSnackbar(getActivity()).TYPE_ERROR, 1000);
            } else {
                TSRDetails tsrDetails = new TSRDetails();
                List<TSRDetails> tsrDetailsList = new ArrayList<>();

                tsrDetailsList = tsrDetails.getAllData();
                int nextMerchantId = Integer.parseInt(tsrDetailsList.get(0).getNextMerchantNo()) + 1;
                String epf = tsrDetailsList.get(0).getEpfNo();
                Merchants merchants = new Merchants();
                merchants.setMerchantId(nextMerchantId + epf);
                merchants.setMerchantName(mName.getText().toString());
                merchants.setAddress(mAddress.getText().toString());
                merchants.setTelephone(mContactNo.getText().toString());
                merchants.setMcity(mCity.getText().toString());
                merchants.setBankAccId(mAccouyntNumber.getText().toString());
                merchants.setBank(mBank.getText().toString());
                merchants.setNic(mNic.getText().toString());
                merchants.setIsSynced("false");
                merchants.save();

                new TSRDetails().updateIsSynced(nextMerchantId + "");

                clearFields();

                Merchants merchants1 = new Merchants();
                List<Merchants> merchantsList = new ArrayList<>();

                merchantsList = merchants1.getAllNotSyncMerchants();

                for(Merchants m : merchantsList){
                    MerchantSync merchantSync = new MerchantSync(this);
                    merchantSync.setMerchants(epf, m.getMerchantId(), m.getMerchantName(), m.getAddress(), m.getTelephone(), m.getMcity(), m.getBankAccId(), m.getBank(), m.getNic(),m.getBankAccName());
                }

                ((MainActivity) getActivity()).snakBarCommon("Saved...", new ColoredSnackbar(getActivity()).TYPE_OK, 1000);
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void init() {
        mTilte.setText("Add Merchants");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClickSingleItem(SingleItemModel selectedItem, String btnType, int position) {
        mCity.setText(selectedItem.itemText);
    }

    @Override
    public void onClickSelectOk(String selectedItemID) {

    }

    @Override
    public void getMerchantSyncSuccess(String update, String merchantId) {
        if(update.equals("1")) {
            new Merchants().updateIsSynced(merchantId);
        }
    }

    @Override
    public void getMerchantSynceFaile(String message) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
