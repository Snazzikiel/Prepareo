package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.uow.snazzikiel.prepareo.Dashboard.getWeekStartDate;

public class CalendarSelect extends AppCompatActivity {
    private static final String TAG = "stateCheck";
    public GregorianCalendar month, month2;
    private calendarAdapter calAdapter;
    private TextView tvMonth;
    GridView calView;

    List<calendarData> calItems = new ArrayList<calendarData>();
    ArrayList<accountData> accountList;

    //HashMap<String,Long> hoursMap = new HashMap<String,Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_calendar);

        calendarData.calData = new ArrayList<calendarData>();

        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month2 = (GregorianCalendar) month.clone();
        calAdapter = new calendarAdapter( this, month, calendarData.calData);

        getProfile();
        String userName = accountList.get(0).getUserName();

//        Query.getUserActivities(calAdapter.getFirstMonday(), calAdapter.getNextMonday());

        /*

        Log.i(TAG, "Owl Info!" + String.valueOf(owlData.owlInfo.size()));
*/

        Date tmpDate = new Date();
        Calendar s = Calendar.getInstance();
        s.setTime(tmpDate);
        s.add(Calendar.DATE, 1);
        tmpDate = s.getTime();
        String nextMonday = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", tmpDate));
        Log.i(TAG, "-----------");

        page p = new page();

        String mon1 = calAdapter.getFirstMonday();
        String mon2 = calAdapter.getNextMonday();

        p.getUserActivities(mon1, mon2, userName);
        if (owlData.owlInfo == null){
            owlData.owlInfo = new ArrayList<owlData>();
            owlData.owlInfo.add(new owlData("ptd665", "empty", (long)0));
        }

        Log.i(TAG, String.valueOf(owlData.owlInfo));
        for(owlData tmp : owlData.owlInfo){
            String user = tmp.getUserName();
            Long i = tmp.getMapTime();
            String key = tmp.getMapKey();
            Log.i(TAG, user + " " + key + " long: " + i);
        }


        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));


        ImageButton back = (ImageButton) findViewById(R.id.ib_prev);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month.get(GregorianCalendar.MONTH) == 4&&month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(CalendarSelect.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setLastMonth();
                    clearCalendar();
                }
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month.get(GregorianCalendar.MONTH) == 5&&month.get(GregorianCalendar.YEAR)==2018) {
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

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setLastMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void clearCalendar() {
        calAdapter.refreshCal();
        calAdapter.notifyDataSetChanged();
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
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
