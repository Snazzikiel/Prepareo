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
    Button goProfile;
    Button goSubject;
    Button goStat;
    ImageView imgExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //button definition
        goNotification = (Button)findViewById(R.id.button_notifications);
        goProfile = (Button)findViewById(R.id.button_profile);
        goSubject = (Button)findViewById(R.id.button_subjects);
        goStat = (Button)findViewById(R.id.button_statistics);

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

        goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Profile.class));
            }
        });


        goSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(Dashboard.this, Subjects.class));
            }
        });


        goStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                //startActivity(new Intent(Dashboard.this, Statistics.class));
                Toast.makeText(getApplicationContext(),
                        "Invalid Click!",Toast.LENGTH_SHORT).show();
            }
        });



    }
}
