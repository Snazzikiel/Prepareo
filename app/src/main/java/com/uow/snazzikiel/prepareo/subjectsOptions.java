package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class subjectsOptions extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "stateCheck";
    List<subjectsData> rowItems = new ArrayList<subjectsData>();

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addSubj;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_subjects_options);

        myList = (ListView) findViewById(R.id.assignments_main_list);

        //Add two test Subjects
        subjectsData subject = new subjectsData("Assignments", null);
        createSubject(subject);

        subject = new subjectsData("Goals", null);
        createSubject(subject);

        subject = new subjectsData("Statistics", null);
        createSubject(subject);

        subject = new subjectsData("Notifications", null);
        createSubject(subject);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                subjectsData subjItem = (subjectsData) parent.getItemAtPosition(position);
                if (subjItem.getCourseName() == "Notifications") {
                    Intent myIntent = new Intent(getApplicationContext(), notifications.class);
                    startActivityForResult(myIntent, 0);
                }

                if (subjItem.getCourseName() == "Assignments"){
                    Intent myIntent = new Intent(getApplicationContext(), Assignments.class);
                    startActivityForResult(myIntent, 0);
                }

                if (subjItem.getCourseName() == "Goals"){
                    Intent myIntent = new Intent(getApplicationContext(), Goals.class);
                    startActivityForResult(myIntent, 0);
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String subjName = rowItems.get(position).getCourseName();
        Toast.makeText(getApplicationContext(), "" + subjName,
                Toast.LENGTH_SHORT).show();
    }

    public void createSubject(subjectsData subject1) {

        Log.i(TAG, "addSubject");
        rowItems.add(subject1);


        subjectsAdapter adapter = new subjectsAdapter(this, rowItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Subjects.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}


