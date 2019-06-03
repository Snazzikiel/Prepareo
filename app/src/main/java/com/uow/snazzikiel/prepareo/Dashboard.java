package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = "dashBoard";
    Button goNotification;
    Button goGoals;
    Button goSubject;
    Button goStat;
    Button goOWL;
    Button goCalendar;
    ImageView imgExit;
    String userName;
    String firstDay;
    String lastDay;
    public GregorianCalendar month, month2;
    private calendarAdapter calAdapter;

    ArrayList<accountData> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getProfile();

        //button definition
        goNotification = (Button)findViewById(R.id.dashboard_button_notifications);
        goGoals = (Button)findViewById(R.id.dashboard_button_goals);
        goSubject = (Button)findViewById(R.id.dashboard_button_subject);
        goStat = (Button)findViewById(R.id.dashboard_button_statistics);
        goCalendar= (Button)findViewById(R.id.dashboard_button_calendar);
        goOWL = (Button)findViewById(R.id.dashboard_button_ontology);

        imgExit = (ImageView)findViewById(R.id.dashboard_logo);

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                //finish();
                //System.exit(0);
                startActivity(new Intent(Dashboard.this, CreateAccount.class));
            }
        });

        goNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, notifications.class));
            }
        });

        goGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Goals.class));
            }
        });

        goSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Subjects.class));
                //startActivity(new Intent(Dashboard.this, EnrolmentRecord.class));
            }
        });

        goStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, statistics.class));
                //Toast.makeText(getApplicationContext(), "Invalid Click!",Toast.LENGTH_SHORT).show();
            }
        });

        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, CalendarSelect.class));
            }
        });

        goOWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, time.class));
            }
        });


        //get Activity list ready for calendar page

        userName = accountList.get(0).getUserName();
        calendarData.calData = new ArrayList<calendarData>();
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month2 = (GregorianCalendar) month.clone();
        calAdapter = new calendarAdapter( this, month, calendarData.calData);
        page p = new page();
        firstDay = calAdapter.getFirstDayYear();
        lastDay = calAdapter.getLastDay();
        p.getUserActivities(firstDay, lastDay, userName);
        //p.queryEnd();


    }

    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public void getProfile( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<accountData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            //create fake user account (*this will never be null due to login/create account script, primarily used for testing, secondary for error checking)
            accountData acc = new accountData("user", "user", "user", "user", "2019-06-03T00:00:00", "user");
            accountList.add(acc);
        }
    }
}
