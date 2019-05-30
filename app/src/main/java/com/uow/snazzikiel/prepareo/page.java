package com.uow.snazzikiel.prepareo;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import java.util.ArrayList;
import java.util.HashMap;

public class page extends AppCompatActivity
{
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
                        menu.put(key, tmpList);
                        key = cat;
                        tmpList = new ArrayList<>();
                        tmpList.add(sub);
                    }
                }
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
    /*protected class queryEndpoint extends AsyncTask<String, String, ArrayList<String>>
    {
        @Override
        protected ArrayList<String> doInBackground(String... queryTargetVars)
        {
            ArrayList<String> people = new ArrayList<String>();
            Query sparqlQuery = sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);

            try
            {
                ResultSet results = qexec.execSelect();
                for( ; results.hasNext() ; )
                {
                    String line = "";
                    QuerySolution soln = results.nextSolution();
                    for(int k = 2; k < queryTargetVars.length; k++)
                    {
                        RDFNode a = soln.get(queryTargetVars[k]);
                        line += queryTargetVars[k] + ": ";
                        if (a != null)
                        {
                            if (a.isURIResource())
                            {
                                String data = a.asNode().getLocalName();
                                line += (data + ", ");
                            }
                            else
                            {
                                String data = a.asLiteral().getLexicalForm();
                                line += (data + ", ");
                            }
                        }
                    }
                    people.add(line);
                }
            }
            finally
            {
                qexec.close();
            }
            return people;
        }

        @Override
        protected void onPostExecute(ArrayList<String> people)
        {
            txt1.setText("");
            for(String person : people)
            {
                txt1.append(person + "\n");
            }
        }
    }*/

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