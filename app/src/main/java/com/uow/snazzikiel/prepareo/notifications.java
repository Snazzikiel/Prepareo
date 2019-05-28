package com.uow.snazzikiel.prepareo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class notifications extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "notifications-";
    //List<notificationData> rowItems = new ArrayList<notificationData>();
    ArrayList<notificationData> rowItems;// = new ArrayList<notificationData>();
    notificationData p;

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addNote;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    private MenuItem menuItemSearch;
    private MenuItem menuItemDelete;

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


        myList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteNotificationItem(position);
                Toast.makeText(getApplicationContext(), "Item Deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
        popupMethod(noteItem);
    }

    private void showDeleteMenu(boolean show){
        menuItemDelete.setVisible(show);
        menuItemSearch.setVisible(!show);
    }

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

        if (noteItem != null){
            EditText etName = (EditText)container.findViewById(R.id.notification_Name);
            EditText etFreq = (EditText)container.findViewById(R.id.notification_frequency);
            EditText etStart = (EditText)container.findViewById(R.id.notification_startDate);
            EditText etEnd = (EditText)container.findViewById(R.id.notification_endDate);
            EditText etMsg = (EditText)container.findViewById(R.id.notification_msg);

            etName.setText(noteItem.getName());
            etFreq.setText(noteItem.getFrequency());
            etStart.setText(noteItem.getStartDate());
            etEnd.setText(noteItem.getEndDate());
            etMsg.setText(noteItem.getPersonalMsg());
        }

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

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(freq)){
                    notificationData newNote = new notificationData(name, freq, dateStart, dateEnd, msg);
                    addNotificationItem(newNote);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }
                popUp.dismiss();
            }
        });
    }

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

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

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

    public void notifyUser(String title, String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1234")
                .setSmallIcon(R.drawable.heart)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.notification_savedata), json);
        editor.apply();
    }

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



}
