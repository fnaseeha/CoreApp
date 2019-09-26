package com.example.user.lankabellapps.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.AddMerchantsActivity;
import com.example.user.lankabellapps.activities.ViewCustomerActivity;
import com.example.user.lankabellapps.adapters.CusAdapter;
import com.example.user.lankabellapps.helper.FilterSingleItem;
import com.example.user.lankabellapps.helper.StringEmptyCheck;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.popups.SingleItemListPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantsListFragment extends Fragment implements CusAdapter.SingleItemClickListener{


    TextView mNoMerchants, mTilte;

    EditText mSearchbar;

    FrameLayout mSearcheLinerLayout;

    //@Bind(R.id.rv_merchants)
    RecyclerView mRecyclerView;

    List<Merchants> merchantsList;

    CusAdapter cusAdapter;

    ImageView mMenu;

    View view;
    DrawerLayout drawer;

    ImageButton mClearSearchBar;

    FloatingActionButton fab;

    //SearchView.OnQueryTextListener listener;

    private OnFragmentInteractionListener mListener;

    public MerchantsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MerchantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchantsListFragment newInstance(String param1, String param2) {
        MerchantsListFragment fragment = new MerchantsListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ButterKnife.bind(getActivity());
        view = inflater.inflate(R.layout.fragment_merchant, container, false);

        mTilte = (TextView) view.findViewById(R.id.tv_title);
        mNoMerchants = (TextView) view.findViewById(R.id.tv_no_merchants);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_merchants);
        mSearchbar = (EditText) view.findViewById(R.id.etv_search);
        mSearcheLinerLayout = (FrameLayout) view.findViewById(R.id.ll_toolbar_search);
        mMenu = (ImageView) view.findViewById(R.id.iv_menu);
        mClearSearchBar = (ImageButton) view.findViewById(R.id.ibtn_searchbar_clear);

        mSearcheLinerLayout.setVisibility(View.VISIBLE);
        mTilte.setText("Merchants");

        mMenu.setVisibility(View.VISIBLE);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START, true);
            }
        });

        mClearSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchbar.setText("");
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.background_color));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        //setHasOptionsMenu(true);
        //getActivity().invalidateOptionsMenu();
//        mSearchbar = new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // newText is text entered by user to SearchView
//                filterItem(newText.trim());
//                //Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
//                return true;
//            }
//        };

        mSearchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println(s.toString());
                filterItem(s.toString().trim());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub

//                //Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
//                return true;
            }
        });


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMerchantsActivity.class);

                Bundle b = new Bundle();
                b.putInt("status",1);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });

        init();




        return view;


    }
    private SearchView mSearchView;
    private MenuItem searchMenuItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //return true;
    }

    private void setToAdapter(List<Merchants> list) {

        List<Merchants> currentList = new ArrayList<>();

        init();
        System.out.println(list.size());
        if (list.size() == 0) {
            mNoMerchants.setVisibility(View.VISIBLE);
            mNoMerchants.setText("No Items");
        }else{
            mNoMerchants.setVisibility(View.GONE);
        }
        for (Merchants singleItemModel : list) {
            currentList.add(singleItemModel);
        }

//        Collections.sort(currentList, new Comparator<Merchants>() {
//            @Override
//            public int compare(Merchants o1, Merchants o2) {
//                return o1.getMerchantName().trim().compareTo(o2.getMerchantName().trim());
//            }
//        });

       // Collections.sort(currentList, (o1, o2) -> o1.getMerchantName().compareTo(o2.getMerchantName()));

        //Collections.sort(currentList, String.CASE_INSENSITIVE_ORDER);

        Collections.sort(currentList, new Comparator<Merchants>() {
            public int compare(Merchants v1, Merchants v2) {
                return v1.getMerchantName().trim().toUpperCase().compareTo(v2.getMerchantName().trim().toUpperCase());
            }
        });

        cusAdapter = new CusAdapter(getActivity(), currentList, this, "");
        mRecyclerView.setAdapter(cusAdapter);


    }

    private void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                if (dy > 0 ||dy<0 && fab.isShown())
