package com.uow.snazzikiel.prepareo;

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
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.function.library.date;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class page extends AppCompatActivity
{
    private static final String TAG = "owlCheck";
    Button selectButton, addButton, deleteButton, editButton;
    TextView txt1, txt2;
    EditText nameBox, numBox, userBox;
    String prefix;

    //Map<String, List> owlData = new HashMap<>();
    HashMap<String,ArrayList<String>> menu = new HashMap<String,ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);

        prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

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
                String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
                String queryString = prefix +
                        "SELECT ?cat ?sub WHERE { " +
                        "?cat rdfs:subClassOf onto:Action . " +
                        "?sub rdfs:subClassOf ?cat " +
                        "}";
                new queryEndpoint().execute(queryString, queryEndpoint, "cat", "sub");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {
                String name, num, user;
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
                new updateEndpoint().execute(updateString, updateEndpoint);
            }
        });

        /*deleteButton.setOnClickListener(new View.OnClickListener()
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
        });*/

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {
                String user;
                String subString;
                user = userBox.getText().toString();
                subString = nameBox.getText().toString();

                String firstDateOfWeek = "2019-05-27T00:00:00";
                String firstDayOfNextWeek = "2019-06-03T00:00:00";
                String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
                String queryString = prefix +
                        "SELECT DISTINCT ?person ?action ?start ?end WHERE { " +
                        "?person onto:hasAction ?a ;" +
                        "onto:hasUsername '" + user + "' ." +
                        "?action onto:hasDuration ?duration ." +
                        "?duration onto:hasStartTime ?start ; "+
                        "onto:hasEndTime ?end ." +
                        "FILTER (?start >= '" + firstDateOfWeek + "'^^xsd:dateTime) ." +
                        "FILTER (?start < '" + firstDayOfNextWeek + "'^^xsd:dateTime) ." +
                        "FILTER regex(str(?action), '" + subString + "') ." +
                        "}";
                new queryEndpointQuota().execute(queryString, queryEndpoint, "person", "action", "start", "end");
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
            saveOwl();
        }
    }

    protected class queryEndpointQuota extends AsyncTask<String, String, Long>
    {
        @Override
        protected Long doInBackground(String... queryTargetVars)
        {
            Query sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);
            long totalMiniutes = 0;
            try
            {
                ResultSet results = qexec.execSelect();
                for(;results.hasNext();)
                {
                    QuerySolution soln = results.nextSolution();
                    RDFNode startTime = soln.get(queryTargetVars[4]);
                    RDFNode endTime = soln.get(queryTargetVars[5]);

                    String startHours = startTime.asLiteral().getLexicalForm();
                    String endHours = endTime.asLiteral().getLexicalForm();

                    startHours = StringUtils.substringAfterLast(startHours, "T");
                    endHours = StringUtils.substringAfterLast(endHours, "T");

                    SimpleDateFormat dif = new SimpleDateFormat("HH:mm:ss");
                    Date parsedStart = dif.parse(startHours);
                    Date parsedEnd = dif.parse(endHours);

                    long milSec = parsedEnd.getTime() - parsedStart.getTime();
                    long min = TimeUnit.MILLISECONDS.toMinutes(milSec);
                    totalMiniutes += min;

                    //testing
                    RDFNode name = soln.get(queryTargetVars[2]);
                    RDFNode action = soln.get(queryTargetVars[3]);
                    name.asResource().getLocalName();
                    action.asResource().getLocalName();
                    Log.i(TAG, name+ "   " + action);
                    Log.i(TAG, startHours+ "   " + endHours);
                    Log.i(TAG, String.valueOf(min));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } finally
            {
                qexec.close();
            }
            return totalMiniutes;
        }

        @Override
        protected void onPostExecute(Long total)
        {
            Log.i(TAG, String.valueOf(total));
        }
    }

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
            txt2.append(success + " ");
        }
    }

    public void saveOwl() {
        SharedPreferences sharedPreferences = getSharedPreferences("aSyncData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(menu);
        editor.putString("aSyncOwlData", json);
        editor.apply();
    }
}