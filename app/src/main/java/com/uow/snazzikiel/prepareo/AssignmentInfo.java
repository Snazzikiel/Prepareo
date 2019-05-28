package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class AssignmentInfo extends AppCompatActivity {

    String subjectName;
    String assignmentName;

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

    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
