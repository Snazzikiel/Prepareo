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

public class Subjects extends AppCompatActivity implements AdapterView.OnItemClickListener {


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
            setContentView(R.layout.activity_subjects);

            myList = (ListView) findViewById(R.id.subjects_main_list);

            //Add two test Subjects
            subjectsData subject = new subjectsData("Project", "CSCI321");
            createSubject(subject);

            subject = new subjectsData("Database Systems", "CSCI235");
            createSubject(subject);

            addSubj = (FloatingActionButton) findViewById(R.id.addSubjects);
            addSubj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMethod(null);
                }
            });

            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected item text from ListView
                    Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
                    startActivityForResult(myIntent, 0);

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

        public void popupMethod(subjectsData subjItem) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = dm.heightPixels;
            conLayout = (ConstraintLayout) findViewById(R.id.subjects_main_layout);

            layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            container = (ViewGroup) layoutInf.inflate(R.layout.subjects_popup, null);

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

                    String name = etName.getText().toString();
                    String code = etCode.getText().toString();


                    subjectsData newSubj = new subjectsData(name, code);

                    createSubject(newSubj);
                    popUp.dismiss();
                }
            });
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
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}


