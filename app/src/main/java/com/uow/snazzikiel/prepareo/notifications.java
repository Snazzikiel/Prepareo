package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
    Class:   notifications
    ---------------------------------------
    Class used to create, save and deploy notifications to users. This class has undergone a
    major method change and overhaul. Due to lack of due, variables and functions may be miscorrectly
    named or obsolete. Review functions carefully until further updated.

    TO DO:  correct functions and update variable names
 */

public class notifications extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "notificationMenu";
    //List<notificationData> rowItems = new ArrayList<notificationData>();
    ArrayList<notificationData> rowItems;// = new ArrayList<notificationData>();
    notificationData p;
    String subjectCode;

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addNote;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    int noteCount = 0;

    /**
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notifications);

        myList = (ListView) findViewById(R.id.main_list);

        loadData();
        //add fake data to reload list for user screen
        p = new notificationData(null, null, null, null, null);
        addNotificationItem(p);
        deleteNotificationItem(rowItems.size()-1);

        addNote = (FloatingActionButton) findViewById(R.id.addNotification);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupMethod(0);

            }
        });


        registerForContextMenu(myList);
    }

    /**
        Function:   onItemClick
        ---------------------------------------
        Default function for action when item is pressed

        parent:     Parent variable to include adapter view
        view:       Current activity view
        position:   Position of item pressed by user
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        //popupMethod(noteItem);
    }

    /**
        Function:   popupMethod
        ---------------------------------------
        **METHOD CHANGE**
     *  Method is to forward to notificationForm to enter/alter data of notifications.
     *  This will allow users to correctly enter date field rather than an incorrect pop-up text
     *  Refer back to old github versions prior to 10-06-19.
     *
     * **OLD METHOD**
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        //@param noteItem:     (notificationData)Object Information retrieved from class.

        TO DO: Input data in to OWL file, create query and post data
    */
    //public void popupMethod(notificationData noteItem){
    public void popupMethod(int index){
        Intent myIntent = new Intent(getApplicationContext(), notificationForm.class);
        startActivityForResult(myIntent, 0);
    }

    /**
        Function:   editItem
        ---------------------------------------
     **METHOD CHANGE**
     *  Method is to forward to notificationForm to enter/alter data of notifications.
     *  This will allow users to correctly enter date field rather than an incorrect pop-up text
     *  Refer back to old github versions prior to 10-06-19.
     *
     * @param itemPosition postion of Item to edit
     *
     *
     Method to bring a pop up for user to enter data. Fill items with data that has been
        previously entered. Re-save data with new items entered.

        assignItem:     (notificationData)Object Information retrieved from class
        itemPosition:   position of Item

        TO DO: Write query to update/input data in to OWL file
    */
    //public void editItem(notificationData subjItem, final int itemPosition) {
    public void editItem(final int itemPosition) {
        Log.i(TAG, String.valueOf(itemPosition));
        Log.i(TAG, rowItems.get(itemPosition).getName());
        Intent myIntent = new Intent(getApplicationContext(), notificationForm.class);
        myIntent.putExtra("notificationPos", itemPosition);
        startActivityForResult(myIntent, 0);
    }

    /*
        Function:   deleteNotificationItem
        ---------------------------------------
        Delete the notification item from the List

        iPosition:    Position of list item clicked
    */
    public void deleteNotificationItem(int iPosition){
        Log.i(TAG, "deleteNotification");
        rowItems.remove(iPosition);
        notificationAdapter adapter = new notificationAdapter(this, rowItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };
        saveData();
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this);
    }

    /**
     Function:   addNotificationItem
     ---------------------------------------
     Add a notification item to the list

     @param note1    the object stored locally into from notificationData
     */
    public void addNotificationItem(notificationData note1){

        Log.i(TAG, "addNotification");
        if (note1 != null){
            rowItems.add(note1);
        }

        notificationAdapter adapter = new notificationAdapter(this, rowItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };
        saveData();
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(this);
    }



    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    /**
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "notificationData" SharedPreference to access.
    */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.notification_savedata), json);
        editor.apply();
    }

    /**
        Function:   loadData
        ---------------------------------------
        Used to retrieve the object to the local android device.
        Use a saveData function to call "notificationData" SharedPreference to overwrite.
    */
    public void loadData( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.notification_savedata), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

    /**
     Function:   onCreateContextMenu
     ---------------------------------------
     Allow user to hold down on item to bring up small popup menu box
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.subjects_menu, menu);
    }

    /**
     Function:   onContextItemSelected
     ---------------------------------------
     Action whichever item is selected on the pop-up menu onCreateContextMenu created.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.subjects_changeDetails:
                //popupMethod(rowItems.get(index));
                editItem(index);
                return true;
            case R.id.subjects_deleteSubject:
                deleteNotificationItem(index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }







}
