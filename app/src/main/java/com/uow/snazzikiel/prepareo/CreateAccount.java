package com.uow.snazzikiel.prepareo;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CreateAccount extends AppCompatActivity {

    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_account);

        btRegister = (Button)findViewById(R.id.create_btn_Register);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
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

        //eturn super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
