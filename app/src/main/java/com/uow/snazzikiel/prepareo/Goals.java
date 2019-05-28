package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
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


public class Goals extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "stateCheck";
    List<goalsData> rowItems = new ArrayList<goalsData>();
    String subjectCode = "";

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addGoals;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    private statisticsTabAdapter mGoals;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Goals");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_goals);

        Log.d(TAG, "goalsCreate.");

        mGoals = new statisticsTabAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.goals_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.goals_tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String goalsTitle = rowItems.get(position).getGoalTitle();
        Toast.makeText(getApplicationContext(), "" + goalsTitle,
                Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        statisticsTabAdapter adapter = new statisticsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new goalsTabOne(), getString(R.string.goals_tab1));
        adapter.addFragment(new goalsTabTwo(), getString(R.string.goals_tab2));
        viewPager.setAdapter(adapter);
    }

    public void popupMethod(goalsData assignItem) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.subject_main_layout);

        layoutInf = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInf.inflate(R.layout.subjectgoals_popup, null);

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
            EditText etName = (EditText) container.findViewById(R.id.ed_subjectgoals_Name);
            EditText etDate = (EditText) container.findViewById(R.id.ed_subjectgoals_dueDate);

            etName.setText(assignItem.getGoalTitle());
            etDate.setText(assignItem.getGoalDueDate());
        }

        btnClose = (Button) container.findViewById(R.id.subjectgoals_btn_close);
        btnSave = (Button) container.findViewById(R.id.subjectgoals_btn_save);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText) container.findViewById(R.id.ed_subjectgoals_Name);
                EditText etDate = (EditText) container.findViewById(R.id.ed_subjectgoals_dueDate);

                String name = etName.getText().toString();
                String weight = etDate.getText().toString();
                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(weight)){
                    weight += "%";
                    goalsData newGoal = new goalsData(name, weight);
                    createItem(newGoal);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }

                popUp.dismiss();
            }
        });
    }

    public void editItem(goalsData subjItem, final int itemPosition) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        conLayout = (ConstraintLayout) findViewById(R.id.subjects_main_layout);

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

        EditText etTitle = (EditText) container.findViewById(R.id.subjects_Name);
        EditText etDesc = (EditText) container.findViewById(R.id.subjects_Code);
        EditText etDate = (EditText) container.findViewById(R.id.subjects_Code);

        etTitle.setText(subjItem.getGoalTitle());
        etDesc.setText(subjItem.getGoalDescription());
        etDate.setText(subjItem.getGoalDueDate());

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

                EditText etTitle = (EditText) container.findViewById(R.id.et_goals_Title);
                EditText etDesc = (EditText) container.findViewById(R.id.et_goals_desc);
                EditText etDate = (EditText) container.findViewById(R.id.et_goals_date);

                String title = etTitle.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                String date = etDate.getText().toString().trim();

                if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(desc) || !TextUtils.isEmpty(date)){
                    rowItems.get(itemPosition).setGoalTitle(title);
                    rowItems.get(itemPosition).setGoalDescription(desc);
                    rowItems.get(itemPosition).setGoalDescription(date);
                    saveData();
                    loadData();
                    goalsData test = new goalsData("s","s", "s");
                    createItem(test);
                    deleteItem(rowItems.size()-1);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }

                popUp.dismiss();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void createItem(goalsData goals1) {

        Log.i(TAG, "addGoals");
        if (goals1.getGoalDueDate() == null || goals1.getGoalDueDate() == "" ||
                goals1.getGoalTitle() == null || goals1.getGoalTitle() == "" ){
            return;
        }

        rowItems.add(goals1);

        goalsAdapter adapter = new goalsAdapter(this, rowItems) {
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
        Log.i(TAG, "deleteGoal");
        rowItems.remove(iPosition);
        goalsAdapter adapter = new goalsAdapter(this, rowItems){
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
        Log.i(TAG, "saveGoal");
        SharedPreferences sharedPreferences = getSharedPreferences("goalData"+ subjectCode, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString((getString(R.string.goals_savedata) + subjectCode), json);
        editor.apply();
    }

    public void loadData() {
        Log.i(TAG, "loadGoal");
        SharedPreferences sharedPreferences = getSharedPreferences("goalData"+ subjectCode, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.goals_savedata) + subjectCode), null);
        Type type = new TypeToken<ArrayList<goalsData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }

}
