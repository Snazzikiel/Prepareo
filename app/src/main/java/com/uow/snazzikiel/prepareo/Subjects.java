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

public class Subjects extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "stateCheck";
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
        if ( accountList.size() != 0){
            String temp = "Hello " + accountList.get(0).getName();
            greeting.setText(temp);
        } else {
            greeting.setText("Hello there ");
        }

        registerForContextMenu(myList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
        myIntent.putExtra("subjectCode", rowItems.get(position).getCourseCode());
        myIntent.putExtra( "subjectPosition", String.valueOf(position));
        startActivityForResult(myIntent, 0);
    }

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

    public void saveData() {
        Log.i(TAG, "saveSubject");
        SharedPreferences sharedPreferences = getSharedPreferences("subjectsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.subjects_savedata), json);
        editor.apply();
    }

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

    public void getProfile( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
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
                deleteSubject(index);
                Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}


