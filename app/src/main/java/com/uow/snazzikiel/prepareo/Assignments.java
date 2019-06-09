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

/*
    Class:   Assignments
    ---------------------------------------
    Get list of assignments loaded to the assigned subject

    rowItems:        List of items in primary list object
    subjectCode:     Subject passed through intents
*/
public class Assignments extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "assignmentCheck";
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

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve items passed in to a string.
        Intent thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");
        String sItemPosition = thisIntent.getStringExtra("subjectPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectCode + " - Assignments");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_assignments);

        myList = (ListView) findViewById(R.id.assignments_main_list);

        //Load saved data
        loadData();
        //add fake data to reload list for user screen
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

        registerForContextMenu(myList);
    }

    /*
        Function:   onItemClick
        ---------------------------------------
        Default function for action when item is pressed

        parent:     Parent variable to include adapter view
        view:       Current activity view
        position:   Position of item pressed by user
    */
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

    /*
        Function:   popupMethod
        ---------------------------------------
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        assignItem:     (assignmentsData)Object Information retrieved from class.

        TO DO: Input assignment data in to OWL file, create query and post data
    */
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

    /*
        Function:   editItem
        ---------------------------------------
        Method to bring a pop up for user to enter data. Fill items with data that has been
        previously entered. Re-save data with new items entered. USED TO EDIT ASSIGNMENTDATA OBJECTS

        assignItem:     (assignmentData)Object Information retrieved from class
        itemPosition:   integer of Item Position selected

        TO DO: Write query to update/input data in to OWL file
    */
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

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    /*
        Function:   createAssignment
        ---------------------------------------
        Used to add an item to a list. Add new object in to local storage data

        assignment1:    (assignmentsData)New object to be inserted in to list and inserted in to
                        saved object.
    */
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

    /*
        Function:   deleteItem
        ---------------------------------------
        Used to delete an item from the List. Deletes off local storage data also

        iPosition:    Position of list item clicked
    */
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

    /*
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "assignmentData" SharedPreference to access.
    */
    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("assignmentData" + subjectCode, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString((getString(R.string.assignments_savedata) + subjectCode), json);
        editor.apply();
    }

    /*
        Function:   loadData
        ---------------------------------------
        Used to retrieve the loadSubjects object to the local android device.
        Use a saveData function to call "assignmentData" SharedPreference to overwrite.
    */
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

    /*
        Function:   onCreateContextMenu
        ---------------------------------------
        Create menu object when user holds down on a list item
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.subjects_menu, menu);
    }

    /*
        Function:   onContextItemSelected
        ---------------------------------------
        Call menu and action each option

        MenuItem:       Menu taken from Menu in Res
    */
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


