package com.uow.snazzikiel.prepareo;

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


public class notifications extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "notifications-";
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

    /*
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

        createNotificationChannel();
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
        //notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        //popupMethod(noteItem);
    }

    /*
        Function:   popupMethod
        ---------------------------------------
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        assignItem:     (goalsData)Object Information retrieved from class.

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

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(freq)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {

                    notificationData newNote = new notificationData(name, freq, dateStart, dateEnd, msg);
                    notifyUser("Notification! " + name, msg);
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
        conLayout = (ConstraintLayout) findViewById(R.id.subjects_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.notification_popup, null);

        popUp = new PopupWindow(container, (int) (width * 0.80), (int) (height * 0.45), true);
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

                    setNotification(true,true, dateEnd);

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
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /*
        Function:   createNotificationChannel
        ---------------------------------------
        Create the notification channel to start up push notifications.
        Each channel must have a different ID.
        Main Function used to send push notification
    */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "desc";
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel channel = new NotificationChannel("1234", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*
        Function:   NotifyUser
        ---------------------------------------
        Used to create a notification once the notification channel has been used. Call the channelID created
        and notification will appear with the details below.
    */
    public void notifyUser(String title, String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1234")
                .setSmallIcon(R.drawable.heart)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, mBuilder.build());
    }

    /*
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

    /*
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.subjects_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.subjects_changeDetails:
                popupMethod(rowItems.get(index));
                return true;
            case R.id.subjects_deleteSubject:
                deleteNotificationItem(index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*
        Function:   setNotification
        ---------------------------------------
        Setup a delayed notification by Date
    */
    private void setNotification(boolean isNotification, boolean isRepeat, String strDate) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        //Change date entered to ints
        //Date df = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE,20);


        myIntent = new Intent(notifications.this,notificationAdapter.AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        if(!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }





}
