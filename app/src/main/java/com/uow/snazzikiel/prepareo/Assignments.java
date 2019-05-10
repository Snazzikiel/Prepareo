package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Assignments extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "stateCheck";
    List<assignmentsData> rowItems = new ArrayList<assignmentsData>();

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addAssign;
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
        setContentView(R.layout.activity_assignments);

        myList = (ListView) findViewById(R.id.assignments_main_list);

        //Add two test Subjects
        assignmentsData assignment = new assignmentsData("Assignment 1", "10%");
        createAssignment(assignment);

        assignment = new assignmentsData("Assignment 2", "10%");
        createAssignment(assignment);

        assignment = new assignmentsData("Assignment 3", "10%");
        createAssignment(assignment);

        assignment = new assignmentsData("Assignment 4", "10%");
        createAssignment(assignment);

        addAssign = (FloatingActionButton) findViewById(R.id.float_addAssignments);
        addAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(null);
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
                //startActivityForResult(myIntent, 0);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String assignName = rowItems.get(position).getAssignmentName();
        Toast.makeText(getApplicationContext(), "" + assignName,
                Toast.LENGTH_SHORT).show();
    }

    public void popupMethod(assignmentsData assignItem) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.assignments_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.assignments_popup, null);

        popUp = new PopupWindow(container, (int) (width * 0.80), (int) (height * 0.45), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.10));

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        if (assignItem != null) {
            EditText etName = (EditText) container.findViewById(R.id.assignments_Name);
            EditText etWeight = (EditText) container.findViewById(R.id.ed_assignments_Weight);

            etName.setText(assignItem.getAssignmentName());
            etWeight.setText(assignItem.getAssignmentWeight());
        }

        btnClose = (Button) container.findViewById(R.id.assignments_btn_close);
        btnSave = (Button) container.findViewById(R.id.assignments_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText) container.findViewById(R.id.assignments_Name);
                EditText etWeight = (EditText) container.findViewById(R.id.ed_assignments_Weight);

                String name = etName.getText().toString();
                String weight = etWeight.getText().toString();


                assignmentsData newAssign = new assignmentsData(name, weight);

                createAssignment(newAssign);
                popUp.dismiss();
            }
        });
    }

    public void createAssignment(assignmentsData assignment1) {

        Log.i(TAG, "addAssignment");
        rowItems.add(assignment1);


        assignmentsAdapter adapter = new assignmentsAdapter(this, rowItems) {
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
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}