//                    fab.hide();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    fab.show();
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onResume() {
        super.onResume();
//        getActivity().setTitle("Select a Merchant");
        mSearchbar.setText("");

        Merchants merchants = new Merchants();
        merchantsList = new ArrayList<>();
        merchantsList = merchants.getAllMerchants();
        setToAdapter(merchantsList);

//        for(Merchants m : merchantsList){
//            System.out.println(m.getMerchantId());
//        }
    }

    @Override
    public void onClickSingleItem(Merchants selectedItem, int position) {
        Intent intent = new Intent(getActivity(), AddMerchantsActivity.class);

        Bundle b = new Bundle();
        b.putInt("status",2);
        b.putString("id", selectedItem.getMerchantId());
        intent.putExtras(b);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onCheckChange(Merchants checkedChanged, int position, String itemKey) {

    }

//    @Override
//    public void onTextChange(String textOnSearch) {
//
//        //System.out.println("test 123");
//    }

    public void searchString(String qury) {
        //
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


    private void filterItem(String search) {
        boolean isEmpty = new StringEmptyCheck().isNotNullNotEmptyNotWhiteSpaceOnly(search);
        mNoMerchants.setVisibility(View.GONE);
        mNoMerchants.setText("");
        if (!isEmpty) {
            if (merchantsList != null) {
                setToAdapter(merchantsList);
            }
        } else {
            System.out.println(merchantsList);
            if (merchantsList != null) {
                List<Merchants> SingleList = new FilterSingleItem().filterMerchants(merchantsList, search);
                //setToAdapter(SingleList);
                if (SingleList.size() > 0) {
                    setToAdapter(SingleList);
                } else {
                    if(cusAdapter != null) {
                        cusAdapter.clearDataSet();
                    }
                    mNoMerchants.setText("No Result for \"" + search + "\"");
                    mNoMerchants.setVisibility(View.VISIBLE);
                }
            } else {
                mNoMerchants.setText("Some Error...");
                mNoMerchants.setVisibility(View.VISIBLE);

            }
        }
    }


    public class CustomComparator implements Comparator<Merchants> {
        @Override
        public int compare(Merchants o1, Merchants o2) {
            return o1.getMerchantName().compareTo(o2.getMerchantName());
        }
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        //MenuItem mSearchMenuItem = menu.findItem(R.id.search);
//        //SearchView search = (SearchView) mSearchMenuItem.getActionView();
//
//        //SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        //getActivity().getMenuInflater().inflate(R.menu.samplemanu, menu);
//
//        //MenuInflater inflater = getActivity().getMenuInflater();
//
//        //inflater.inflate(R.menu.settings, menu);
//
//
//
//
//
//        //Menu menu1 = menu;
//
////        for (int i = 0; i < menu1.size(); i++) {
////            if (menu1.getItem(i).getItemId() != R.id.search) {
////                menu1.getItem(i).setVisible(false);
////            }
////        }
//
//        //SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        //SearchView search;
//
//        //search = (SearchView) menu1.findItem(R.id.search).getActionView();
//
//            MenuItem mSearchMenuItem = menu.findItem(R.id.search);
//            SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
//
//
//        //search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                filterItem(s.trim());
//                System.out.println("sa");
//                return false;
//            }
//        });
//
//    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        // Implementing ActionBar Search inside a fragment
//        MenuItem item = menu.add("Search");
//        item.setIcon(R.drawable.search); // sets icon
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        SearchView sv = new SearchView(getActivity());
//
//        // modifying the text inside edittext component
////        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
////        TextView textView = (TextView) sv.findViewById(id);
////        textView.setHint("Search location...");
////        textView.setHintTextColor(getResources().getColor(R.color.button_color_0));
////        textView.setTextColor(getResources().getColor(R.color.colorAccent));
//
//        // implementing the listener
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                if (s.length() < 4) {
//                    Toast.makeText(getActivity(),
//                            "Your search query must not be less than 3 characters",
//                            Toast.LENGTH_LONG).show();
//                    return true;
//                } else {
//                    filterItem(s.trim());
//                    return false;
//                }
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });
//        item.setActionView(sv);
//    }



}



