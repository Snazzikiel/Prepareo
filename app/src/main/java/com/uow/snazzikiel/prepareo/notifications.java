package com.uow.snazzikiel.prepareo;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class notifications extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "stateCheck";
    List<notificationData> rowItems = new ArrayList<notificationData>();

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addNote;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notifications);

        myList = (ListView) findViewById(R.id.main_list);

        //Add two test Notifications
        notificationData userNotifications = new notificationData("Example 1", "Daily",
                "19-08-18", "23-09-19", "TestMessage");
        addNotification(userNotifications);

        userNotifications = new notificationData("Example 2", "Daily",
                "19-08-18", "23-09-19", "TestMessage");
        addNotification(userNotifications);

        /*
        addNote = (FloatingActionButton) findViewById(R.id.addNotification);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(notifications.this, notification_popUp.class));
            }
        });*/

        addNote = (FloatingActionButton) findViewById(R.id.addNotification);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupMethod(null);
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                notificationData noteItem = (notificationData) parent.getItemAtPosition(position);
                popupMethod(noteItem);

            }
        });

        /*
        mylistview = (ListView) findViewById(R.id.main_list);
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
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(this);
        */

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String noteName = rowItems.get(position).getName();
        Toast.makeText(getApplicationContext(), "" + noteName,
                Toast.LENGTH_SHORT).show();
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

                String name = etName.getText().toString();
                String freq = etFreq.getText().toString();
                String dateStart = etStart.getText().toString();
                String dateEnd = etEnd.getText().toString();
                String msg = etMsg.getText().toString();

                notificationData newNote = new notificationData(name, freq, dateStart, dateEnd, msg);

                addNotification(newNote);
                popUp.dismiss();
            }
        });
    }

    public void addNotification(notificationData note1){

        Log.i(TAG, "addNotification");
        rowItems.add(note1);


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
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(this);
    }




    /*private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    */
}
