package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Assignments extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "stateCheck";
    List<assignmentsData> rowItems = new ArrayList<assignmentsData>();
    String subjectCode;
    int itemPosition;

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

        //Set title with items passed
        Intent thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");
        String sItemPosition = thisIntent.getStringExtra("subjectPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectCode + " - Assignments");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_assignments);

        myList = (ListView) findViewById(R.id.assignments_main_list);

        //Add two test Subjects
        loadData();
        assignmentsData test = new assignmentsData("Assignment 1", "10%");
        createAssignment(test);
        deleteItem(rowItems.size()-1);

        addAssign = (FloatingActionButton) findViewById(R.id.float_addAssignments);
        addAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(null);
            }
        });

        /*myList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteItem(position);
                Toast.makeText(getApplicationContext(), "Item Deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        registerForContextMenu(myList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Toast.makeText(getApplicationContext(), "Hold item for menu",
                Toast.LENGTH_SHORT).show();

        /*Intent myIntent = new Intent(getApplicationContext(), AssignmentInfo.class);
        myIntent.putExtra("subjectCode", subjectCode);
        myIntent.putExtra( "assignmentName", rowItems.get(position).getAssignmentName());
        startActivityForResult(myIntent, 0);*/
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
            EditText etName = (EditText) container.findViewById(R.id.ed_assignments_Name);
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
                EditText etName = (EditText) container.findViewById(R.id.ed_assignments_Name);
                EditText etWeight = (EditText) container.findViewById(R.id.ed_assignments_Weight);

                String name = etName.getText().toString();
                String weight = etWeight.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(weight)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {
                    weight += "%";
                    assignmentsData newAssign = new assignmentsData(name, weight);
                    createAssignment(newAssign);
                }

                popUp.dismiss();
            }
        });
    }

    public void editItem(assignmentsData assignItem, final int itemPosition) {
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

        EditText etName = (EditText) container.findViewById(R.id.ed_assignments_Name);
        EditText etWeight = (EditText) container.findViewById(R.id.ed_assignments_Weight);

        etName.setText(assignItem.getAssignmentName());
        etWeight.setText(assignItem.getAssignmentWeight());


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
                EditText etName = (EditText) container.findViewById(R.id.ed_assignments_Name);
                EditText etWeight = (EditText) container.findViewById(R.id.ed_assignments_Weight);

                String name = etName.getText().toString();
                String weight = etWeight.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(weight)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {

                    rowItems.get(itemPosition).setAssignmentWeight(weight);
                    rowItems.get(itemPosition).setAssignmentName(name);
                    assignmentsData newAssign = new assignmentsData(name, weight);
                    createAssignment(newAssign);
                    deleteItem(rowItems.size() - 1);

                }

                popUp.dismiss();
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    public void createAssignment(assignmentsData assignment1) {

        Log.i(TAG, "addAssignment");
        if (assignment1.getAssignmentName() == null || assignment1.getAssignmentName() == "" ||
                assignment1.getAssignmentWeight() == null || assignment1.getAssignmentWeight() == "" ){
            return;
        }

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
        saveData();
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(this);
    }

    public void deleteItem(int iPosition){
        Log.i(TAG, "deleteAssignment");
        rowItems.remove(iPosition);
        assignmentsAdapter adapter = new assignmentsAdapter(this, rowItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };

        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this);
        saveData();
    }

    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("assignmentData" + subjectCode, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString((getString(R.string.assignments_savedata) + subjectCode), json);
        editor.apply();
    }

    public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("assignmentData" + subjectCode, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.assignments_savedata) + subjectCode), null);
        Type type = new TypeToken<ArrayList<assignmentsData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.subjects_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.subjects_changeDetails:
                editItem(rowItems.get(index), index);
                return true;
            case R.id.subjects_deleteSubject:
                editItem(rowItems.get(index), index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}


