package com.uow.snazzikiel.prepareo;

/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Adam
 * Assisted:		Connor, Lachlan
 ***********************************************/

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/*
    Class: Dashboard
    ---------------------------------------
    Main dashboard page. All OWL activity data regarding activities and date input is loaded from the OWL file
*/
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
    page owlLoad;

    HashMap<String,ArrayList<String>> menu = new HashMap<String,ArrayList<String>>();
    //prefix required to query OWL file
    final String  prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
            "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    /*
        Function: onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getProfile();

        //Get all data from the owl file for student
        //Set start date, and end date.. for sample purposes, get from first day of year until
        //current day.
        owlLoad = new page();
        userName = accountList.get(0).getUserName();
        calendarData.calData = new ArrayList<calendarData>();
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month2 = (GregorianCalendar) month.clone();
        calAdapter = new calendarAdapter( this, month, calendarData.calData);
        firstDay = calAdapter.getFirstDayYear();
        lastDay = calAdapter.getLastDay();
        loadActivityList();
        loadUserActivities();

        //button definition
        goNotification = (Button)findViewById(R.id.dashboard_button_notifications);
        goGoals = (Button)findViewById(R.id.dashboard_button_goals);
        goSubject = (Button)findViewById(R.id.dashboard_button_subject);
        goStat = (Button)findViewById(R.id.dashboard_button_statistics);
        goCalendar= (Button)findViewById(R.id.dashboard_button_calendar);
        goOWL = (Button)findViewById(R.id.dashboard_button_ontology);

        /*
        imgExit = (ImageView)findViewById(R.id.dashboard_logo);

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                //finish();
                //System.exit(0);
                startActivity(new Intent(Dashboard.this, CreateAccount.class));
            }
        });*/

        //Set actionable items for each button on dashboard.
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
    }

    /*
        Function: loadUserActivities
        ---------------------------------------
        Load activities entered with date/times in to owl object
    */
    public void loadUserActivities(){
        //get Activity list ready for calendar page, get all activities loaded for user
        owlLoad.getUserActivities(firstDay, lastDay, userName);
    }

    /*
        Function: loadActivityList
        ---------------------------------------
        Load each activity the OWL file has in to an object
    */
    public void loadActivityList(){
        //get list of activities for the user to load activities
        getActivityList();
    }

    /*
        Function: getWeekStartDate
        ---------------------------------------
        Get the date of Monday in current week
    */
    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }

    /*
        Function: getWeekEndDate
        ---------------------------------------
        Get the date of Sunday in current week
    */
    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /*
        Function: getProfile
        ---------------------------------------
        Load profile of logged in user
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

    /*
        Function: getActivityList
        ---------------------------------------
        Prepare query and begin aSyncTask to get all activities in OWL
    */
    public void getActivityList(){
        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "SELECT ?cat ?sub WHERE { " +
                "?cat rdfs:subClassOf onto:Action . " +
                "?sub rdfs:subClassOf ?cat " +
                "}";
        new queryEndpoint().execute(queryString, queryEndpoint, "cat", "sub");
    }

    /*
        Class: queryEndpoint
        ---------------------------------------
        aSyncTask to query the OWL file. Gets a list of each activity and sub category.
        Places OWL data in to a hashtable object.
    */
    protected class queryEndpoint extends AsyncTask<String, String, HashMap<String, ArrayList<String>>>
    {
        @Override
        protected HashMap<String, ArrayList<String>> doInBackground(String... queryTargetVars)
        {
            menu = new HashMap<String,ArrayList<String>>();
            Query sparqlQuery = sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);

            try
            {
                ResultSet results = qexec.execSelect();
                String key = "";
                ArrayList tmpList = new ArrayList<>();

                for(;results.hasNext();)
                {
                    QuerySolution soln = results.nextSolution();
                    RDFNode catNode = soln.get(queryTargetVars[2]);
                    String cat = catNode.asNode().getLocalName();

                    RDFNode subNode = soln.get(queryTargetVars[3]);
                    String sub = subNode.asNode().getLocalName();

                    //place Space before any uppercase letter in category or sub-cat
                    Log.i(TAG, "This is the cat: " + cat);
                    Log.i(TAG, "This is the sub: " + sub);
                    cat = Character.toUpperCase(cat.charAt(0)) +
                            cat.substring(1).replaceAll("(?<!_)(?=[A-Z])", " ");
                    sub = Character.toUpperCase(sub.charAt(0)) +
                            sub.substring(1).replaceAll("(?<!_)(?=[A-Z])", " ");
                    Log.i(TAG, "This is the cat: " + cat);
                    Log.i(TAG, "This is the sub: " + sub);

                    if(key.equals(cat))
                    {
                        tmpList.add(sub);
                    }
                    else
                    {
                        if(key.equals(""))
                        {
                            key = cat;
                            tmpList.add(sub);
                        }
                        else
                        {
                            menu.put(key, tmpList);
                            tmpList = new ArrayList<>();
                            key = cat;
                            tmpList.add(sub);
                            Log.i(TAG, sub);
                        }
                    }
                }
                menu.put(key, tmpList);
            }
            finally
            {
                qexec.close();
            }
            return menu;
        }

        @Override
        protected void onPostExecute(HashMap <String, ArrayList<String>> menu)
        {
            Log.i(TAG, "saving to the owl file!!!");
            saveOwl(menu);
        }
    }

    /*
        Function:   saveOwl
        ---------------------------------------
        Used to store the activity list HashTable object to the local android device.
        Use a loadData function to call "aSyncData" SharedPreference to access.
    */
    public void saveOwl(HashMap<String, ArrayList<String>> menu) {
        Log.i(TAG, "saveOwl");
        SharedPreferences sharedPreferences = getSharedPreferences("aSyncData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(menu);
        editor.putString("aSyncOwlData", json);
        editor.apply();
    }
}
