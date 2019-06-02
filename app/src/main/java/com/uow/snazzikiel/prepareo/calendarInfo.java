package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class calendarInfo extends AppCompatActivity {
    private static final String TAG = "calendarInfo";

    String calendarDate;
    FloatingActionButton addNote;

    List<owlData> allActivities;
    List<owlData> dayActivities;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();
        calendarDate = thisIntent.getStringExtra("dateSelected");
        setTitle("Activities - " + calendarDate);

        allActivities = new ArrayList<owlData>();
        dayActivities = new ArrayList<owlData>();
        myList = (ListView) findViewById(R.id.main_list);//list for notifications
        

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notifications);

        for(owlData tmp : owlData.owlInfo){
            String user = tmp.getUserName();
            Long i = tmp.getMapTime();
            String key = tmp.getMapKey();
            allActivities.add(new owlData(user, key, i));
        }
        saveData();
        addNote = (FloatingActionButton) findViewById(R.id.addNotification);//button for add notifications
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getApplicationContext(), time.class);
                myIntent.putExtra("activityDate", calendarDate);
            }
        });
    }

    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("activityData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allActivities);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }

    /*public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.calendar_savedata)), null);
        Type type = new TypeToken<ArrayList<calendarData>>() {}.getType();

        calItems = gson.fromJson(json, type);

        if (calItems == null) {
            calItems = new ArrayList<>();
        }
    }*/

    public void createActivities(owlData allActivities) {

        Log.i(TAG, "addActivities");
        if (allActivities.getMapKey() == null || allActivities.getUserName() == "" ||
                allActivities.getMapTime() == null){
            return;
        }

        dayActivities.add(allActivities);
        
        calendarInfoAdapter adapter = new calendarInfoAdapter(this, dayActivities) {

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
        saveData();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
