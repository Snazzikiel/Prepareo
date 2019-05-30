package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "calendarCheck";
    CalendarView calView;
    TextView tvDate;

    List<assignmentsData> rowItems = new ArrayList<assignmentsData>();
    ViewGroup container;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);

        String dateToday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String dateToday1 = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String dateToday2 = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dateToday3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        Log.i(TAG, dateToday1 + " /// " + dateToday2 + " /// " + dateToday3 + " /// ");

        calView = (CalendarView) findViewById(R.id.calender_selection);
        tvDate = (TextView) findViewById(R.id.calendar_heading);
        tvDate.setText(dateToday);

        // Add Listener in calendar
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {

                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                tvDate.setText(Date);
            }
        });

        myList = (ListView) findViewById(R.id.calendar_main_list);

        //Add two test Subjects
        assignmentsData assignment = new assignmentsData("Assignment 1", "10%");
        createItems(assignment);

        assignment = new assignmentsData("Assignment 2", "10%");
        createItems(assignment);

        assignment = new assignmentsData("Assignment 3", "10%");
        createItems(assignment);
        assignment = new assignmentsData("Assignment 4", "10%");
        createItems(assignment);
        assignment = new assignmentsData("Assignment 5", "10%");
        createItems(assignment);
        assignment = new assignmentsData("Assignment 6", "10%");
        createItems(assignment);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
                //startActivityForResult(myIntent, 0);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String assignName = rowItems.get(position).getAssignmentName();
        Toast.makeText(getApplicationContext(), "" + assignName,
                Toast.LENGTH_SHORT).show();
    }

    public void createItems(assignmentsData assignment1) {

        Log.i(TAG, "addAssignment");
        rowItems.add(assignment1);


        assignmentsAdapter adapter = new assignmentsAdapter(this, rowItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}

