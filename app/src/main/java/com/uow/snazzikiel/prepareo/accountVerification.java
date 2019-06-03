package com.uow.snazzikiel.prepareo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.util.concurrent.ExecutionException;

public class accountVerification extends AppCompatActivity {
    private static final String TAG = "VerifyAccount";
    final String prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
            "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    String fName;
    String lName;
    String bday;
    String userName;
    String pw1;




    public boolean verifyUser(String userName, String pw1) {

        this.userName = userName;
        this.pw1 = pw1;
        boolean check = false;

        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "ASK { " +
                "?user onto:hasUsername '" + userName + "';" +
                "onto:hasPassword '" + pw1 + "'" +
                "}";
        try {
            check = new passCheck().execute(queryString, queryEndpoint).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean verifyCreateUser(String fName, String lName, String userName, String bday, String pw1) {
        this.fName = fName;
        this.lName = lName;
        this.bday = bday;
        this.userName = userName;
        this.pw1 = pw1;
        boolean check = false;

        Log.d(TAG, fName + lName + bday + userName + pw1);

        String user = userName;
        String queryEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/query";
        String queryString = prefix +
                "ASK { " +
                "?user onto:hasUsername '" + user +
                "'}";
        try {
            check = new userCheck().execute(queryString, queryEndpoint).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return check;
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
                //userNameFound = true;
            }
            else
            {
                String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";
                String updateString = prefix +
                        "INSERT DATA { " +
                        "onto:"+userName+" rdf:type onto:Student ; " +
                        "onto:hasFirstName '"+fName+"' ; " +
                        "onto:hasLastName '"+lName+"' ;" +
                        "onto:hasBirthday '" + bday + "'^^xsd:dateTime ;" +
                        //"onto:hasEmail '" + email + "' ;" +
                        "onto:hasUsername '"+userName+"' ;" +
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
            //userNameFound = false;
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
        }
    }
}

