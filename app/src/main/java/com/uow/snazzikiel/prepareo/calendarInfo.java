package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David, Alec
 ***********************************************/


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
    Class:   calendarInfo
    ---------------------------------------

    Actioned when a date is selected in the Calendar. Displays a list of activities loaded for
    the selected date.
*/
public class calendarInfo extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "calendarInfo";

    String calendarDate;
    FloatingActionButton addNote;

    List<owlData> dayActivities;
    ListView myList;

    /**
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

        //load activities in to list which match day selected
        for(owlData tmp : owlData.owlInfo){
            String d = tmp.getDate();
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

        registerForContextMenu(myList);
    }

    /**
        Function: onItemClick
        ---------------------------------------
        Default function for when item is pressed

        @param parent   Parent view to get context of screen
        @param view     View of loaded item
        @param position position of item clicked
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        //popupMethod(noteItem);
    }

    /**
        Function:   saveData
        ---------------------------------------
        Used to store the activity list object to the local android device.
        Use a loadData function to call "activityData" SharedPreference to access.

        @return     void
    */
    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("activityData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dayActivities);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }

    /**
        Function:   createActivities
        ---------------------------------------
        Fills the activity list with activities loaded in owl data

        @param      activity     Current activity to be loaded to list
        @return     void
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

    /**
     * Function:    deleteActivityItems
     * ---------------------------------------
     * Used to delete any loaded activities. Run query to delete from OWL File in PAGE class
     * and delete from local storage
     *
     * @param   iPosition   The position of the activity item clicked
     * @return  void
     */
    public void deleteActivityItem(int iPosition){
        int tmpCounter = 0;
        int objLocation = 0;

        //look through owlData and find location of item clicked - delete the item.
        for(owlData tmp : owlData.owlInfo){
            if ( dayActivities.get(iPosition).equals(tmp)){
                objLocation = tmpCounter;
            }
            tmpCounter++;
        }

        page runQuery = new page();
        String dateFiltered = dayActivities.get(iPosition).getDate().replace("-", "");
        String activityInfo = dayActivities.get(iPosition).getMapKey() + ":" + dateFiltered;
        Log.i(TAG, activityInfo);
        //runQuery.deleteActivityFromList(dayActivities.get(iPosition).getUserName(), activityInfo);

        //delete from local data and reload list
        owlData.owlInfo.remove(objLocation);
        dayActivities.remove(iPosition);
        saveData();
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
    }

    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default required function to include a back button arrow on the top of the page

        @param  item        The back button
        @return boolean     Return true for action

    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), CalendarSelect.class);
        startActivityForResult(myIntent, 0);
        //finish();
        return true;
    }

    /**
        Function:   onCreateContextMenu
        ---------------------------------------
        Create menu object when user holds down on a list item
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.activity_menu, menu);
    }

    /**
        Function:   onContextItemSelected
        ---------------------------------------
        Call menu and action each option

        @param item     Menu taken from Menu in Res
    */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.activity_delete:
                //editItem(rowItems.get(index), index);
                deleteActivityItem(index);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
