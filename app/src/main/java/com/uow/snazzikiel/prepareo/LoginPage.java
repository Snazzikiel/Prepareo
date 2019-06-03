package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "stateCheck";
    Button btLogin, btFacebook, goRegister;
    EditText txtLogin, txtPassword;
    TextView tvHint;
    int counter = 3;
    boolean verified = false;

    List<accountData> accountList;

    String login;
    String pw1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        btLogin = (Button)findViewById(R.id.login_btn_login);
        txtLogin = (EditText)findViewById(R.id.login_email);
        txtPassword = (EditText)findViewById(R.id.login_password);
        goRegister = (Button)findViewById(R.id.noAccount);

        accountList = new ArrayList<>();
        //loadData();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = txtLogin.getText().toString().trim();
                pw1 = txtPassword.getText().toString().trim();
                verifyData();
                /*
                if (login == ""){
                    login = "user";
                }

                //add user session to memory
                accountData acc = new accountData(login, login, login, login, "2019-03-22T00:00:00", pw1 );
                accountList.add(acc);
                saveData();

                startActivity(new Intent(LoginPage.this, Dashboard.class));
                Toast.makeText(getApplicationContext(),
                        "Success!",Toast.LENGTH_SHORT).show();

/*
                ** UNLOCK THIS WHEN COMPLETE TO ADD TEST FOR CONTINUE BUTTON
                if(txtLogin.getText().toString().equals("user") &&

                        txtPassword.getText().toString().equals("user")) {
                    startActivity(new Intent(LoginPage.this, Dashboard.class));
                    Toast.makeText(getApplicationContext(),
                            "Success!...",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Incorrect Credentials",Toast.LENGTH_SHORT).show();

                    //counter--;
                    //if (counter == 0) {
                    //    btLogin.setEnabled(false);
                    //}
                }

                */
            }
        });

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(LoginPage.this, CreateAccount.class));
            }
        });

        Log.i(TAG, "onCreate");
    }

    private void verifyData(){
        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(pw1)){
            Toast.makeText(getApplicationContext(), "Fields cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Verifying..", Toast.LENGTH_SHORT).show();
            accountVerification p = new accountVerification();
            verified = p.verifyUser(login, pw1);
            if (verified){

                //add user session to memory
                accountData acc = new accountData(login, login, login, login, "2019-03-22T00:00:00", pw1 );
                accountList.add(acc);
                saveData();

                startActivity(new Intent(LoginPage.this, Dashboard.class));
                Toast.makeText(getApplicationContext(),
                        "Success!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Data!", Toast.LENGTH_SHORT).show();
                //}

                //Toast.makeText(getApplicationContext(), "12345", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountList);
        editor.putString(getString(R.string.account_savedata), json);
        editor.apply();
    }

    /* Retrieval not needed as database enquiry
    public void loadData( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<accountData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
        }
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
}