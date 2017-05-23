package com.quickblox.push_sender_tools.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.ui.adapters.SectionsPagerAdapter;
import com.quickblox.push_sender_tools.utils.Consts;
import com.quickblox.push_sender_tools.utils.GooglePlayServicesHelper;

public class MessagesActivity extends BaseActivity{

    private final String TAG = getClass().getSimpleName();

    private GooglePlayServicesHelper googlePlayServicesHelper;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static void start(Context context, String message) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra(Consts.EXTRA_GCM_MESSAGE, message);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        googlePlayServicesHelper = new GooglePlayServicesHelper();
        googlePlayServicesHelper.checkPlayServicesAvailable(this);

        initUI();
    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.fragments_container);
        viewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}