package com.uow.snazzikiel.prepareo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "stateCheck";
    Button btLogin, btFacebook, goRegister;
    EditText txtLogin, txtPassword;
    TextView tvHint;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        btLogin = (Button)findViewById(R.id.login_btn_login);
        txtLogin = (EditText)findViewById(R.id.login_email);
        txtPassword = (EditText)findViewById(R.id.login_password);
        goRegister = (Button)findViewById(R.id.noAccount);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginPage.this, Dashboard.class));
                Toast.makeText(getApplicationContext(),
                        "Success!...",Toast.LENGTH_SHORT).show();
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
