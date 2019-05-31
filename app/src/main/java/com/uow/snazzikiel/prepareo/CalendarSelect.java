package com.uow.snazzikiel.prepareo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CalendarSelect extends AppCompatActivity {
    private static final String TAG = "stateCheck";
    public GregorianCalendar month, month2;
    private calendarAdapter calAdapter;
    private TextView tvMonth;
    GridView calView;

    List<calendarData> calItems = new ArrayList<calendarData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarData.calData = new ArrayList<calendarData>();


        loadData();

        if (calItems != null){
            calendarData.calData.add(new calendarData("2019-06-28" ,"Diwali","Holiday","this is holiday"));
            calendarData.calData.add(new calendarData("2019-06-13" ,"Holi","Holiday","this is holiday"));
            calendarData.calData.add(new calendarData("2019-06-05" ,"Statehood Day","Holiday","this is holiday"));
            calendarData.calData.add(new calendarData("2019-05-02" ,"Republic Unian","Holiday","this is holiday"));
            calendarData.calData.add(new calendarData("2019-04-05" ,"ABC","Holiday","this is holiday"));
            calendarData.calData.add(new calendarData("2017-06-15" ,"demo","Holiday","this is holiday"));
            saveData();
        }

        Log.i(TAG, String.valueOf(calItems.size()));

        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month2 = (GregorianCalendar) month.clone();
        calAdapter = new calendarAdapter( this, month, calendarData.calData);

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
                String selectedGridDate = HwAdapter.day_string.get(position);
                //((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity.this);
                //gridview.getChildAt(position).setBackgroundColor(Color.BLACK);
                // v.setBackgroundColor(Color.RED);

                clearCalendar();
                Log.i(TAG, selectedGridDate);
            }

        });
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
        //calAdapter.refreshCal();
        //calAdapter.notifyDataSetChanged();
        //tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(HomeCollection.date_collection_arr);
        editor.putString((getString(R.string.calendar_savedata)), json);
        editor.apply();
    }

    public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("calendarData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.calendar_savedata)), null);
        Type type = new TypeToken<ArrayList<HomeCollection>>() {}.getType();
        /*HomeCollection.date_collection_arr = gson.fromJson(json, type);

        if (HomeCollection.date_collection_arr == null) {
            HomeCollection.date_collection_arr = new ArrayList<>();
        }*/

        calItems = gson.fromJson(json, type);

        if (calItems == null) {
            calItems = new ArrayList<>();
        }
    }
}
