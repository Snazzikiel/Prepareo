package com.uow.snazzikiel.prepareo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class accountVerification extends AppCompatActivity {
    private static final String TAG = "VerifyAccount";
    String prefix;

    String fName;
    String lName;
    String bday;
    String email;
    String pw1;

    boolean verified = false;

    public void verifyUser(String email, String pw1) {

        this.email = email;
        this.pw1 = pw1;

        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "ASK { " +
                "?user onto:hasUsername '" + email + "';" +
                "onto:hasPassword '" + pw1 + "'" +
                "}";
        new accountVerification.passCheck().execute(queryString, queryEndpoint);

        //return verified;
    }

    public void verifyCreateUser(String fName, String lName, String email, String bday, String pw1) {
        this.fName = fName;
        this.lName = lName;
        this.bday = bday;
        this.email = email;
        this.pw1 = pw1;

        Log.d(TAG, fName + lName + bday + email + pw1);

        prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

        String user = email;
        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "ASK { " +
                "?user onto:hasUsername '" + user +
                "'}";
        new accountVerification.userCheck().execute(queryString, queryEndpoint);

    }

    protected class userCheck extends AsyncTask<String, String, Boolean>
    {
        @Override
        protected void onPreExecute() {
            //checkText.setText("!!!CHECKING USER!!!");
        }
        @Override
        protected Boolean doInBackground(String... queryTargetVars) {
            Query sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);
            try
            {
                return qexec.execAsk();
            }
            finally
            {
                qexec.close();
            }
        }

        protected void onPostExecute(Boolean found)
        {
            if(found)
            {
                //checkText.setText("YOU IN HERE ALREADY");
                //verified = false;
            }
            else
            {
                String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
                String updateString = prefix +
                        "INSERT DATA { " +
                        "onto:"+email+" rdf:type onto:Student ; " +
                        "onto:hasFirstName '"+fName+"' ; " +
                        "onto:hasLastName '"+lName+"' ;" +
                        "onto:hasBirthday '" + bday + "'^^xsd:dateTime ;" +
                        "onto:hasUsername '"+email+"' ;" +
                        "onto:hasPassword '" + pw1 + "'" +
                        "}";
                new accountVerification.updateEndpoint().execute(updateString, updateEndpoint);
            }
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
            //verified = true;
            return "UPDATED";
        }
        @Override
        protected void onPostExecute(String success)
        {
            //checkText.setText(success);
            //Toast.makeText(getApplicationContext(), success,
            //        Toast.LENGTH_SHORT).show();
        }
    }

    protected class passCheck extends AsyncTask<String, String, Boolean>
    {
        @Override
        protected void onPreExecute() {
            //checkText.setText("...Looking for you");
        }
        @Override
        protected Boolean doInBackground(String... queryTargetVars) {
            Query sparqlQuery = QueryFactory.create(queryTargetVars[0]);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(queryTargetVars[1], sparqlQuery);
            try
            {
                return qexec.execAsk();
            }
            finally
            {
                qexec.close();
            }
        }
        @Override
        protected void onPostExecute(Boolean found)
        {
            if(found)
            {
                //verified = true;
                //checkText.setText("YOU HAVE LOGGED IN???");
            }
            else
            {
                //verified = false;
                //checkText.setText("WRONG!");
            }
        }
    }
}

