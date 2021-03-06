package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Lachlan, Connor
 ***********************************************/
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
    Class:   goalsTabOne
    ---------------------------------------
    Tab1 of Goals - Personal goals loaded by user
*/
public class goalsTabOne extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "Goals-Tab1Fragment";

    List<goalsData> rowItems = new ArrayList<goalsData>();

    View popupView;

    //popup Window
    PopupWindow popUp;
    FloatingActionButton addGoals;
    LayoutInflater layoutInf;
    ConstraintLayout conLayout;
    Button btnClose;
    Button btnSave;
    ViewGroup container;
    ListView myList;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goals_tab1,container,false);

        myList = (ListView) view.findViewById(R.id.goals_main_list);

        //Add two test Subjects
        loadData();
        goalsData goal = new goalsData("Goal 1", "10-01-2019");
        createItem(goal);
        deleteItem(rowItems.size()-1);

        addGoals = (FloatingActionButton) view.findViewById(R.id.float_addGoals);
        addGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMethod(null, v);
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //Intent myIntent = new Intent(getApplicationContext(), subjectsOptions.class);
                //startActivityForResult(myIntent, 0);
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                deleteItem(pos);
                goalsData goal = new goalsData("Goal 1", "10-01-2019");
                createItem(goal);
                deleteItem(rowItems.size()-1);

                return true;
            }
        });




        return view;
    }

    /**
        Function:   onItemClick
        ---------------------------------------
        Default function for action when item is pressed

        @param parent:     Parent variable to include adapter view
        @param view:       Current activity view
        @param position:   Position of item pressed by user
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

            editItem(rowItems.get(position), view);
    }

    /**
        Function:   editItem
        ---------------------------------------
        Method to bring a pop up for user to enter data. Fill items with data that has been
        previously entered. Re-save data with new items entered.

        @param assignItem:     (goalsData)Object Information retrieved from class

        TO DO: Write query to update/input data in to OWL file
    */
    public void editItem(goalsData assignItem, View v) {
        Log.i(TAG, "goalsPopup");
        DisplayMetrics dm = new DisplayMetrics();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popupView = getLayoutInflater().inflate(R.layout.goals_popup, null);
        popUp = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        int location[] = new int[2];
        v.getLocationOnScreen(location);
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.45));

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        EditText etTitle = (EditText) popupView.findViewById(R.id.et_goals_title);
        EditText etDate = (EditText) popupView.findViewById(R.id.et_goals_dueDate);

        etTitle.setText(assignItem.getGoalTitle());
        etDate.setText(assignItem.getGoalDueDate());


        btnSave = (Button) popupView.findViewById(R.id.goals_btn_save);
        btnClose = (Button) popupView.findViewById(R.id.goals_btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etTitle = (EditText) popupView.findViewById(R.id.et_goals_title);
                EditText etDate = (EditText) popupView.findViewById(R.id.et_goals_dueDate);

                String title = etTitle.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)){
                    Toast.makeText(getContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                } else {
                    date += "%";
                    goalsData newGoal = new goalsData(title, date);
                    createItem(newGoal);
                }
                Log.i(TAG, "goalsAdd");
                popUp.dismiss();
            }
        });
    }

    /**
        Function:   popupMethod
        ---------------------------------------
        Method to bring a pop up for user to enter data. Used to fill out information
        and save it in to the object for list creation.

        assignItem:     (goalsData)Object Information retrieved from class.

        TO DO: Input data in to OWL file, create query and post data
    */
    public void popupMethod(goalsData assignItem, View v) {
        Log.i(TAG, "goalsPopup");
        DisplayMetrics dm = new DisplayMetrics();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popupView = getLayoutInflater().inflate(R.layout.goals_popup, null);
        popUp = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        int location[] = new int[2];
        v.getLocationOnScreen(location);
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, (int) (width * 0.10), (int) (height * 0.45));

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUp.dismiss();
                return true;
            }
        });

        if (assignItem != null) {
            EditText etTitle = (EditText) popupView.findViewById(R.id.et_goals_title);
            EditText etDate = (EditText) popupView.findViewById(R.id.et_goals_dueDate);

            etTitle.setText(assignItem.getGoalTitle());
            etDate.setText(assignItem.getGoalDueDate());
        }

        btnSave = (Button) popupView.findViewById(R.id.goals_btn_save);
        btnClose = (Button) popupView.findViewById(R.id.goals_btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "goalsDismiss");
                popUp.dismiss();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "goalsAdd");
                EditText etTitle = (EditText) popupView.findViewById(R.id.et_goals_title);
                EditText etDate = (EditText) popupView.findViewById(R.id.et_goals_dueDate);

                String title = etTitle.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(date)){
                    date += "%";
                    goalsData newGoal = new goalsData(title, date);
                    createItem(newGoal);
                } else {
                    Toast.makeText(getContext(), "Unable to add blanks",
                            Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "goalsAdd");
                popUp.dismiss();
            }
        });
    }

    /**
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default function for back button
    */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /**
        Function:   createItem
        ---------------------------------------
        Used to add an item to a list. Add new object in to local storage data

        @param goals1:    (goalsData)New object to be inserted in to list and inserted in to
                        saved object.
    */
    public void createItem(goalsData goals1) {

        Log.i(TAG, "addGoals");
        if (goals1.getGoalDueDate() == null || goals1.getGoalDueDate() == "" ||
                goals1.getGoalTitle() == null || goals1.getGoalTitle() == "" ){
            return;
        }

        rowItems.add(goals1);

        goalsAdapter adapter = new goalsAdapter(getContext(), rowItems) {
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

    /**
        Function:   deleteItem
        ---------------------------------------
        Used to delete an item from the List. Deletes off local storage data also

        iPosition:    Position of list item clicked
    */
    public void deleteItem(int iPosition){
        Log.i(TAG, "deleteGoal");
        rowItems.remove(iPosition);
        goalsAdapter adapter = new goalsAdapter(getContext(), rowItems){
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
        Used to store the object to the local android device.
        Use a loadData function to call "goalDataTabOne" SharedPreference to access.
    */
    public void saveData() {
        Log.i(TAG, "saveGoal");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("goalData" + "TabOne", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rowItems);
        editor.putString((getString(R.string.goals_savedata) + "TabOne"), json);
        editor.apply();
    }

    /**
        Function:   loadData
        ---------------------------------------
        Used to retrieve the object to the local android device.
        Use a saveData function to call "goalDataTabOne" SharedPreference to overwrite.
    */
    public void loadData() {
        Log.i(TAG, "loadGoal");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("goalData"+ "TabOne", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString((getString(R.string.goals_savedata) + "TabOne"), null);
        Type type = new TypeToken<ArrayList<goalsData>>() {}.getType();
        rowItems = gson.fromJson(json, type);

        if (rowItems == null) {
            rowItems = new ArrayList<>();
        }
    }
}
