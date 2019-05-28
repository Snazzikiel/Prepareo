package com.uow.snazzikiel.prepareo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class subjectGoals extends Goals {

    private static final String TAG = "stateCheck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set title with items passed
        Intent thisIntent = getIntent();
        subjectCode = thisIntent.getStringExtra("subjectCode");
        String sItemPosition = thisIntent.getStringExtra("subjectPosition");
        //itemPosition = Integer.parseInt(sItemPosition);
        setTitle(subjectCode + " - Goals");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_subject_goals);

        myList = (ListView) findViewById(R.id.subject_main_list);

        //Add two test Subjects
        loadData();
        goalsData test = new goalsData("Goal 1", "12-10-2019");
        createItem(test);
        deleteItem(rowItems.size()-1);

        addGoals = (FloatingActionButton) findViewById(R.id.float_addsubject);
        addGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(null);
            }
        });

        /*myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goalsData noteItem = (goalsData) parent.getItemAtPosition(position);
                popupMethod(noteItem);
            }
        });*/

        myList.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteItem(position);
                Toast.makeText(getApplicationContext(), "Item Deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
