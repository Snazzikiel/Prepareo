package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Adam
 ***********************************************/
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
    Class:   notificationForm
    ---------------------------------------
    Used to create or edit new notifications for overall or individual subjects.
    Changed from popup box due to Date and Time picker pop-up fields are better to be entered this way
    for formatting.
 */
public class notificationForm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "notificationForm";
    ArrayList<notificationData> rowItems;
    Button btnClose;
    Button btnSave;
    int index;
    String sIndex;
    String subjectCode;

    DatePickerDialog.OnDateSetListener mStartDate;
    DatePickerDialog.OnDateSetListener mEndDate;
    EditText etName;
    TextView etTime;
    TextView etStart;
    TextView etEnd;
    EditText etMsg;
    Intent thisIntent;

    /**
         Function:   onCreate
         ---------------------------------------
         Default function to create the context and instance for Android screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.notification_popup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);

        thisIntent = getIntent();
        loadData();
        if (thisIntent.hasExtra("subjectCode")) {
            subjectCode = thisIntent.getStringExtra("subjectCode");
        }


        etName = (EditText)findViewById(R.id.notification_Name);
        etTime = (TextView)findViewById(R.id.notification_frequency);
        etStart = (TextView)findViewById(R.id.notification_startDate);
        etEnd = (TextView)findViewById(R.id.notification_endDate);
        etMsg = (EditText)findViewById(R.id.notification_msg);


        if (thisIntent.hasExtra("notificationPos")){
            index = thisIntent.getIntExtra("notificationPos", 0);
            setTitle("Edit Notification");
            etName.setText(rowItems.get(index).getName());
            etTime.setText(rowItems.get(index).getTime());
            etStart.setText(rowItems.get(index).getStartDate());
            etEnd.setText(rowItems.get(index).getEndDate());
            etMsg.setText(rowItems.get(index).getPersonalMsg());
        } else {
            setTitle("Create Notification");
        }
        
        etTime.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new timepick();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        
        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        notificationForm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mStartDate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "-" + month + "-" + year);

                String date = year + "-" + month + "-" + day;
                etStart.setText(date);
            }
        };

        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        notificationForm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mEndDate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "-" + month + "-" + year);

                String date = year + "-" + month + "-" + day;
                etEnd.setText(date);
            }
        };


        btnSave = (Button) findViewById(R.id.subjects_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String time = etTime.getText().toString().trim();
                String dateStart = etStart.getText().toString().trim();
                String dateEnd = etEnd.getText().toString().trim();
                String msg = etMsg.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(time) || TextUtils.isEmpty(dateStart)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {
                    createNotificationChannel(name, msg);

                    if (thisIntent.hasExtra("notificationPos")) {
                        rowItems.get(index).setName(name);
                        rowItems.get(index).setStartDate(dateStart);
                        rowItems.get(index).setEndDate(dateEnd);
                        rowItems.get(index).setPersonalMsg(msg);
                        rowItems.get(index).setTime(time);
                    } else {
                        notificationData newNote = new notificationData(name, time, dateStart, dateEnd, msg);

                        //addNotificationItem(newNote);
                        rowItems.add(newNote);
                    }
                    notifyUser("Prepareo: " + name, msg);
                    saveData();
                    if (thisIntent.hasExtra("subjectCode")) {
                        Intent myIntent = new Intent(getApplicationContext(), subjectNotifications.class);
                        myIntent.putExtra("subjectCode", subjectCode);
                        startActivityForResult(myIntent, 0);
                    } else {
                        Intent myIntent = new Intent(getApplicationContext(), notifications.class);
                        startActivityForResult(myIntent, 0);
                    }
                }
            }
        });
    }

    /**
     Function:   onTimeSet
     ---------------------------------------
     Popup clock to select time for notification and place in to textview

     @param view    get the current view
     @param hour    hour selected
     @param minute  minute selected
     */
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute)
    {
        String hh = String.valueOf(hour);
        String mm = String.valueOf(minute);

        if (hour < 10)
        {
            hh = "0" + hh;
        }
        if (minute < 10)
        {
            mm = "0" + mm;
        }
        String addStartTime = (hh + ":" + mm + ":00");
        etTime.setText(addStartTime);
    }


    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    /**
        Function:   onItemClick
        ---------------------------------------
        Default function for action when item is pressed

        parent:     Parent variable to include adapter view
        view:       Current activity view
        position:   Position of item pressed by user
    */
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(getApplicationContext(), "Hold item for menu",
                Toast.LENGTH_SHORT).show();
    }


    /**
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "notificationData" SharedPreference to access.
    */
    public void saveData() {
        if (thisIntent.hasExtra("subjectCode")) {
            subjectCode = thisIntent.getStringExtra("subjectCode");
            SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(rowItems);
            String tmp = getString(R.string.notification_savedata) + subjectCode;
            editor.putString(tmp, json);
            editor.apply();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(rowItems);
            editor.putString(getString(R.string.notification_savedata), json);
            editor.apply();
        }
    }

    /**
        Function:   loadData
        ---------------------------------------
        Used to retrieve the object to the local android device.
        Use a saveData function to call "notificationData" SharedPreference to overwrite.
    */
    public void loadData( ) {
        if (thisIntent.hasExtra("subjectCode")) {
            subjectCode = thisIntent.getStringExtra("subjectCode");
            SharedPreferences sharedPreferences = getSharedPreferences("notificationData" + subjectCode, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString((getString(R.string.notification_savedata) + subjectCode), null);
            Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
            rowItems = gson.fromJson(json, type);
        } else {

            SharedPreferences sharedPreferences = getSharedPreferences("notificationData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(getString(R.string.notification_savedata), null);
            Type type = new TypeToken<ArrayList<notificationData>>() {
            }.getType();
            rowItems = gson.fromJson(json, type);
        }

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

    /**
        Function:   createNotificationChannel
        ---------------------------------------
        Create the notification channel to start up push notifications.
        Each notification(channel) must have a different ID. Fixed for Tradeshow but need to alter
        in future.
        Main Function used to send push notification

        @param title    The title of the notification
        @param message  The message of the notification
    */
    private void createNotificationChannel(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = title;
            String description = message;
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel channel = new NotificationChannel("1234", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
        Function:   NotifyUser
        ---------------------------------------
        Used to create a notification once the notification channel has been used. Call the channelID created
        and notification will appear with the details below.

        @param title    Title of the notification
        @param message  The message of the notification
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

    /**
        Function:   setNotification
        ---------------------------------------
        Setup a delayed notification by Date -- Not used as instant notifications setup for demo

        @param isNotification   if it is a notification or not
        @param isRepeat         If the user has opted for a one off or repeat
        @param strDate          The date of the notification to be set
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


        myIntent = new Intent(notificationForm.this,notificationAdapter.AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        if(!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }


}
