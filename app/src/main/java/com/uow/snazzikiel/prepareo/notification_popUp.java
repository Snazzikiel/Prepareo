package com.uow.snazzikiel.prepareo;
/*
This class has been made obsolete.
Popup windows has been incorporated in to notifications.java



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class notification_popUp extends AppCompatActivity {

    private static final String TAG = "stateCheck";
    Button btnClose;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setTitle("Create Notification");
        setContentView(R.layout.notification_popup);

        btnClose = (Button) findViewById(R.id.notification_btn_close);
        btnSave = (Button) findViewById(R.id.notification_btn_save);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.8));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    notifications parentNotification = new notifications();

                    EditText etName = (EditText)findViewById(R.id.notification_Name);
                    EditText etFreq = (EditText)findViewById(R.id.notification_frequency);
                    EditText etStart = (EditText)findViewById(R.id.notification_startDate);
                    EditText etEnd = (EditText)findViewById(R.id.notification_endDate);
                    EditText etMsg = (EditText)findViewById(R.id.notification_msg);

                    String name = etName.getText().toString();
                    String freq = etFreq.getText().toString();
                    String dateStart = etStart.getText().toString();
                    String dateEnd = etEnd.getText().toString();
                    String msg = etMsg.getText().toString();

                    notificationData newNote = new notificationData(name, freq, dateStart, dateEnd, msg);

                    parentNotification.addNotification(newNote);
                    Log.i(TAG, "onAddition");
                }
        });
    }
}
*/