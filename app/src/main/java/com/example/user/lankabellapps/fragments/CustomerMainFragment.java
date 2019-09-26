package com.example.user.lankabellapps.fragments;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lankabellapps.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerMainFragment extends Fragment {


    DrawerLayout drawer;
    ImageView mMenu;
    TextView mTilte;

    ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_customer_main, container, false);

        mTilte = (TextView) view.findViewById(R.id.tv_title);
        mMenu = (ImageView) view.findViewById(R.id.iv_menu);

        mMenu.setVisibility(View.VISIBLE);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START, true);
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.background_color));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub


                switch (position) {
                    case 0:
                        //setTitle("Select a Merchant");
                        break;
                    case 1:
                        //setTitle("Select a Customer");

                        //getItem(position).refresh();
                        break;
                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                // TODO Auto-generated method stub


            }


        });
        //mRecyclerView.setLayoutManager(mLayoutManager);
//        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        init();

        return view;
    }


    private void init() {
        mTilte.setText("Customers");
    }

    private void setupViewPager(ViewPager viewPager) {

         adapter = new ViewPagerAdapter(getChildFragmentManager());
//        adapter.mFragmentList.clear();
//        adapter.mFragmentTitleList.clear();

        adapter.addFragment(new CustomerSearchFragment(), "Search");
        adapter.addFragment(new VisitLogFragment(), "Log");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}