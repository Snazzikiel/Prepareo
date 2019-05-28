package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "calendarCheck";
    CalendarView calView;
    TextView tvDate;

    List<subjectsData> dataSubjects;// = new ArrayList<>();
    List<assignmentsData> dataAssign;// = new ArrayList<>();
    List<notificationData> dataNote;// = new ArrayList<>();
    List<goalsData> dataGoals;// = new ArrayList<>();+

    ViewGroup container;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);

        List<subjectsData> dataSubjects = new ArrayList<>();
        List<assignmentsData> dataAssign = new ArrayList<>();
        List<notificationData> dataNote = new ArrayList<>();
        List<goalsData> dataGoals = new ArrayList<>();

        String dateToday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        calView = (CalendarView) findViewById(R.id.calender_selection);
        tvDate = (TextView) findViewById(R.id.calendar_heading);
        tvDate.setText(dateToday);

        // Add Listener in calendar
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                loadAllData();
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                tvDate.setText(Date);
            }
        });


        myList = (ListView) findViewById(R.id.calendar_main_list);

        //Add two test Subjects
        //assignmentsData assignment = new assignmentsData("Assignment 1", "10%");
        //createItems(assignment);



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

        /*String assignName = rowItems.get(position).getAssignmentName();
        Toast.makeText(getApplicationContext(), "" + assignName,
                Toast.LENGTH_SHORT).show();*/
    }

    public void createItems(assignmentsData assignment1) {

        Log.i(TAG, "addAssignment");
        dataAssign.add(assignment1);


        assignmentsAdapter adapter = new assignmentsAdapter(this, dataAssign) {
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

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        //String json = gson.toJson(rowItems);
        //editor.putString(getString(R.string.notification_savedata), json);
        editor.apply();
    }

    public void loadData(String prefName, String prefKey) {

        Type type;
        SharedPreferences sharedPreferences = getSharedPreferences(prefName, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(prefKey, null);

        if (prefName == "subjectsData"){
            type = new TypeToken<ArrayList<subjectsData>>() {}.getType();
            dataSubjects = gson.fromJson(json, type);
            if (dataSubjects == null) {
                dataSubjects = new ArrayList<>();
            }
        };

    }

    public void loadAllData(){

        loadData("subjectsData", getString(R.string.subjects_savedata));
        Log.i(TAG, dataSubjects.get(0).courseName);
        subjectsData x = new subjectsData(dataSubjects.get(0).courseName, dataSubjects.get(0).courseCode);
        createSubject(x);
    }


    public void createSubject(subjectsData subject1) {

        Log.i(TAG, "addSubject");
        if (subject1.getCourseName() == null || subject1.getCourseName() == "" ||
                subject1.getCourseCode() == null || subject1.getCourseCode() == "" ){
            return;
        }

        dataSubjects.add(subject1);

        subjectsAdapter adapter = new subjectsAdapter(this, dataSubjects) {
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



}

