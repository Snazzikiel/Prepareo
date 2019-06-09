package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/*
    Class:   AssignmentInfo
    ---------------------------------------
    Display information of selected Assignment

    subjectName:        Login Username entered on LoginPage
    assignmentName:     password entered taken on LoginPage
*/
public class AssignmentInfo extends AppCompatActivity {

    String subjectName;
    String assignmentName;

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set title with items passed
        Intent thisIntent = getIntent();
        subjectName = thisIntent.getStringExtra("subjectCode");
        assignmentName = thisIntent.getStringExtra("assignmentName");
        //String sItemPosition = thisIntent.getStringExtra("subjectPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectName + " (" + assignmentName + ")");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_assignment_info);
    }

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
