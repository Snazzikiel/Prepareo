package com.uow.snazzikiel.prepareo;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {
    private static final String TAG = "CreateAccount";
    Button btRegister;
    ArrayList<accountData> accountList;

    TextView tvFullName;
    TextView tvBirthday;
    TextView tvEmail;
    TextView tvAddress;
    TextView tvPassword;
    TextView tvPassword2;

    String fName;
    String bday;
    String email;
    String address;
    String pw1;
    String pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_account);
        accountList = new ArrayList<>();

        btRegister = (Button)findViewById(R.id.create_btn_Register);
        tvFullName = (TextView)findViewById(R.id.create_acc_fullName);
        tvBirthday = (TextView)findViewById(R.id.create_acc_Birthday);
        tvEmail = (TextView)findViewById(R.id.create_acc_Email);
        tvAddress = (TextView)findViewById(R.id.create_acc_ADDRESS);
        tvPassword = (TextView)findViewById(R.id.create_Password);
        tvPassword2 = (TextView)findViewById(R.id.create_Password2);

        fName = tvFullName.getText().toString().trim();
        bday = tvBirthday.getText().toString().trim();
        email = tvEmail.getText().toString().trim();
        address = tvAddress.getText().toString().trim();
        pw1 = tvPassword.getText().toString().trim();
        pw2 = tvPassword2.getText().toString().trim();


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(bday) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(address) ||
                        TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2)){
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else if (pw1.equals(pw2)){
                    Toast.makeText(getApplicationContext(), "Both password fields must match.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    accountData user = new accountData(fName, email, address, bday, pw1);
                    accountList.add(user);
                    saveData();
                    startActivity(new Intent(CreateAccount.this, Dashboard.class));
                }

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
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountList);
        editor.putString(getString(R.string.account_savedata), json);
        editor.apply();
    }

    public void loadData( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("createAccount", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.account_savedata), null);
        Type type = new TypeToken<ArrayList<notificationData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
        }
    }
}
