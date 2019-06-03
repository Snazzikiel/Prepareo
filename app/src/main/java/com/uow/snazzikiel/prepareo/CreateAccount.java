package com.uow.snazzikiel.prepareo;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateAccount extends AppCompatActivity {
    private static final String TAG = "CreateAccount";
    Button btRegister;
    ArrayList<accountData> accountList;

    Button checkButton;
    TextView checkText;
    EditText userCheckBox;
    String prefix;

    TextView tvFirstName;
    TextView tvLastName;
    TextView tvUserName;
    TextView tvBirthday;
    TextView tvEmail;
    TextView tvPassword;
    TextView tvPassword2;

    String fName;
    String lName;
    String userName;
    String bday;
    String email;
    String pw1;
    String pw2;
    Boolean userNameFound = false;

    DatePickerDialog.OnDateSetListener mDateSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_account);
        accountList = new ArrayList<>();

        btRegister = (Button)findViewById(R.id.create_btn_Register);
        tvFirstName = (TextView)findViewById(R.id.create_acc_firstName);
        tvLastName = (TextView)findViewById(R.id.create_acc_LastName);
        tvUserName = (TextView)findViewById(R.id.create_acc_userName);
        tvBirthday = (TextView)findViewById(R.id.create_acc_Birthday);
        tvEmail = (TextView)findViewById(R.id.create_acc_Email);
        tvPassword = (TextView)findViewById(R.id.create_Password);
        tvPassword2 = (TextView)findViewById(R.id.create_Password2);


        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateAccount.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSet,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "-" + month + "-" + year);

                String date = year + "-" + month + "-" + day;
                tvBirthday.setText(date);
            }
        };

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = tvFirstName.getText().toString().trim();
                lName = tvLastName.getText().toString().trim();
                userName = tvUserName.getText().toString().trim();
                //bday = tvBirthday.getText().toString().trim() + "T00:00:00";
                bday = "2019-06-03T00:00:00";
                email = tvEmail.getText().toString().trim();
                pw1 = tvPassword.getText().toString().trim();
                pw2 = tvPassword2.getText().toString().trim();

                verifyFields(v);
            }
        });

    }

    public void verifyFields(View v){

        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(bday) || TextUtils.isEmpty(userName) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(lName) ||
                TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2)){
            Toast.makeText(getApplicationContext(), "Fields cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else if (!pw1.equals(pw2)){
            Toast.makeText(getApplicationContext(), "Both password fields must match.",
                    Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Verifying..!",
                    Toast.LENGTH_SHORT).show();
            accountVerification p = new accountVerification();
            userNameFound = p.verifyCreateUser(fName, lName, userName, bday, pw1);
            if(userNameFound){
                Toast.makeText(getApplicationContext(), "User name already exists.",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                accountData user = new accountData(fName, lName, userName, email, bday, pw1);
                accountList.add(user);

                Toast.makeText(getApplicationContext(), "Success!",
                        Toast.LENGTH_SHORT).show();
                saveData();
                startActivity(new Intent(CreateAccount.this, Dashboard.class));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
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
        Type type = new TypeToken<ArrayList<accountData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
        }
    }
}