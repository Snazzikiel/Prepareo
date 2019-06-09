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

import org.apache.log4j.PropertyConfigurator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
    Class:   LoginPage
    ---------------------------------------
    Login to application - verify data from OWL File
*/
public class LoginPage extends AppCompatActivity {

    private static final String TAG = "loginCheck"; //used to check logs on Login SCreen

    Button btLogin, goRegister;
    EditText txtLogin, txtPassword;
    boolean verified = false;

    List<accountData> accountList; //store user logged in data

    String login;
    String pw1;

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen

        btLogin:        Login button
        goRegister:     Registration Button
        txtLogin:       Text field for Login
        txtPassword:    Text field for Password
        verified:       Result of asynctask OWL query
        accountList:    List used to create and store user data after successful login

        login:          String used to store txtLogin field
        pw1:            String used to store txtPassword field
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create a log file in the directory if not already exists - maven requires a local log file
        //(this log file will never be used by this program)
        Properties prop = new Properties();
        prop.setProperty("log4j.rootLogger", "WARN");
        PropertyConfigurator.configure(prop);


        super.onCreate(savedInstanceState);

        //initialise the variables declaraed
        setContentView(R.layout.activity_login_page);
        btLogin = (Button)findViewById(R.id.login_btn_login);
        txtLogin = (EditText)findViewById(R.id.login_email);
        txtPassword = (EditText)findViewById(R.id.login_password);
        goRegister = (Button)findViewById(R.id.noAccount);

        //create a few user account list
        accountList = new ArrayList<>();
        //overwrite saved accountlist File with BLANK for fresh login details entered
        saveData();

        //Once login button is pressed - check login details
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = txtLogin.getText().toString().trim();
                pw1 = txtPassword.getText().toString().trim();
                verifyData();
            }
        });

        //Go to create account registration page
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                startActivity(new Intent(LoginPage.this, CreateAccount.class));
            }
        });

        Log.i(TAG, "Create-Complete");
    }

    /*
        Function:   verifyData
        ---------------------------------------
        Function used to verify the data input, Check for blanks and then query against the OWL file
        using accountVerification class
    */
    private void verifyData(){
        //check if fields are empty
        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(pw1)){
            Toast.makeText(getApplicationContext(), "Fields cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else {
            //Check OWL File for correct login data
            Toast.makeText(getApplicationContext(), "Verifying..", Toast.LENGTH_SHORT).show();
            accountVerification p = new accountVerification();
            verified = p.verifyUser(login, pw1); //Checks the account verification class (Connects to OWL file)

            if (verified){//correct login data entered
                //add user session to memory
                //only require username to be saved in session memory to look at OWL database. This is used to create
                //a temporary account session
                accountData acc = new accountData(login, login, login, login, "2019-03-22T00:00:00", pw1 );
                accountList.add(acc);
                saveData();//save to session data

                Toast.makeText(getApplicationContext(),
                        "Success!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginPage.this, Dashboard.class));

            } else {//incorrect login data entered
                Toast.makeText(getApplicationContext(), "Incorrect Data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
        Function:   saveData
        ---------------------------------------
        Used to store the accountList object to the local android device.
        Use a loadData function to call "createAccount" SharedPreference to access.
    */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountList);
        editor.putString(getString(R.string.account_savedata), json);
        editor.apply();
    }

    //Temp testing functions - These are not required but left here for future use if needed
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