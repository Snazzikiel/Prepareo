package com.uow.snazzikiel.prepareo;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateAccount extends AppCompatActivity {
    private static final String TAG = "CreateAccount";
    Button btRegister;
    ArrayList<accountData> accountList;

    TextView tvFirstName;
    TextView tvLastName;
    TextView tvBirthday;
    TextView tvEmail;
    TextView tvPassword;
    TextView tvPassword2;

    String fName;
    String lName;
    String bday;
    String email;
    String pw1;
    String pw2;

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
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = year + "/" + month + "/" + day;
                tvBirthday.setText(date);
            }
        };

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                fName = tvFirstName.getText().toString().trim();
                lName = tvLastName.getText().toString().trim();
                bday = tvBirthday.getText().toString().trim() + "T00:00:00";
                email = tvEmail.getText().toString().trim();
                pw1 = tvPassword.getText().toString().trim();
                pw2 = tvPassword2.getText().toString().trim();

                Log.d(TAG, bday);

                if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(bday) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(lName) ||
                        TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2)){
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else if (!pw1.equals(pw2)){
                    Toast.makeText(getApplicationContext(), "Both password fields must match.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    accountData user = new accountData(fName, lName, email, bday, pw1);
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
        Type type = new TypeToken<ArrayList<accountData>>() {}.getType();
        accountList = gson.fromJson(json, type);

        if (accountList == null) {
            accountList = new ArrayList<>();
        }
    }
}
