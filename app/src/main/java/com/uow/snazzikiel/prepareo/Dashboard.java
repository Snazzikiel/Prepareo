package com.uow.snazzikiel.prepareo;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    Button goNotification;
    Button goGoals;
    Button goSubject;
    Button goStat;
    Button goOWL;
    Button goCalendar;
    ImageView imgExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //button definition
        goNotification = (Button)findViewById(R.id.dashboard_button_notifications);
        goGoals = (Button)findViewById(R.id.dashboard_button_goals);
        goSubject = (Button)findViewById(R.id.dashboard_button_subject);
        goStat = (Button)findViewById(R.id.dashboard_button_statistics);
        goCalendar= (Button)findViewById(R.id.dashboard_button_calendar);
        goOWL = (Button)findViewById(R.id.dashboard_button_ontology);

        imgExit = (ImageView)findViewById(R.id.dashboard_logo);

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                finish();
                System.exit(0);
            }
        });

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
                //startActivity(new Intent(Dashboard.this, Subjects.class));
                startActivity(new Intent(Dashboard.this, EnrolmentRecord.class));
            }
        });


        goStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Subjects.class));
                //Toast.makeText(getApplicationContext(), "Invalid Click!",Toast.LENGTH_SHORT).show();
            }
        });

        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Calendar.class));
            }
        });

        goOWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Profile.class));
            }
        });



    }
}
