package com.uow.snazzikiel.prepareo;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class time extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
{
    Button startTime;
    Button endTime;
    TextView startTxt;
    TextView endTxt;

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
    String callback = "new";

    String prefix =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                    "PREFIX onto: <http://www.semanticweb.org/snoop/ontologies/2019/4/student-planner#>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.time);
        startTime = (Button) findViewById(R.id.btnStart);
        endTime = (Button) findViewById(R.id.btnEnd);
        startTxt = (TextView) findViewById(R.id.startText);
        endTxt = (TextView) findViewById(R.id.endText);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(callback.equals("initialized"))
                {
                    addStartTime = addEndTime;
                    activityType = "Exam"; //Get this from activity picked. auto to "Sleeping" if first entry
                    activityDate = activityType +":"+ uniqueSuffix;
                    activityInterval = activityDate +":INTERVAL";
                    DialogFragment timePicker = new timepick();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
                else
                {
                    DialogFragment timePicker = new timepick();
                    timePicker.show(getSupportFragmentManager(), "time picker");

                    DialogFragment timePicker2 = new timepick();
                    timePicker2.show(getSupportFragmentManager(), "time picker");
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute)
    {
        String hh = String.valueOf(hour);
        String mm = String.valueOf(minute);

        if (hour < 10)
        {
            hh = "0" + hh;
        }
        if (minute < 10)
        {
            mm = "0" + mm;
        }

        if (callback.equals("initialized"))
        {
            addEndTime = (theDate + "T" + hh + ":" + mm + ":00");

            String updateEndpoint = "http://220.158.191.18:8080/fuseki/student-ontology/update";

            String updateString = prefix +
                    "INSERT DATA { " +
                    "onto:"+activityInterval+" rdf:type onto:Interval ; " +
                    "onto:hasStartTime '" + addStartTime +"'^^xsd:dateTime ;" +
                    "onto:hasEndTime '" + addEndTime +"'^^xsd:dateTime ." +
                    "onto:"+activityDate+" rdf:type onto:" + activityType + " ;" +
                    "onto:hasDuration onto:"+activityInterval + " }";
            new time.updateEndpoint().execute(updateString, updateEndpoint);
        }
        if (callback.equals("new"))
        {
            activityType = "Sleeping";
            activityDate = activityType +":"+ uniqueSuffix;
            activityInterval = activityDate +":INTERVAL";
            addStartTime = (theDate + "T" + hh + ":" + mm + ":00");
            callback = "initialized";
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
        }
    }
}