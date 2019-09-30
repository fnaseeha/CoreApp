package com.example.user.lankabellapps.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.SectionsPagerAdapter;
import com.example.user.lankabellapps.fragments.LeaveFragment;
import com.example.user.lankabellapps.fragments.ReportingFragment;
import com.example.user.lankabellapps.fragments.SubstituteFragment;

public class FragmentActivity extends AppCompatActivity {

    private static final String TAG = "FragmentActivity";

    private SectionsPagerAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SubstituteFragment(), "Substitute Apprtoval");
        adapter.addFragment(new LeaveFragment(), "Leave Request");
        adapter.addFragment(new ReportingFragment(), "Reporting Approval");
        viewPager.setAdapter(adapter);
    }
}