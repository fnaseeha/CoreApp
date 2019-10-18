package com.example.user.lankabellapps.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.SectionsPagerAdapter;

public class LeaveMainFragment extends AppCompatActivity {

    private static final String TAG = "LeaveMainFragment";

    private SectionsPagerAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.d(TAG, "onCreate: Starting.");
        TextView title = findViewById(R.id.title);
        title.setText("LEAVE");
        mViewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Leave Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Substitute Approval(6)"));
        tabLayout.addTab(tabLayout.newTab().setText("Reporting Approval(5)"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.green));

        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        mViewPager.setAdapter(mSectionsPageAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }





}