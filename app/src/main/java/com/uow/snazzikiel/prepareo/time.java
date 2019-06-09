package com.uow.snazzikiel.prepareo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
    Class:   time
    ---------------------------------------
    Used to load activities to OWL File
*/
public class time extends AppCompatActivity {

    private static final String TAG = "owlTime";
    Button selectTime;
    Button sendUpdates;
    Switch switchTime;
    NumberPicker startTimePicker;
    NumberPicker endTimePicker;
    NumberPicker catPick;
    NumberPicker subPick;

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
    String username = "ptd665"; //needs to come from app

    HashMap<String, ArrayList<String>> owlItems;

    final String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
    final String prefix =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                    "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    String actionUpdates = prefix; //store all action updates
    String userUpdates = prefix; //store all user updates

    /*
        Class:   accountVerification
        ---------------------------------------
        Verification class used to check data against the OWL file when user is creating or logging in.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Load Activities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.time);

        loadOwl();

        final String [] allCat = new String[owlItems.size()];

        int i = 0;
        for(String key : owlItems.keySet())
        {
            allCat[i] = (key);
            i++;
        }

        final String[] allTimes12hr = new String []
                {
                        "12:00am", "12:30am", "1:00am", "1:30am" ,"2:00am" ,"2:30am", "3:00am", "3:30am", "4:00am", "4:30am", "5:00am", "5:30am", "6:00am",
                        "6:30am", "7:00am", "7:30am" ,"8:00am" ,"8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm",
                        "12:30pm", "1:00pm", "1:30pm" ,"2:00pm" ,"2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm", "5:00pm", "5:30pm", "6:00pm",
                        "6:30pm", "7:00pm", "7:30pm" ,"8:00pm" ,"8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm", "11:59pm"
                };

        final String[] allTimes24hr = new String []
                {
                        "00:00", "00:30", "01:00", "01:30" ,"02:00" ,"02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
                        "06:30", "07:00", "07:30" ,"08:00" ,"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                        "12:30", "13:00", "13:30", "14:00", "14:30" ,"15:00" ,"15:30", "16:00", "16:30", "17:00", "17:30", "18:00pm",
                        "18:30", "19:00", "19:30", "20:00", "20:30" ,"21:00" ,"21:30", "22:00", "22:30", "23:00", "23:30", "23:59"
                };

        setContentView(R.layout.time);
        selectTime = (Button) findViewById(R.id.selectTime);
        switchTime = (Switch) findViewById(R.id.switcher);
        sendUpdates = (Button) findViewById(R.id.executeArray);
        startTimePicker = (NumberPicker) findViewById(R.id.startPick);
        endTimePicker = (NumberPicker) findViewById(R.id.endPick);
        catPick = (NumberPicker) findViewById(R.id.catPick);
        subPick = (NumberPicker) findViewById(R.id.subPick);

        catPick.setMinValue(0);
        catPick.setMaxValue(allCat.length-1);
        catPick.setDisplayedValues(allCat);

        String []allSubs = owlItems.get(allCat[catPick.getValue()]).toArray(new String[0]);
        subPick.setMinValue(0);
        subPick.setMaxValue(0);
        subPick.setDisplayedValues(null);
        subPick.setMaxValue(allSubs.length-1);
        subPick.setDisplayedValues(allSubs);

        catPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                String []allSubs = owlItems.get(allCat[catPick.getValue()]).toArray(new String[0]);
                subPick.setMinValue(0);
                subPick.setMaxValue(0);
                subPick.setDisplayedValues(null);
                subPick.setMaxValue(allSubs.length-1);
                subPick.setDisplayedValues(allSubs);
            }
        });

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

        switchTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    startTimePicker.setDisplayedValues(allTimes24hr);
                    endTimePicker.setDisplayedValues(allTimes24hr);
                }
                else
                {
                    startTimePicker.setDisplayedValues(allTimes12hr);
                    endTimePicker.setDisplayedValues(allTimes12hr);
                }
            }
        });

        //for one slider
        /*selCat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (selCat.getText().equals("SelectCat"))
                {
                    //store subs of cat
                    catSubs = owlItems.get(allCat[catPick.getValue()]);

                    //convert to array for number picker (soft copy)
                    String []allSubs = catSubs.toArray(new String[0]);
                    catPick.setMinValue(0);
                    catPick.setMaxValue(0);
                    catPick.setDisplayedValues(null);
                    catPick.setMaxValue(allSubs.length-1);
                    catPick.setDisplayedValues(allSubs);
                    selCat.setText("Back");
                }
                else
                {

                    catPick.setMinValue(0);
                    catPick.setMaxValue(0);
                    catPick.setDisplayedValues(null);
                    catPick.setMaxValue(allCat.length-1);
                    catPick.setDisplayedValues(allCat);
                    selCat.setText("SelectCat");
                }
            }
        });*/

        sendUpdates.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(actionUpdates.equals(prefix))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Please add one or more activities before saving";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    new time.updateEndpoint().execute(actionUpdates, userUpdates, updateEndpoint);
                }
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(startTimePicker.getValue() >= endTimePicker.getValue())
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Please select a valid time range";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    activityType = owlItems.get(allCat[catPick.getValue()]).get(subPick.getValue());
                    activityDate = activityType + ":" + uniqueSuffix;
                    activityInterval = activityDate + ":INTERVAL";

                    addStartTime = (theDate + "T" + allTimes24hr[startTimePicker.getValue()] + ":00");
                    addEndTime = (theDate + "T" + allTimes24hr[endTimePicker.getValue()] + ":00");

                    String updateAddAction =
                            "INSERT DATA { " +
                                    "onto:" + activityInterval + " rdf:type onto:Interval ; " +
                                    "onto:hasStartTime '" + addStartTime + "'^^xsd:dateTime ;" +
                                    "onto:hasEndTime '" + addEndTime + "'^^xsd:dateTime ." +
                                    "onto:" + activityDate + " rdf:type onto:" + activityType + " ;" +
                                    "onto:hasDuration onto:" + activityInterval + " }";

                    String updateAttachUser =
                            "INSERT { " +
                                    "?user onto:hasAction onto:" + activityDate +
                                    ". } WHERE { " +
                                    "?user rdf:type onto:Student; " +
                                    "onto:hasUsername '" + username + "'" +
                                    "}";

                    actionUpdates += updateAddAction + " ;";
                    userUpdates += updateAttachUser + " ;";
                }
            }
        });
    }

    /*
        Class: updateEndpoint
        ---------------------------------------
        This class is an AsyncTask, which means it will run in the background whilst the application is
        continuing to function. This class is used to query the OWL file to verify if a user exist, if a user
        does not exist, it will insert the entered data to the OWL file to create the user account.
    */
    protected class updateEndpoint extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... updateAndTarget)
        {
            //add action
            UpdateRequest update = UpdateFactory.create(updateAndTarget[0]);
            UpdateProcessor uexec = UpdateExecutionFactory.createRemote(update, updateAndTarget[2]);
            uexec.execute();

            //relate to user
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

    /*
        Function:   loadOwl
        ---------------------------------------
        Used to retrieve the loadSubjects object to the local android device.
        Use a saveData function to call "aSyncOwlData" SharedPreference to overwrite.
    */
    public void loadOwl( ){
        SharedPreferences sharedPreferences = getSharedPreferences("aSyncData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("aSyncOwlData", null);
        Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType();

        owlItems = gson.fromJson(json, type);

        if (owlItems == null)
        {
            owlItems = new HashMap<>();
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add("temp");
            owlItems.put("temp", tmp);
        }
    }




}