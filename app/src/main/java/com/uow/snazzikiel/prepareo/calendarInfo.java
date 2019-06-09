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
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/*
    Class:   calendarInfo
    ---------------------------------------
    Adapter class to load list in to the calendar page.
    Create calendar in GridView, take events from OWL file and circle them if activity
    has been loaded.
*/
public class calendarInfo extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "calendarInfo";

    String calendarDate;
    FloatingActionButton addNote;

    List<owlData> allActivities;
    List<owlData> dayActivities;
    ListView myList;

    /*
        Function: onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen.

        Uses same layout as notification.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();
        calendarDate = thisIntent.getStringExtra("dateSelected");
        setTitle("Activities - " + calendarDate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notifications);

        dayActivities = new ArrayList<owlData>();
        myList = (ListView) findViewById(R.id.main_list);//list for notifications

        //load activities in to list
        for(owlData tmp : owlData.owlInfo){
            String user = tmp.getUserName();
            String d = tmp.getDate();
            Long i = tmp.getMapTime();
            String key = tmp.getMapKey();
            Log.i(TAG, d + " = " + calendarDate);
            if (calendarDate.equals(d)){
                createActivities(tmp);
                Log.i(TAG, d);
            }
        }

        saveData();
        addNote = (FloatingActionButton) findViewById(R.id.addNotification);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getApplicationContext(), time.class);
                myIntent.putExtra("activityDate", calendarDate);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    /*
        Function: onItemClick
        ---------------------------------------
        Default function for when item is pressed
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        //popupMethod(noteItem);
    }

    /*
        Function:   saveData
        ---------------------------------------
        Used to store the activity list object to the local android device.
        Use a loadData function to call "activityData" SharedPreference to access.
    */
    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("activityData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allActivities);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }

    /*
        Function:   createActivities
        ---------------------------------------
        Fills the activity list with activities loaded in owl data
    */
    public void createActivities(owlData activity) {

        Log.i(TAG, "addActivities");
        if (activity.getMapKey() == null || activity.getUserName() == "" ||
                activity.getMapTime() == null){
            return;
        }

        dayActivities.add(activity);
        
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

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default required function to include a back button arrow on the top of the page
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
