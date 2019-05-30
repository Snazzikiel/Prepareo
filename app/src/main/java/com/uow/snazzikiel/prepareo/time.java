package com.uow.snazzikiel.prepareo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class time extends AppCompatActivity
{
    //ArrayList<String> updateActions = new ArrayList<>();
    //ArrayList<String> updatePerson = new ArrayList<>();
    private static final String TAG = "owlCheck";
    Button selectTime;
    Button switchTime;
    Button sendUpdates;
    NumberPicker startTimePicker;
    NumberPicker endTimePicker;

    Date curTime = Calendar.getInstance().getTime();
    SimpleDateFormat getDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat getSuffix = new SimpleDateFormat("yyyyMMddmmssMs");
    String theDate = getDate.format(curTime);
    String uniqueSuffix = getSuffix.format(curTime);

    String activityType;
    String activityDate;
    String activityInterval;
    String addStartTime = "";
    String addEndTime = "";
    String username = "sallyseo"; //needs to come from app

    HashMap<String, ArrayList<String>> owlItems;

    final String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
    final String prefix =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                    "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadOwl();
        testHashMap();
        final String[] allTimes12hr = new String []{
                "12:00am", "12:30am", "1:00am", "1:30am" ,"2:00am" ,"2:30am", "3:00am", "3:30am", "4:00am", "4:30am", "5:00am", "5:30am", "6:00am",
                "6:30am", "7:00am", "7:30am" ,"8:00am" ,"8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                "12:30pm", "1:00pm", "1:30pm" ,"2:00pm" ,"2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm", "5:00pm", "5:30pm", "6:00pm",
                "6:30pm", "7:00pm", "7:30pm" ,"8:00pm" ,"8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"};

        final String[] allTimes24hr = new String []{
                "00:00", "00:30", "01:00", "01:30" ,"02:00" ,"02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
                "06:30", "07:00", "07:30" ,"08:00" ,"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                "12:30", "13:00", "13:30", "14:00", "14:30" ,"15:00" ,"15:30", "16:00", "16:30", "17:00", "17:30", "18:00pm",
                "18:30", "19:00", "19:30", "20:00", "20:30" ,"21:00" ,"21:30", "22:00", "22:30", "23:00pm", "23:30"};

        setContentView(R.layout.time);
        selectTime = (Button) findViewById(R.id.selectTime);
        switchTime = (Button) findViewById(R.id.btnEnd);
        sendUpdates = (Button) findViewById(R.id.executeArray);
        startTimePicker = (NumberPicker) findViewById(R.id.startPick);
        endTimePicker = (NumberPicker) findViewById(R.id.endPick);

        startTimePicker.setMinValue(0);
        startTimePicker.setMaxValue(allTimes12hr.length-1);
        startTimePicker.setWrapSelectorWheel(false);
        startTimePicker.setValue(24);
        startTimePicker.setDisplayedValues(allTimes12hr);

        endTimePicker.setMinValue(0);
        endTimePicker.setMaxValue(allTimes12hr.length-1);
        endTimePicker.setWrapSelectorWheel(false);
        endTimePicker.setValue(24);
        endTimePicker.setDisplayedValues(allTimes12hr);

        switchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchTime.getText().equals("to24hour")) {
                    startTimePicker.setDisplayedValues(allTimes24hr);
                    endTimePicker.setDisplayedValues(allTimes24hr);
                    switchTime.setText("to12hour");
                } else {
                    startTimePicker.setDisplayedValues(allTimes12hr);
                    endTimePicker.setDisplayedValues(allTimes12hr);
                    switchTime.setText("to24hour");
                }
            }
        });

        startTimePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i(TAG, String.valueOf(newVal));

                String[] time;
                List<String> timesList;
                switchTime = (Button) findViewById(R.id.btnEnd);
                endTimePicker = (NumberPicker) findViewById(R.id.endPick);

                final String[] xxx = new String []{
                        "16:30", "17:00", "17:30", "18:00pm",
                        "18:30", "19:00", "19:30", "20:00", "20:30" ,"21:00" ,"21:30", "22:00", "22:30", "23:00pm", "23:30"};

                if (switchTime.getText().equals("to24hour")) {
                    timesList = new ArrayList<>(Arrays.asList(allTimes24hr));
                } else {
                    timesList = new ArrayList<>(Arrays.asList(allTimes12hr));
                }
                View child = picker.getChildAt(picker.getValue());

                 for (int i = 0; i < timesList.size(); i++){
                     if (i < newVal){
                         timesList.remove(i);
                     }
                 }

                 time = timesList.toArray(new String[0]);
                 endTimePicker.setValue(15);
                 endTimePicker.setDisplayedValues(xxx);


                //setNumberPickerTextColor(picker);

            }
        });

        /*sendUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new time.updateEndpointArray().execute(updateActions, updatePerson);
            }
        });*/

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                activityType = "Driving"; //Get this from activity picked. auto to "Sleeping" if first entry
                activityDate = activityType + ":" + uniqueSuffix;
                activityInterval = activityDate + ":INTERVAL";

                addStartTime = (theDate + "T" + allTimes24hr[startTimePicker.getValue()] + ":00");
                addEndTime = (theDate + "T" + allTimes24hr[endTimePicker.getValue()] + ":00");

                String updateString =   prefix +
                        "INSERT DATA { " +
                        "onto:" + activityInterval + " rdf:type onto:Interval ; " +
                        "onto:hasStartTime '" + addStartTime + "'^^xsd:dateTime ;" +
                        "onto:hasEndTime '" + addEndTime + "'^^xsd:dateTime ." +
                        "onto:" + activityDate + " rdf:type onto:" + activityType + " ;" +
                        "onto:hasDuration onto:" + activityInterval + " }";

                String updateUser =     prefix +
                        "INSERT { " +
                        "?user onto:hasAction onto:" + activityDate +
                        ". } WHERE { " +
                        "?user rdf:type onto:Student; " +
                        "onto:hasUsername '" + username + "'" +
                        "}";
                //updateActions.add(updateString);
                //updatePerson.add(updateUser);
                new time.updateEndpoint().execute(updateString, updateUser, updateEndpoint);
            }
        });
    }

    public void setNumberPickerTextColor(NumberPicker numberPicker){
        int color= Color.RED;

        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Log.e("setNumberPickerColor1", "test");
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                }
                catch(NoSuchFieldException e){
                    Log.e("setNumberPickerColor1", ""+e);
                }
                catch(IllegalAccessException e){
                    Log.e("setNumberPickerColor2", ""+e);
                }
                catch(IllegalArgumentException e){
                    Log.e("setNumberPickerColor3", ""+e);
                }
            }
        }
    }

    protected class updateEndpoint extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... updateAndTarget)
        {
            UpdateRequest update = UpdateFactory.create(updateAndTarget[0]);
            UpdateProcessor uexec = UpdateExecutionFactory.createRemote(update, updateAndTarget[2]);
            uexec.execute();

            UpdateRequest updateUser = UpdateFactory.create(updateAndTarget[1]);
            UpdateProcessor uexecUser = UpdateExecutionFactory.createRemote(updateUser, updateAndTarget[2]);
            uexecUser.execute();
            return "update";
        }
        @Override
        protected void onPostExecute(String success)
        {
        }
    }

    public void loadOwl( ) {

        SharedPreferences sharedPreferences = getSharedPreferences("aSyncData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("aSyncOwlData", null);
        Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType();
        owlItems = gson.fromJson(json, type);

        if (owlItems == null) {
            owlItems = new HashMap<>();
        }
    }

    public void testHashMap(){

        Log.i(TAG, "This is an owl Check for data");
        Log.i(TAG, String.valueOf(owlItems.size()));
    }


}