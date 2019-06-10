package com.uow.snazzikiel.prepareo;

/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CalendarView;
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

/**
    Class:   EnrolmentRecord
    ---------------------------------------
    Obsolete class - NOT USED
*/
public class EnrolmentRecord extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "enrolmentCheck";

    List<enrolmentData> rowItems = new ArrayList<enrolmentData>();
    ViewGroup container;
    ListView myList;

    enrolmentData objBlank;

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addNote;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enrolment Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_enrolment_record);

        myList = (ListView) findViewById(R.id.enrolment_main_list);

        createItems(null);
        loadData1();


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
                //startActivityForResult(myIntent, 0);

            }
        });

        addNote = (FloatingActionButton) findViewById(R.id.btn_enrolment_add);
        addNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupMethod(null);

            }
        });


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enrolmentData item = (enrolmentData) parent.getItemAtPosition(position);
                popupMethod(null);

            }
        });

        myList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(position);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Toast.makeText(getApplicationContext(), "" + rowItems.get(position).getBachelor(),
                Toast.LENGTH_SHORT).show();
    }

    public void createItems(enrolmentData note1) {

        Log.i(TAG, "addEnrolment");
        if (note1 != null){
            rowItems.add(note1);
        }

        enrolmentAdapter adapter = new enrolmentAdapter(this, rowItems) {
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
        saveData1();
    }

    public void popupMethod(enrolmentData enrolItem){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.enrolment_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.enrolment_popup, null);

        popUp = new PopupWindow(container, (int)(width*0.80),(int)(height*0.80), true);
        popUp.showAtLocation(conLayout, Gravity.NO_GRAVITY, (int)(width*0.10), (int)(height*0.10));

        container.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                popUp.dismiss();
                return true;
            }
        });

        if (enrolItem != null){
            EditText etBach = (EditText)container.findViewById(R.id.ed_enrolment_Bachelor);
            EditText etMajor = (EditText)container.findViewById(R.id.ed_enrolment_Major);

            etBach.setText(enrolItem.getBachelor());
            etMajor.setText(enrolItem.getMajor());
        }

        btnClose = (Button) container.findViewById(R.id.enrolment_btn_close);
        btnSave = (Button) container.findViewById(R.id.enrolment_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etBach = (EditText)container.findViewById(R.id.ed_enrolment_Bachelor);
                EditText etMajor = (EditText)container.findViewById(R.id.ed_enrolment_Major);

                String bach = etBach.getText().toString();
                String major = etMajor.getText().toString();

                if (bach.isEmpty()){
                    Log.i(TAG, "bach: " + bach);
                    enrolmentData newItem = new enrolmentData(bach, major);

                    createItems(newItem);
                }

                saveData1();
                popUp.dismiss();
            }
        });
    }

    public void deleteItem(int iPosition){
        Log.i(TAG, "deleteEnrolment");
        rowItems.remove(iPosition);
        enrolmentAdapter adapter = new enrolmentAdapter(this, rowItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                // Set a background color for ListView regular row/item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                return view;
            }
        };
        saveData1();
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void saveData1() {
        SharedPreferences sharedPreferences = getSharedPreferences("EnrolmentRecord", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString(getString(R.string.enrolmentrecord_savedata), json);
        editor.apply();
    }

    public void loadData1() {

        SharedPreferences sharedPreferences = getSharedPreferences("EnrolmentRecord", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.enrolmentrecord_savedata), null);
        Type type = new TypeToken<ArrayList<enrolmentData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

}
