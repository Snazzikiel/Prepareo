package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
    Class:   sharedPreferences
    ---------------------------------------
    ***Obsolete Class - called within individual classes***
 *  Changed due to adding calls in to local classes
    Class used to assist in storing objects in to local device.

*/

public class sharedPreferences extends AppCompatActivity {

    public sharedPreferences(){}

    public void saveNotifications( ArrayList<notificationData> rowItems ) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString("task list", json);
        editor.apply();
    }

    public ArrayList<notificationData> loadNotifications( ArrayList<notificationData> rowItems ) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<notificationData>();
        }

        return rowItems;
    }




}
