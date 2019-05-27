package com.uow.snazzikiel.prepareo;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {

    Button btRegister;
    ArrayList<accountData> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_account);
        accountList = new ArrayList<>();

        btRegister = (Button)findViewById(R.id.create_btn_Register);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                saveData();
                startActivity(new Intent(CreateAccount.this, LoginPage.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LoginPage.class);
        startActivityForResult(myIntent, 0);
        return true;

        /*switch (item.getItemId()) {
            case R.id.:
                finish();
                return true;
        }*/

        //return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountList);
        editor.putString(getString(R.string.account_savedata), json);
        editor.apply();
    }

    public void loadData( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
        }
    }
}
