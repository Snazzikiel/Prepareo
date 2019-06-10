package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

/*
    Class:   statistics
    ---------------------------------------
    Load up the statistics page
*/
public class statistics extends AppCompatActivity {

    private static final String TAG = "Statistics";

    private statisticsTabAdapter mStatisticsPageAdapter;

    private ViewPager mViewPager;

    /*
        Function: onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_statistics);


        Log.d(TAG, "statisticsCreate.");

        mStatisticsPageAdapter = new statisticsTabAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /*
        Function: setupViewPager
        ---------------------------------------
        Setup screen in to 3 fragments - Today, Week, Month
    */
    private void setupViewPager(ViewPager viewPager) {
        statisticsTabAdapter adapter = new statisticsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new statisticsTabOne(), "TODAY");
        adapter.addFragment(new statisticsTabTwo(), "WEEK");
        adapter.addFragment(new statisticsTabThree(), "MONTH");
        viewPager.setAdapter(adapter);
    }

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default required function to include a back button arrow on the top of the page
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


}
