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


public class subjectNotifications extends notifications implements AdapterView.OnItemClickListener {
    private static final String TAG = "notifications-";

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

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
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
        SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.notification_savedata), json);
        editor.apply();
    }

    public void loadData( ) {

        SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.subjects_notifications_savedata) + subjectCode), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }



}
