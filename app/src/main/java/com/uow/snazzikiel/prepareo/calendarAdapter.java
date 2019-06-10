package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Alec
 ***********************************************/


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hp.hpl.jena.sparql.function.library.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import static com.uow.snazzikiel.prepareo.Dashboard.getWeekStartDate;

/**
    Class:   calendarAdapter
    ---------------------------------------
    Adapter class to load list in to the calendar Page.
    Create calendar in GridView, take events from OWL file and circle them if activity
    has been loaded.

*/
class calendarAdapter extends BaseAdapter {

    private static final String TAG = "calAdapter";
    private Activity context;

    Calendar month;

    GregorianCalendar gcMonth;
    GregorianCalendar gcMaxMonth;
    private GregorianCalendar gcDateSelect;
    int monthFDay;
    int iTotalWeekNumber;
    int totalPreviousMonth;
    int calendarMaxPrevious;
    int monthDays;
    String selectionDate;
    String todayDate;
    DateFormat dfSelection;

    private ArrayList<String> eventItems;
    public static List<String> day;
    public ArrayList<calendarData>  calData;
    private String gridvalue;

    public calendarAdapter(Activity context, GregorianCalendar monthCal,ArrayList<calendarData> calData) {
        this.calData=calData;
        calendarAdapter.day = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCal;
        gcDateSelect = (GregorianCalendar) monthCal.clone();

        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.eventItems = new ArrayList<String>();

        dfSelection = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        todayDate = dfSelection.format(gcDateSelect.getTime());
        refreshCal();

    }

    /**
        Function:   getCount
        ---------------------------------------
        returns size of day array
    */
    public int getCount() {
        return day.size();
    }

    /**
        Function:   getItem
        ---------------------------------------
        returns item on selected position
    */
    public Object getItem(int position) {
        return day.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
        Function:   getView
        ---------------------------------------
        Create the calendar view, day by day. Get dates from current month and color
        days accordingly.
    */
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day.get(position).split("-");

        gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < monthFDay)) {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridGray));
            dayView.setClickable(true);
            dayView.setFocusable(true);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridGray));
            dayView.setClickable(true);
            dayView.setFocusable(true);
        } else {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridMonth));
        }


        if (day.get(position).equals(todayDate)) {
            v.setBackgroundResource(R.drawable.bg_star);
            dayView.setTextColor(context.getResources().getColor(R.color.colorWhiteSmoke));
        } else {
            v.setBackgroundColor(Color.WHITE);
        }

        dayView.setText(gridvalue);
        String date = day.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }

        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        setEventView(v, position,dayView);



        return v;
    }

    /**
        Function:   activityView
        ---------------------------------------
        same as getView by different Context items
    */
    public View activityView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_item, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day.get(position).split("-");

        gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < monthFDay)) {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridGray));
            dayView.setClickable(true);
            dayView.setFocusable(true);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridGray));
            dayView.setClickable(true);
            dayView.setFocusable(true);
        } else {
            dayView.setTextColor(context.getResources().getColor(R.color.colorGridMonth));
        }

        if (day.get(position).equals(todayDate)) {
            v.setBackgroundColor(Color.WHITE);
        } else {
            v.setBackgroundColor(Color.WHITE);
        }

        dayView.setText(gridvalue);
        String date = day.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }

        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        setEventView(v, position,dayView);

        return v;
    }

    /**
        Function:   getMaxP
        ---------------------------------------
        Get the dates of the previous month for start of calendar
    */
    private int getMaxP() {
        int maxPrevMonth;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            gcMonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            gcMonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxPrevMonth = gcMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxPrevMonth;
    }

    /**
        Function:   setEventView
        ---------------------------------------
        Look through activity list and circle each date that has a loaded activity in RED

        v:          Current view of device
        iPosition:  Date position of item in grid list
        txt:        txtView displaying the text of the date in the view
    */
    public void setEventView(View v,int iPosition,TextView txt){
        int len=calendarData.calData.size();

        Log.i(TAG, day.get(iPosition));
        Log.i(TAG, String.valueOf(len));
        for (int i = 0; i < len; i++) {

            calendarData eventLoaded = calendarData.calData.get(i);
            String date = eventLoaded.date;


            int len1 = day.size();
            if (len1 > iPosition) {
                if (day.get(iPosition).equals(date)) {
                    if ((Integer.parseInt(gridvalue) > 1) && (iPosition < monthFDay)) {

                    } else if ((Integer.parseInt(gridvalue) < 7) && (iPosition > 28)) {

                    } else {
                        v.setBackgroundColor(Color.parseColor("#343434"));
                        v.setBackgroundResource(R.drawable.rounded_calender);
                        txt.setTextColor(Color.parseColor("#696969"));
                    }
                }

            }
        }
    }

    /**
        Function:   refreshCal
        ---------------------------------------
        Refresh all Calendar information each month is selected
    */
    public void refreshCal() {
        eventItems.clear();
        day.clear();
        Locale.setDefault(Locale.US);
        gcMonth = (GregorianCalendar) month.clone();
        monthFDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        iTotalWeekNumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        monthDays = iTotalWeekNumber * 7;
        totalPreviousMonth = getMaxP();
        calendarMaxPrevious = totalPreviousMonth - (monthFDay - 1);
        gcMaxMonth = (GregorianCalendar) gcMonth.clone();
        gcMaxMonth.set(GregorianCalendar.DAY_OF_MONTH, calendarMaxPrevious + 1);


        for (int n = 0; n < monthDays; n++) {
            selectionDate = dfSelection.format(gcMaxMonth.getTime());
            gcMaxMonth.add(GregorianCalendar.DATE, 1);
            day.add(selectionDate);
        }
    }

    /**
        Function:   getFirstMonday
        ---------------------------------------
        Get the first monday of the current month
    */
    public String getFirstMonday(){

        Date s = getWeekStartDate();
        String mondayDate = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", s));

        return mondayDate + "T00:00:00";
    }

    /**
        Function:   getNextMonday
        ---------------------------------------
        get the next monday of the next month
    */
    public String getNextMonday(){
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);

        Date tmpDate = calendar.getTime();
        String nextMonday = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", tmpDate));
        return nextMonday + "T00:00:00";
    }

    /**
        Function:   getFirstDay
        ---------------------------------------
        Get the first day of the month
    */
    public String getFirstDay(){
        //Get start of the month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date tmpDate = new Date();
        tmpDate = cal.getTime();
        String firstDay = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", tmpDate));

        return firstDay;
    }

    /**
        Function:   getFirstDayYear
        ---------------------------------------
        Get the first day of the year
    */
    public String getFirstDayYear(){
        //Get start of the month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date tmpDate = new Date();
        tmpDate = cal.getTime();
        String firstDay = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", tmpDate));
        firstDay += "T00:00:00";
        return firstDay;
    }

    /**
        Function:   getLastDay
        ---------------------------------------
        Get the last day of the year
    */
    public String getLastDay(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date lastDayOfMonth = cal.getTime();
        String lastDay = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", lastDayOfMonth));
        lastDay += "T00:00:00";
        return lastDay;
    }
}

