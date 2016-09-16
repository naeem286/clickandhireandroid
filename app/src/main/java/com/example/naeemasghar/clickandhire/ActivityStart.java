package com.example.naeemasghar.clickandhire;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ActivityStart extends AppCompatActivity {
    Button btn_Login,btn_Register;
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_start);

        btn_Login= (Button)findViewById(R.id.btn_start_login);
        btn_Register= (Button)findViewById(R.id.btn_start_register);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(ActivityStart.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btn_Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Launch Login Activity
                Intent intent = new Intent(ActivityStart.this,
                        ActivityLogin.class);
                startActivity(intent);
            }
        });
        // Register button Click Event
        btn_Register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Launch Register Activity
                Intent intent = new Intent(ActivityStart.this,
                        ActivityRegister.class);
                startActivity(intent);
            }
        });
        // Floating Search Button Click Event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Quick Search  Activity
                Intent intent = new Intent(ActivityStart.this,
                        ActivityQuickSearch.class);
                startActivity(intent);
            }
        });
    }

}
