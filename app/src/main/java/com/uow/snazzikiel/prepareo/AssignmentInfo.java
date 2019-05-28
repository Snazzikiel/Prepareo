package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentInfo extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "assignmentInfo";
    List<assignmentsData> rowItems = new ArrayList<assignmentsData>();
    String subjectCode;
    int itemPosition;

    String subjectName;
    String assignmentName;

    TextView etTitle;
    TextView etWeight;
    TextView etTotal;
    TextView etDueDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_assignment_info);
        //Set title with items passed

        Bundle extras = getIntent().getExtras();
        subjectCode = extras.getString("subjectCode");
        assignmentName = extras.getString("assignmentName");

        // sItemPosition = extras.getString("assignmentPosition");
        itemPosition = extras.getInt("assignmentPosition");

        Log.i(TAG, String.valueOf(itemPosition));
        //Intent thisIntent = getIntent();

        loadData();

        //subjectCode = thisIntent.getStringExtra("subjectCode");
        //assignmentName = thisIntent.getStringExtra("assignmentName");
        //String sItemPosition = thisIntent.getStringExtra("assignmentPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectCode + " (" + assignmentName + ")");
        Log.i(TAG, String.valueOf(rowItems.size()));

        etTitle = (TextView) findViewById(R.id.tv_assdet_title1);
        etWeight = (TextView) findViewById(R.id.tv_assdet_weight2);
        etTotal = (TextView) findViewById(R.id.tv_assdet_totalMark2);
        etDueDate = (TextView) findViewById(R.id.tv_assdet_dueDate);

        etTitle.setText(rowItems.get(itemPosition).getAssignmentName());
        etWeight.setText(rowItems.get(itemPosition).getAssignmentWeight());
        etTotal.setText(rowItems.get(itemPosition).getAssignmentTotalMark());
        etDueDate.setText(rowItems.get(itemPosition).getAssignmentDueDate());


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }


    public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("assignmentData" + subjectCode, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.assignments_savedata) + subjectCode), null);
        Type type = new TypeToken<ArrayList<assignmentsData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }
}
