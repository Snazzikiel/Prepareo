package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class calendarInfo extends AppCompatActivity {
    private static final String TAG = "calendarInfo";

    String calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();
        calendarDate = thisIntent.getStringExtra("dateSelected");
        setTitle(calendarDate + " - Assignments");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar_info);
    }

    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(calendarData.calData);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }
/*
    public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.calendar_savedata)), null);
        Type type = new TypeToken<ArrayList<calendarData>>() {}.getType();

        calItems = gson.fromJson(json, type);

        if (calItems == null) {
            calItems = new ArrayList<>();
        }
    }
*/

    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
