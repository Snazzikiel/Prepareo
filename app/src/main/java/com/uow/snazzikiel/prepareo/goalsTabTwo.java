package com.uow.snazzikiel.prepareo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;


public class goalsTabTwo extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "Goals-Tab2Fragment";


    List<goalsData> rowItems = new ArrayList<goalsData>();

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addGoals;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;
    CheckBox goalsChkBox;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goals_tab2,container,false);

        myList = (ListView) view.findViewById(R.id.goals_main_list2);
        //goalsChkBox = (CheckBox) findViewById((R.id.goals_checkbox));
        /*btnTEST = (Button) view.findViewById(R.id.btnTEST);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
            }
        });
        */

        //Add two test Subjects
        goalsData goal = new goalsData("Goal 4", "10-01-2019");
        createGoal(goal);

        goal = new goalsData("Goal 5", "11-01-2019");
        createGoal(goal);

        goal = new goalsData("Goal 6", "12-01-2019");
        createGoal(goal);

        addGoals = (FloatingActionButton) view.findViewById(R.id.float_addGoals2);
        addGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popupMethod(null);
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



        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String goalsTitle = rowItems.get(position).getGoalTitle();
        //Toast.makeText(view.getApplicationContext(), "" + goalsTitle,
        //        Toast.LENGTH_SHORT).show();
    }
/*
    public void popupMethod(assignmentsData assignItem) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.goals_main_layout);

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
                EditText etTitle = (EditText) container.findViewById(R.id.assignments_Name);
                EditText etDate = (EditText) container.findViewById(R.id.ed_assignments_Weight);

                String title = etTitle.getText().toString();
                String date = etDate.getText().toString();


                goalsData newGoal = new goalsData(title, date);

                createGoal(newGoal);
                popUp.dismiss();
            }
        });
    }*/

    public void createGoal(goalsData goal1) {

        Log.i(TAG, "addGoals");
        rowItems.add(goal1);


        goalsAdapter adapter = new goalsAdapter(getContext(), rowItems) {
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
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        return true;
    }

}