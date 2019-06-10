package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Alec
 * Assisted:		David
 ***********************************************/

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.util.concurrent.ExecutionException;

/**
    Class:   accountVerification
    ---------------------------------------
    Verification class used to check data against the OWL file when user is creating or logging in.
*/
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

    /**
        Function:   verifyUser
        ---------------------------------------
        Login data taken from LoginPage, username and password, then the OWL file is queried to see
        if that data exists and a boolean is then returned.

        userName:   Login Username entered on LoginPage
        pw1:        password entered taken on LoginPage
    */
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

    /**
        Function:   verifyCreateUser
        ---------------------------------------
        Account data taken from the create user page. This function creates the string to be queried against
        the OWL file

        @param      fName       first name
        @param      lName       last name
        @param      userName    username
        @param      bday        Date of Birth of user
        @param      pw1         Password entered
    */
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

    /**
        Class: userCheck
        ---------------------------------------
        This class is an AsyncTask, which means it will run in the background whilst the application is
        continuing to function. This class is used to query the OWL file to verify if a user exist, if a user
        does not exist, it will insert the entered data to the OWL file to create the user account.
    */
    protected class userCheck extends AsyncTask<String, String, Boolean>
    {
        @Override
        protected void onPreExecute() {
            //checkText.setText("!!!CHECKING USER!!!");
        }

        //function to run the query utilising aSync - The main function of this class.
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

        //Result of query to be actioned - Insert the data to the OWL file is does not exist.
        //Inserting the data will use another Async task class so two Async tasks will be running simultaneously.
        protected void onPostExecute(Boolean found)
        {
            if(found)
            {
                //checkText.setText("This account exists already");
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

    /**
        Class: userCheck
        ---------------------------------------
        This class is an AsyncTask, which means it will run in the background whilst the application is
        continuing to function. This class is used to insert the data to the OWL file after checking if it has
        checked if it exists or not.
    */
    protected class updateEndpoint extends AsyncTask<String, String, String>
    {
        /*
            Primary function inside the aSync Task. Used to process the query
        */
        @Override
        protected String doInBackground(String... updateAndTarget)
        {
            UpdateRequest update = UpdateFactory.create(updateAndTarget[0]);
            UpdateProcessor uexec = UpdateExecutionFactory.createRemote(update, updateAndTarget[1]);
            uexec.execute();
            //userNameFound = false;
            return "UPDATED";
        }

        //Do something with the result
        @Override
        protected void onPostExecute(String success)
        {
            //checkText.setText(success);
            //Toast.makeText(getApplicationContext(), success,
            //        Toast.LENGTH_SHORT).show();
        }
    }


    /**
         Class: passCheck
         ---------------------------------------
         This class is an AsyncTask, which means it will run in the background whilst the application is
         continuing to function. This class is used to ASK the query to verify the password
     */
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

