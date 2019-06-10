package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Alec
 ***********************************************/
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.OnRecyclerItemClickListener;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
    Class:   Profile
    ---------------------------------------
    ****NOT USED CLASS***
    print OWL File in to a dropdown list
*/
public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile Setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_profile);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final MultiLevelRecyclerView multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //List<profileData> itemList = (List<profileData>) recursivePopulateFakeData(0, 12);
        List<profileData> itemList = (List<profileData>) addFakeData(0, 12);

        profileAdapter myAdapter = new profileAdapter(this, itemList, multiLevelRecyclerView);

        multiLevelRecyclerView.setAdapter(myAdapter);

        //If you are handling the click on your own then you can
        // multiLevelRecyclerView.removeItemClickListeners();
        multiLevelRecyclerView.setToggleItemOnClick(false);

        multiLevelRecyclerView.setAccordion(false);

        //multiLevelRecyclerView.openTill(0,1,2,3);
    }


    profileData itemLevel0(String title, int arg){
        profileData item = new profileData(0);
        item.setText(String.format(Locale.ENGLISH, title, arg));
        return item;
    }

    profileData itemLevel1(String title, int arg){
        profileData item = new profileData(1);
        item.setText(String.format(Locale.ENGLISH, title, arg));
        return item;
    }

    profileData itemLevel2(String title, int arg){
        profileData item = new profileData(2);
        item.setText(String.format(Locale.ENGLISH, title, arg));
        return item;
    }

    private List<RecyclerViewItem> addMultiLevel(List<RecyclerViewItem> level0, profileData item1, profileData item2, profileData item3, profileData item4){
        //List<RecyclerViewItem> level0 = new ArrayList<>();
        List<RecyclerViewItem> level1 = new ArrayList<>();

        level1.add(item2);
        level1.add(item3);
        level1.add(item4);
        item1.addChildren(level1);
        level0.add(item1);

        return level0;
    }

    private List<RecyclerViewItem> addOneLevel(List<RecyclerViewItem> level0, profileData item1, profileData item2, profileData item3, profileData item4){
        //List<RecyclerViewItem> level0 = new ArrayList<>();
        List<RecyclerViewItem> level1 = new ArrayList<>();

        level0.add(item1);
        level0.add(item2);
        level0.add(item3);
        level0.add(item4);
        //item1.addChildren(level1);
        //level0.add(item1);

        return level0;
    }
    private List<?> addFakeData(int levelNumber, int depth) {
        List<RecyclerViewItem> itemList = new ArrayList<>();
        List<RecyclerViewItem> level0 = new ArrayList<>();
        List<RecyclerViewItem> level1 = new ArrayList<>();

        profileData item;
        profileData item1;
        profileData item2;
        profileData item3;
        profileData item4;

        item = itemLevel0("Action", 0);

        item1 = itemLevel1("Activities of Daily Living", 0);
        item2 = itemLevel2("Cleaning", 0);
        item3 = itemLevel2("Lawn Mowing", 1);
        item4 = itemLevel2("Learning to Drive", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));

        item1 = itemLevel1("Communication", 1);
        item2 = itemLevel2("International Phone Call", 0);
        item3 = itemLevel2("Domestic Phone Call", 1);
        item4 = itemLevel2("Mobile Phone Call", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));

        item1 = itemLevel1("Leisure", 2);
        item2 = itemLevel2("Social Media", 0);
        item3 = itemLevel2("Gardening", 1);
        item4 = itemLevel2("Dancing", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));

        item1 = itemLevel1("Travel", 3);
        item2 = itemLevel2("Walking", 0);
        item3 = itemLevel2("Cycling", 1);
        item4 = itemLevel2("Driving", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Assessment", 1);
        item1 = itemLevel1("Assignment", 0);
        item2 = itemLevel1("Final Exam", 1);
        item3 = itemLevel1("Lab Assessment", 2);
        item4 = itemLevel1("Tutorial Assessment", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Challenge", 2);
        item1 = itemLevel1("Culture Challenge", 0);
        item2 = itemLevel1("Accomidation Challenge", 1);
        item3 = itemLevel1("Communication Challenge", 2);
        item4 = itemLevel1("Health Challenge", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Cognition", 3);
        item1 = itemLevel1("Attention", 0);
        item2 = itemLevel1("Response", 1);
        item3 = itemLevel1("Self Awareness", 2);
        item4 = itemLevel1("Problem Solving", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Course", 4);
        item1 = itemLevel1("Bachelor of Computer Science", 0);
        item2 = itemLevel1("Master of Public Health Care", 1);
        item3 = itemLevel1("Graduate Certificate in Computer Science", 2);
        item4 = itemLevel1("Master of Health Informatics Course", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Goal", 4);
        item1 = itemLevel1("Career Goal", 0);
        item2 = itemLevel1("English Language Skill Development Goal", 1);
        item3 = itemLevel1("Graduation Goal", 2);
        item4 = itemLevel1("Task Related Goal", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Motivation", 4);
        item1 = itemLevel1("Self Determination", 0);
        item2 = itemLevel1("Self Development", 1);
        item3 = itemLevel1("Self Efficiency", 2);
        item4 = itemLevel1("Peer Related", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        item = itemLevel0("Outcome", 0);
        item1 = itemLevel1("Intangible Outcome", 0);
        item2 = itemLevel2("Reduced Cognitive Dissonance", 0);
        item3 = itemLevel2("Self Actualization", 1);
        item4 = itemLevel2("Self Efficiency Improvement", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));

        item1 = itemLevel1("Tangible Outcome", 1);
        item2 = itemLevel2("Attendance Outcome", 0);
        item3 = itemLevel2("Knowledge", 1);
        item4 = itemLevel2("Skill", 2);
        item.addChildren(addMultiLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Person", 4);
        item1 = itemLevel1("Student", 0);
        item2 = itemLevel1("Teaching Staff", 1);
        item3 = itemLevel1("Administration Staff", 2);
        item4 = itemLevel1("Other", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Plan", 4);
        item1 = itemLevel1("Career Plan", 0);
        item2 = itemLevel1("Exercise Plan", 1);
        item3 = itemLevel1("Family Plan", 2);
        item4 = itemLevel1("Study Plan", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Resource", 4);
        item1 = itemLevel1("Course Information", 0);
        item2 = itemLevel1("Social Support Resource", 1);
        item3 = itemLevel1("Study Resource", 2);
        item4 = itemLevel1("Subject Information", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Subject", 4);
        item1 = itemLevel1("CSCI862 - System Security", 0);
        item2 = itemLevel1("CSIT113 - Problem Solving", 1);
        item3 = itemLevel1("CSIT940 - Research Methods", 2);
        item4 = itemLevel1("MATH221 - Mathematics", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);

        level0 = new ArrayList<>();
        item = itemLevel0("Time", 4);
        item1 = itemLevel1("Due Time", 0);
        item2 = itemLevel1("Duration", 1);
        item3 = itemLevel1("End Time", 2);
        item4 = itemLevel1("Start Time", 3);
        item.addChildren(addOneLevel(level0, item1, item2, item3, item4));
        itemList.add(item);


        /*
        for (int i = 0; i < depth; i++) {
            profileData item = new profileData(levelNumber);
            item.setText(String.format(Locale.ENGLISH, title, i));
            //item.setSecondText(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if(depth % 2 == 0){
                item.addChildren((List<RecyclerViewItem>) recursivePopulateFakeData(levelNumber + 1, depth/2));
            }
            itemList.add(item);
        }
        */
        return itemList;
    }

    private List<?> recursivePopulateFakeData(int levelNumber, int depth) {
        List<RecyclerViewItem> itemList = new ArrayList<>();

        String title;
        switch (levelNumber){
            case 1:
                title = "PQRST %d";
                break;
            case 2:
                title = "XYZ %d";
                break;
            default:
                title = "ABCDE %d";
                break;
        }

        for (int i = 0; i < depth; i++) {
            profileData item = new profileData(levelNumber);
            item.setText(String.format(Locale.ENGLISH, title, i));
            //item.setSecondText(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if(depth % 2 == 0){
                item.addChildren((List<RecyclerViewItem>) recursivePopulateFakeData(levelNumber + 1, depth/2));
            }
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(myIntent, 0);
        return true;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
    }
}
