package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Adam
 ***********************************************/

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

/**
    Class:   Assignments
    ---------------------------------------
    Get list of assignments loaded to the assigned subject. This class has undergone a
     major method change and overhaul. Due to lack of due, variables and functions may be miscorrectly
     named or obsolete. Review functions carefully until further updated.

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

    /**
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_assignments);
        super.onCreate(savedInstanceState);
        //Retrieve items passed in to a string.
        Intent thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");
        setTitle(subjectCode + " - Assignments");

        //Load saved data
        loadData();

        myList = (ListView) findViewById(R.id.assignments_main_list);

        addAssign = (FloatingActionButton) findViewById(R.id.float_addAssignments);
        addAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(0);
            }
        });

        //load list
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

        //line to activate menus
        registerForContextMenu(myList);
    }

    /**
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
    }

    /**
        Function:   popupMethod
        ---------------------------------------
        Method forwards to assignmentInfo for data to be entererd in to local objects

        @param iPosition    position of clicked item
    */
    public void popupMethod(int iPosition) {
        Intent myIntent = new Intent(getApplicationContext(), AssignmentInfo.class);
        myIntent.putExtra("subjectCode", subjectCode);
        startActivityForResult(myIntent, 0);
    }

    /**
        Function:   editItem
        ---------------------------------------
        Method forwards to assignmentInfo for data to be entererd in to local objects

        @param itemPosition    position of clicked item
    */
    public void editItem(final int itemPosition) {
        Intent myIntent = new Intent(getApplicationContext(), AssignmentInfo.class);
        myIntent.putExtra("subjectCode", subjectCode);
        myIntent.putExtra("assignmentName", rowItems.get(itemPosition).getAssignmentName());
        myIntent.putExtra("subjectPos", itemPosition);
        startActivityForResult(myIntent, 0);
    }

    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    /**
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

    /**
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

    /**
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

    /**
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

    /**
        Function:   onContextItemSelected
        ---------------------------------------
        Call menu and action each option

        item:       Menu taken from Menu in Res
    */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.subjects_changeDetails:
                editItem(index);
                return true;
            case R.id.subjects_deleteSubject:
                deleteItem(index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}


