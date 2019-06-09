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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
    Class:   Subjects
    ---------------------------------------
    Used to store goal objects
*/
public class Subjects extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "subjectCheck";
    List<subjectsData> rowItems = new ArrayList<subjectsData>();
    ArrayList<accountData> accountList;

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addSubj;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;
    TextView greeting;

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enrolment Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_subjects);

        myList = (ListView) findViewById(R.id.enrolment_main_list);

        loadData();
        subjectsData test = new subjectsData("s","s");
        createSubject(test);
        deleteSubject(rowItems.size()-1);

        addSubj = (FloatingActionButton) findViewById(R.id.btn_enrolment_add);
        addSubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(null);
            }
        });

        greeting = (TextView) findViewById(R.id.enrolment_tv_creditAvg);
        getProfile();


        Log.i(TAG, String.valueOf(accountList.size()));
        //Log.i(TAG, String.valueOf(accountList.get(1).getName()));
        Log.i(TAG, String.valueOf(accountList.get(0).getName()));
        if ( accountList.size() != 0){
            String temp = "Hello " + accountList.get(0).getName();
            greeting.setText(temp);
        } else {
            greeting.setText("Hello there ");
        }

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

        Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
        myIntent.putExtra("subjectCode", rowItems.get(position).getCourseCode());
        myIntent.putExtra( "subjectPosition", String.valueOf(position));
        startActivityForResult(myIntent, 0);
    }

    /*
        Function:   popupMethod
        ---------------------------------------
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        subjItem:     (subjectsData)Object Information retrieved from class.

        TO DO: Input subject data in to OWL file, create query and post data
    */
    public void popupMethod(subjectsData subjItem) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.enrolment_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.subjects_popup2, null);

        popUp = new PopupWindow(container, (int) (width * 0.80), (int) (height * 0.45), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.25));

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        if (subjItem != null) {
            EditText etName = (EditText) container.findViewById(R.id.subjects_Name);
            EditText etCode = (EditText) container.findViewById(R.id.subjects_Code);

            etName.setText(subjItem.getCourseName());
            etCode.setText(subjItem.getCourseCode());
        }

        btnClose = (Button) container.findViewById(R.id.subjects_btn_close);
        btnSave = (Button) container.findViewById(R.id.subjects_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText) container.findViewById(R.id.subjects_Name);
                EditText etCode = (EditText) container.findViewById(R.id.subjects_Code);

                String name = etName.getText().toString().trim();
                String code = etCode.getText().toString().trim();

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(code)){
                    subjectsData newSubj = new subjectsData(name, code);
                    createSubject(newSubj);
                    loadData();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }

                popUp.dismiss();
            }
        });
    }

    /*
        Function:   editItem
        ---------------------------------------
        Method to bring a pop up for user to enter data. Fill items with data that has been
        previously entered. Re-save data with new items entered.

        subjItem:     (subjectsData)Object Information retrieved from class
        itemPosition:   integer of Item Position selected

        TO DO: Write query to update/input data in to OWL file
    */
    public void editItem(subjectsData subjItem, final int itemPosition) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.enrolment_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.subjects_popup2, null);

        popUp = new PopupWindow(container, (int) (width * 0.80), (int) (height * 0.45), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.25));

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        EditText etName = (EditText) container.findViewById(R.id.subjects_Name);
        EditText etCode = (EditText) container.findViewById(R.id.subjects_Code);

        etName.setText(subjItem.getCourseName());
        etCode.setText(subjItem.getCourseCode());

        btnClose = (Button) container.findViewById(R.id.subjects_btn_close);
        btnSave = (Button) container.findViewById(R.id.subjects_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etName = (EditText) container.findViewById(R.id.subjects_Name);
                EditText etCode = (EditText) container.findViewById(R.id.subjects_Code);

                String name = etName.getText().toString().trim();
                String code = etCode.getText().toString().trim();

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(code)){
                    rowItems.get(itemPosition).setCourseCode(code);
                    rowItems.get(itemPosition).setCourseName(name);
                    saveData();
                    loadData();
                    subjectsData test = new subjectsData("s","s");
                    createSubject(test);
                    deleteSubject(rowItems.size()-1);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }

                popUp.dismiss();
            }
        });
    }

    /*
        Function:   createSubject
        ---------------------------------------
        Used to add an item to a list. Add new object in to local storage data

        subject1:    (subjectsData)New object to be inserted in to list and inserted in to
                        saved object.
    */
    public void createSubject(subjectsData subject1) {

        Log.i(TAG, "addSubject");
        if (subject1.getCourseName() == null || subject1.getCourseName() == "" ||
                subject1.getCourseCode() == null || subject1.getCourseCode() == "" ){
            return;
        }

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
        saveData();
    }

    /*
        Function:   deleteItem
        ---------------------------------------
        Used to delete an item from the List. Deletes off local storage data also

        iPosition:    Position of list item clicked
    */
    public void deleteSubject(int iPosition){
        Log.i(TAG, "deleteSubject");
        rowItems.remove(iPosition);
        subjectsAdapter adapter = new subjectsAdapter(this, rowItems){
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

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /*
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "subjectsData" SharedPreference to access.
    */
    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("subjectsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.subjects_savedata), json);
        editor.apply();
    }

    /*
        Function:   loadData
        ---------------------------------------
        Used to retrieve the loadSubjects object to the local android device.
        Use a saveData function to call "subjectsData" SharedPreference to overwrite.
    */
    public void loadData( ) {
        Log.i(TAG, "loadSubjects");
        SharedPreferences sharedPreferences = getSharedPreferences("subjectsData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.subjects_savedata), null);
        Type type = new TypeToken<ArrayList<subjectsData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

    /*
        Function:   getProfile
        ---------------------------------------
        Get user account of user logged in
    */
    public void getProfile( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<accountData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
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
                deleteSubject(index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}


