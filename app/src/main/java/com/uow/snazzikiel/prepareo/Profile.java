package com.uow.snazzikiel.prepareo;

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

        List<profileData> itemList = (List<profileData>) recursivePopulateFakeData(0, 12);

        profileAdapter myAdapter = new profileAdapter(this, itemList, multiLevelRecyclerView);

        multiLevelRecyclerView.setAdapter(myAdapter);

        //If you are handling the click on your own then you can
        // multiLevelRecyclerView.removeItemClickListeners();
        multiLevelRecyclerView.setToggleItemOnClick(false);

        multiLevelRecyclerView.setAccordion(false);

        //multiLevelRecyclerView.openTill(0,1,2,3);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
