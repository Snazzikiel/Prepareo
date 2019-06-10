package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Alec
 * Assisted:		David
 ***********************************************/

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
    Class: page
    ---------------------------------------
    Original Query page to speak to owlFile.
    mostly obsolete - Main functions used to Dashboard page
*/
public class page extends AppCompatActivity
{
    private static final String TAG = "owlCheck";
    Button selectButton, addButton, deleteButton, editButton;
    TextView txt1, txt2;
    EditText nameBox, numBox, userBox;
    final String  prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
            "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    String user;
    ArrayList<accountData> accountList;

    //Map<String, List> owlData = new HashMap<>();
    HashMap<String,ArrayList<String>> menu = new HashMap<String,ArrayList<String>>();
    HashMap<String,Long> hoursMap = new HashMap<String,Long>();
    List<owlData> owlList = new ArrayList<>();

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);


        selectButton = (Button) findViewById(R.id.button12);
        addButton = (Button) findViewById(R.id.button11);
        deleteButton = (Button) findViewById(R.id.buttondel);
        editButton = (Button) findViewById(R.id.buttonEdit);

        nameBox = (EditText) findViewById(R.id.editText11);
        numBox = (EditText) findViewById(R.id.editText12);
        userBox = (EditText) findViewById(R.id.editText13);

        txt1 = (TextView) findViewById(R.id.textView12);
        txt2 = (TextView) findViewById(R.id.textViewAdd);

        selectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {
                /*String name, num, user;
                name = nameBox.getText().toString();
                num = numBox.getText().toString();
                user = userBox.getText().toString();

                String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
                String updateString = prefix +
                        "INSERT DATA { " +
                        "onto:"+name+" rdf:type onto:Student ; " +
                        "onto:hasFirstName '"+name+"' ; " +
                        "onto:hasStudentNumber '"+num+"' ;" +
                        "onto:hasUsername '"+user+"'" +
                        "}";
                new updateEndpoint().execute(updateString, updateEndpoint);*/
            }
        });

        /*
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {
                String user;
                user = userBox.getText().toString();

                String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
                String updateString = prefix +
                        "DELETE WHERE { " +
                        "?person rdf:type onto:Student ;" +
                        "onto:hasUsername '"+user+"' ;" +
                        "?prop ?value }";
                new updateEndpoint().execute(updateString, updateEndpoint);
            }
        });
        */

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {


            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x) {
                String user, num;
                user = userBox.getText().toString();
                num = numBox.getText().toString();

                String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
                String updateString = prefix +
                        "DELETE { " +
                        "?p onto:hasStudentNumber ?old" +
                        "}" +
                        "INSERT { " +
                        "?p onto:hasStudentNumber '"+num+"'" +
                        "}" +
                        "WHERE { " +
                        "?p rdf:type onto:Student ;" +
                        "onto:hasUsername '"+user+"' ;" +
                        "onto:hasStudentNumber ?old" +
                        "}";
                new updateEndpoint().execute(updateString, updateEndpoint);
            }
        });
    }

    /**
        Class: queryEndpointQuota
        ---------------------------------------
        aSyncTask to query the OWL file. Set dates wanted to call, and get list of each activity
        loaded between both dates
    */
    protected class queryEndpointQuota extends AsyncTask<String, String, HashMap<String, Long>>
    {
        @Override
        protected HashMap<String, Long> doInBackground(String... queryTargetVars)
        {
            Query sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);
            try
            {
                ResultSet results = qexec.execSelect();
                owlData.owlInfo = new ArrayList<owlData>();
                for(;results.hasNext();)
                {
                    QuerySolution soln = results.nextSolution();

                    RDFNode typeNode = soln.get(queryTargetVars[2]);
                    RDFNode startTime = soln.get(queryTargetVars[3]);
                    RDFNode endTime = soln.get(queryTargetVars[4]);

                    String type = typeNode.asNode().getLocalName();
                    String startHours = startTime.asLiteral().getLexicalForm();
                    String endHours = endTime.asLiteral().getLexicalForm();

                    String dateTime = StringUtils.substringBeforeLast(startHours, "T");

                    startHours = StringUtils.substringAfterLast(startHours, "T");
                    endHours = StringUtils.substringAfterLast(endHours, "T");

                    SimpleDateFormat dif = new SimpleDateFormat("HH:mm:ss");
                    Date parsedStart = dif.parse(startHours);
                    Date parsedEnd = dif.parse(endHours);

                    long milSec = parsedEnd.getTime() - parsedStart.getTime();
                    long min = TimeUnit.MILLISECONDS.toMinutes(milSec);

                    //primary line to add information of activities to owlData Class local storage
                    //Put a space in the TYPE before loading in to OWL Class
                    String tmpType = Character.toUpperCase(type.charAt(0)) +
                            type.substring(1).replaceAll("(?<!_)(?=[A-Z])", " ");
                    owlData.owlInfo.add(new owlData(user, dateTime, startHours, endHours, type, min));
                    Log.i(TAG, user + ".." + dateTime + ".." +  startHours + ".." +  endHours + ".." +  tmpType + ".." +  min);

                    if(hoursMap.containsKey(type))
                    {

                        hoursMap.put(tmpType, (min + hoursMap.get(tmpType)));
                    }
                    else
                    {
                        hoursMap.put(tmpType, (min));
                    }
                    Log.i(TAG, tmpType);
                    Log.i(TAG, startHours+ "   " + endHours);
                    Log.i(TAG, String.valueOf(min));
                    Log.i(TAG, hoursMap.values().toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } finally
            {
                qexec.close();
            }
            return hoursMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, Long> hours)
        {
            //saveOwl();
            createOWL_Objects();
        }
    }

    /**
        Class: updateEndpoint
        ---------------------------------------
        aSyncTask to query the OWL file. Inserts new data to the OWL File
    */
    protected class updateEndpoint extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... updateAndTarget)
        {

            UpdateRequest update = UpdateFactory.create(updateAndTarget[0]);
            UpdateProcessor uexec = UpdateExecutionFactory.createRemote(update, updateAndTarget[1]);
            uexec.execute();
            return "update";
        }

        @Override
        protected void onPostExecute(String success)
        {
            //txt2.append(success + " ");
        }
    }

    /**
        Function: createOWL_Objects
        ---------------------------------------
        Obsolete function - Used to fill owlData.owlInfo with data pulled from query.
        This function is not required to filter the HashMap as we now store the owlData in to the
        local storage owlData Class each time it is requested.
    */
    public void createOWL_Objects(){
        /*
        //obsolete function due to adding this line in the endpointquota
        Log.i(TAG, "Owl created.");
        //owlData.owlInfo = new ArrayList<owlData>();

        for(String key : hoursMap.keySet()){
            Long i = hoursMap.get(key);
            Log.i(TAG, key + ": " + Long.toString(i));
            //owlData.owlInfo.add(new owlData(user, key, i));
        }
        */
    }

    /**
        Function: getUserActivities
        ---------------------------------------
        Start query to get activities from owlFile
        @param firstDate:              Start date of query to be called
        @param firstDateOfNextRange:   End date of query to be called
        @param userName:               Username of person to query
    */
    public void getUserActivities(String firstDate, String firstDateOfNextRange, String userName){
        //user = userBox.getText().toString();
        //firstDate = "2019-05-27T00:00:00";
        //firstDateOfNextRange = "2019-06-03T00:00:00";
        this.user = userName;
        //firstDate = "2019-05-27T00:00:00"; can use 2 different dates for current day hours
        //firstDateOfNextRange = "2019-05-28T00:00:00";
        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "SELECT DISTINCT ?type ?start ?end WHERE { " +
                "?person onto:hasAction ?action ;" +
                "onto:hasUsername '" + userName + "' ." +
                "?action onto:hasDuration ?duration ;"+
                "rdf:type ?type ." +
                "?duration onto:hasStartTime ?start ; "+
                "onto:hasEndTime ?end ." +
                "FILTER (?start >= '" + firstDate + "'^^xsd:dateTime) ." +
                "FILTER (?start < '" + firstDateOfNextRange + "'^^xsd:dateTime) ." +
                "}";
        //"FILTER regex(str(?action), '" + subString + "') ." +
        new queryEndpointQuota().execute(queryString, queryEndpoint, "type", "start", "end");
    }

    /**
        Function: updateActivityList
        ---------------------------------------
        Start query to insert activities to OWL File
        @param name:           Start date of query to be called
        @param user:           Username of person to query
    */
    //public void getActivityList(String name, String num, String user){
    public void updateActivityList(String name, String user){
        String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
        String updateString = prefix +
                "INSERT DATA { " +
                "onto:"+name+" rdf:type onto:Student ; " +
                "onto:hasFirstName '"+name+"' ; " +
                //"onto:hasStudentNumber '"+num+"' ;" +
                "onto:hasUsername '"+user+"'" +
                "}";
        new updateEndpoint().execute(updateString, updateEndpoint);
    }

    /**
     * Function:    deleteActivityFromList
     * ---------------------------------------
     * Used to delete activity
     *
     * @param   userName    The username of person to be delete activity from
     * @param   activityInfo        The name of activity to be deleted
     * @return  void
     */
    public void deleteActivityFromList(String userName, String activityInfo){
        this.user = userName;
        //firstDate = "2019-05-27T00:00:00"; can use 2 different dates for current day hours
        //firstDateOfNextRange = "2019-05-28T00:00:00";
        String deleteEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String deleteString = prefix +
                "DELETE WHERE { ?" + user +
                " onto:hasAction onto:" + activityInfo +
                ". onto:" + activityInfo + " ?p ?v}";
        new deleteEndpoint().execute(deleteString, deleteEndpoint);
    }

    /**
     Class: deleteEndpoint
     ---------------------------------------
     aSyncTask to query the OWL file. Deletes activity from the OWL File
     */
    protected class deleteEndpoint extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... deleteAndTarget)
        {

            UpdateRequest update = UpdateFactory.create(deleteAndTarget[0]);
            UpdateProcessor uexec = UpdateExecutionFactory.createRemote(update, deleteAndTarget[1]);
            uexec.execute();
            return "delete";
        }

        @Override
        protected void onPostExecute(String success)
        {
            //txt2.append(success + " ");
        }
    }


}