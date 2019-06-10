package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Alec
 * Assisted:		David
 ***********************************************/

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
    Class:   CalendarSelect
    ---------------------------------------
    Adapter class to load list in to the calendar page.
    Create calendar in GridView, take events from OWL file and circle them if activity
    has been loaded.
*/
public class CalendarSelect extends AppCompatActivity {
    private static final String TAG = "calendarSelect";
    public GregorianCalendar month, month2;
    private calendarAdapter calAdapter;
    private TextView tvMonth;
    GridView calView;

    List<owlData> calItems = new ArrayList<owlData>();
    ArrayList<accountData> accountList;

    List<owlData> allActivities;

    /**
        Function: onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);

        for(owlData tmp : owlData.owlInfo){

            String user = tmp.getUserName();
            String d = tmp.getDate();
            String key = tmp.getMapKey();
            Long i = tmp.getMapTime();
            Log.i(TAG, user + " " + d + " " + key + " long1234: " + i);
            calendarData.calData.add(new calendarData(d, user, key, String.valueOf(i)));
        }

        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month2 = (GregorianCalendar) month.clone();
        calAdapter = new calendarAdapter( this, month, calendarData.calData);


        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));


        //put restriction on how far back user can go in date calendar
        ImageButton back = (ImageButton) findViewById(R.id.ib_prev);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month.get(GregorianCalendar.MONTH) == 4&&month.get(GregorianCalendar.YEAR)==2000) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(CalendarSelect.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setLastMonth();
                    clearCalendar();
                }
            }
        });

        //put restriction on how far forward a user can go in date calendar
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month.get(GregorianCalendar.MONTH) == 5&&month.get(GregorianCalendar.YEAR)==2025) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(CalendarSelect.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    clearCalendar();
                }
            }
        });

        calView = (GridView) findViewById(R.id.gv_calendar);
        calView.setAdapter(calAdapter);

        calView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = calendarAdapter.day.get(position);
                Intent myIntent = new Intent(getApplicationContext(), calendarInfo.class);
                myIntent.putExtra("dateSelected", selectedGridDate);
                startActivityForResult(myIntent, 0);

                //clearCalendar();
                Log.i(TAG, selectedGridDate);
            }

        });
    }

    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default required function to include a back button arrow on the top of the page
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /**
        Function:   setNextMonth
        ---------------------------------------
        Get name/number of next month
    */
    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    /**
        Function:   setLastMonth
        ---------------------------------------
        get last month
    */
    protected void setLastMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    /**
        Function:   clearCalendar
        ---------------------------------------
        Clears the calendar and all information loaded on interface
    */
    public void clearCalendar() {
        calAdapter.refreshCal();
        calAdapter.notifyDataSetChanged();
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    /**
        Function:   saveData
        ---------------------------------------
        Used to store the activity list object to the local android device.
        Use a loadData function to call "calendarData" SharedPreference to access.
    */
    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(calendarData.calData);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }

    /**
        Function:   loadData
        ---------------------------------------
        Used to retrieve the calItems object to the local android device.
        Use a saveData function to call "calendarData" SharedPreference to overwrite.
    */
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

    /**
        Function:   getProfile
        ---------------------------------------
        Used to retrieve the loadSubjects object to the local android device.
        Use a saveData function to call "createAccount" SharedPreference to overwrite.
    */
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
