package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Adam
 ***********************************************/

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
    Class:   AssignmentInfo
    ---------------------------------------
    Display information of selected Assignment to edit/create new
*/
public class AssignmentInfo extends AppCompatActivity {
    private static final String TAG = "assignmentInfo";

    List<assignmentsData> rowItems = new ArrayList<assignmentsData>();
    String subjectCode;
    String sAssName;

    Button btnSave;
    EditText etAssignmentName;
    EditText etAssignmentWeight;
    EditText etAssignmentMark;
    TextView tvAssignmentDueDate;
    DatePickerDialog.OnDateSetListener mDueDate;
    Intent thisIntent;
    int index;

    /**
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.assignments_popup);
        super.onCreate(savedInstanceState);

        loadData();
        //Set title with items passed
        thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");

        etAssignmentName = (EditText)findViewById(R.id.ed_assignments_Name);
        etAssignmentMark = (EditText)findViewById(R.id.ed_assignments_mark);
        etAssignmentWeight = (EditText)findViewById(R.id.ed_assignments_Weight);
        tvAssignmentDueDate = (TextView)findViewById(R.id.ed_assignments_dueDate);

        if (thisIntent.hasExtra("subjectPos")) {
            index = thisIntent.getIntExtra("subjectPos", 0);
            etAssignmentName.setText(rowItems.get(index).getAssignmentName());
            etAssignmentMark.setText(rowItems.get(index).getAssignmentMark());
            etAssignmentWeight.setText(rowItems.get(index).getAssignmentWeight());
            tvAssignmentDueDate.setText(rowItems.get(index).getAssignmentDueDate());

            sAssName = thisIntent.getStringExtra("assignmentName");
            setTitle(subjectCode + " (" + sAssName + ")");
        } else {
            setTitle(subjectCode + " - New");
        }

        tvAssignmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AssignmentInfo.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDueDate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDueDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "-" + month + "-" + year);

                String date = year + "-" + month + "-" + day;
                tvAssignmentDueDate.setText(date);
            }
        };

        btnSave = (Button) findViewById(R.id.assignments_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etAssignmentName.getText().toString().trim();
                String mark = etAssignmentMark.getText().toString().trim();
                String weight = etAssignmentWeight.getText().toString().trim();
                String dueDate = tvAssignmentDueDate.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mark) || TextUtils.isEmpty(weight) ||
                        TextUtils.isEmpty(dueDate)){
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {

                    if (thisIntent.hasExtra("subjectPos")) {
                        rowItems.get(index).setAssignmentName(name);
                        rowItems.get(index).setAssignmentWeight(weight);
                        rowItems.get(index).setAssignmentDueDate(dueDate);
                        rowItems.get(index).setAssignmentMark(mark);
                    } else {
                        assignmentsData newAss = new assignmentsData(name, weight, mark, dueDate);
                        rowItems.add(newAss);
                    }
                    saveData();
                    Intent myIntent = new Intent(getApplicationContext(), Assignments.class);
                    myIntent.putExtra("subjectCode", subjectCode);
                    startActivityForResult(myIntent, 0);

                }
            }
        });


    }

    /**
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

    /**
     Function:   onItemClick
     ---------------------------------------
     Default function for action when item is pressed

     parent:     Parent variable to include adapter view
     view:       Current activity view
     position:   Position of item pressed by user
     */
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(getApplicationContext(), "Hold item for menu",
                Toast.LENGTH_SHORT).show();
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
}
