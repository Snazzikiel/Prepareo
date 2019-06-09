package com.uow.snazzikiel.prepareo;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/*
    Class:   CreateAccount
    ---------------------------------------
    Create new user data
*/
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

    /*
        Function:   onCreate
        ---------------------------------------
        Default function to create the context and instance for Android screen

        btLogin:        Login button
        btRegister:     Registration Button
        tvFirstName:    Text field for First Name
        tvLastName:     Text field for Last Name
        tvUserName:     Text field for Username
        tvBirthday:     Text field for Date of Birth
        tvEmail:        Text field for Email
        tvPassword:     Text field for Password
        tvPassword2:    Text field for Password2

        fName:          String for First Name TextView
        lName:          String for Last name TextView
        userName:       String for userName TextView
        bday:           String for birthday TextView
        email:          String for email TextView
        pw1:            String field for Password
        pw2:            String field for Password2

        userNameFound:  Boolean return of OWL File
        mDateSet:       DateSet picker fort Date of Birth field
    */
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

        //opens up a date selector when TextView for birthday is entered
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

        //Convert date selector input back to a string and place text in to tvBirthday
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


    /*
        Function:   verifyFields
        ---------------------------------------
        Function used to verify the data input, Check for blanks, if Password fields match
        and then query against the OWL file using accountVerification class

        fName:      First Name
        lName:      Last Name
        bday:       Date of Birth
        userName:   username entered
        email:      email address of user
        pw1:        Password entry 1
        pw2:        Password entry 2
    */
    public void verifyFields(View v){

        //check each field to see if there is blanks
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

            //Send query to OWL file to check if userName is found or not
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

    /*
        Function:   onOptionsItemSelected
        ---------------------------------------
        Default required function to include a back button arrow on the top of the page
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

    /*
        Function:   loadData
        ---------------------------------------
        Used to retrieve the accountList object to the local android device.
        Use a saveData function to call "createAccount" SharedPreference to overwrite.
    */
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