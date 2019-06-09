package com.uow.snazzikiel.prepareo;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class subjectNotifications extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "subjNotifications-";

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

    EditText dateChoice;
    private DatePickerDialog.OnDateSetListener dateListener;

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notifications");

        //Set title with items passed
        Intent thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");
        //String sItemPosition = thisIntent.getStringExtra("subjectPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectCode + " - Notifications");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notifications);

        myList = (ListView) findViewById(R.id.main_list);

        loadData();
        p = new notificationData(null, null, null, null, null);
        addNotificationItem(p);
        deleteNotificationItem(rowItems.size()-1);

        addNote = (FloatingActionButton) findViewById(R.id.addNotification);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupMethod(null);

            }
        });

        /*myList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteNotificationItem(position);
                Toast.makeText(getApplicationContext(), "Item Deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/


        registerForContextMenu(myList);
    }

    /*
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
        notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        popupMethod(noteItem);
    }

    /*
        Function:   addNotificationItem
        ---------------------------------------
        Add a notification item to the list

        iPosition:    Position of list item clicked
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

    /*
        Function:   popupMethod
        ---------------------------------------
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        noteItem:     (notificationData)Object Information retrieved from class.

        TO DO: Input data in to OWL file, create query and post data
    */
    public void popupMethod(notificationData noteItem){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.notification_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.notification_popup, null);

        popUp = new PopupWindow(container, (int)(width*0.80),(int)(height*0.80), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int)(width*0.10), (int)(height*0.10));

        container.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                popUp.dismiss();
                return true;
            }
        });

        //Make date selector
        EditText etStart = (EditText)container.findViewById(R.id.notification_startDate);
        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateToday = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                int year = Integer.parseInt(dateToday);
                dateToday = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                int month = Integer.parseInt(dateToday);
                dateToday = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                int day = Integer.parseInt(dateToday);


                DatePickerDialog dialog = new DatePickerDialog(
                        subjectNotifications.this,
                        android.R.style.Widget_Holo_ActionBar_Solid,
                        dateListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        btnClose = (Button) container.findViewById(R.id.subjects_btn_close);
        btnSave = (Button) container.findViewById(R.id.subjects_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText)container.findViewById(R.id.notification_Name);
                EditText etFreq = (EditText)container.findViewById(R.id.notification_frequency);
                EditText etStart = (EditText)container.findViewById(R.id.notification_startDate);
                EditText etEnd = (EditText)container.findViewById(R.id.notification_endDate);
                EditText etMsg = (EditText)container.findViewById(R.id.notification_msg);

                String name = etName.getText().toString().trim();
                String freq = etFreq.getText().toString().trim();
                String dateStart = etStart.getText().toString().trim();
                String dateEnd = etEnd.getText().toString().trim();
                String msg = etMsg.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateStart) ||
                        TextUtils.isEmpty(freq) || TextUtils.isEmpty(dateEnd) || TextUtils.isEmpty(msg)){
                            Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {

                    notificationData newNote = new notificationData(name, freq, dateStart, dateEnd, msg);
                    addNotificationItem(newNote);
                }
                popUp.dismiss();
            }
        });
    }

    /*
        Function:   editItem
        ---------------------------------------
        Method to bring a pop up for user to enter data. Fill items with data that has been
        previously entered. Re-save data with new items entered.

        assignItem:     (notificationData)Object Information retrieved from class
        itemPosition:   position of Item

        TO DO: Write query to update/input data in to OWL file
    */
    public void editItem(notificationData subjItem, final int itemPosition) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.notification_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.notification_popup, null);

        popUp = new PopupWindow(container, (int) (width * 0.80), (int) (height * 0.85), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.25));

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        EditText etName = (EditText)container.findViewById(R.id.notification_Name);
        EditText etFreq = (EditText)container.findViewById(R.id.notification_frequency);
        EditText etStart = (EditText)container.findViewById(R.id.notification_startDate);
        EditText etEnd = (EditText)container.findViewById(R.id.notification_endDate);
        EditText etMsg = (EditText)container.findViewById(R.id.notification_msg);

        etName.setText(subjItem.getName());
        etFreq.setText(subjItem.getFrequency());
        etStart.setText(subjItem.getStartDate());
        etEnd.setText(subjItem.getEndDate());
        etMsg.setText(subjItem.getPersonalMsg());

        btnClose = (Button) container.findViewById(R.id.subjects_btn_close);
        btnSave = (Button) container.findViewById(R.id.subjects_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        Log.i(TAG, "4");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etName = (EditText)container.findViewById(R.id.notification_Name);
                EditText etFreq = (EditText)container.findViewById(R.id.notification_frequency);
                EditText etStart = (EditText)container.findViewById(R.id.notification_startDate);
                EditText etEnd = (EditText)container.findViewById(R.id.notification_endDate);
                EditText etMsg = (EditText)container.findViewById(R.id.notification_msg);

                String name = etName.getText().toString().trim();
                String freq = etFreq.getText().toString().trim();
                String dateStart = etStart.getText().toString().trim();
                String dateEnd = etEnd.getText().toString().trim();
                String msg = etMsg.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dateStart) ||
                        TextUtils.isEmpty(freq) || TextUtils.isEmpty(dateEnd) || TextUtils.isEmpty(msg)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {
                    rowItems.get(itemPosition).setEndDate(dateEnd);
                    rowItems.get(itemPosition).setName(name);
                    rowItems.get(itemPosition).setFrequency(freq);
                    rowItems.get(itemPosition).setPersonalMsg(msg);
                    rowItems.get(itemPosition).setStartDate(dateStart);
                    saveData();
                    loadData();
                    notificationData test = new notificationData("s", "s" , "s" +
                            "s","s","s");
                    addNotificationItem(test);
                    deleteNotificationItem(rowItems.size()-1);
                }

                popUp.dismiss();
            }
        });
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

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    /*
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "notificationData + <subjectCode>" SharedPreference to access.
    */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        String tmp = getString(R.string.notification_savedata) + subjectCode;
        editor.putString(tmp, json);
        editor.apply();
    }

    /*
        Function:   loadData
        ---------------------------------------
        Used to retrieve the object to the local android device.
        Use a saveData function to call "notificationData + <subjectCode>" SharedPreference to overwrite.
    */
    public void loadData( ) {

        SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.notification_savedata) + subjectCode), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

    /*
        Function:   onCreateContextMenu
        ---------------------------------------
        Create menu object when user holds down on a list item
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.subjects_menu, menu);
    }

    /*
        Function:   onContextItemSelected
        ---------------------------------------
        Call menu and action each option

        MenuItem:       Menu taken from Menu in Res
    */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.subjects_changeDetails:
                editItem(rowItems.get(index), index);
                Toast.makeText(this, rowItems.get(index).getName(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subjects_deleteSubject:
                deleteNotificationItem(index);
                Toast.makeText(this, "Item has been deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }






}
