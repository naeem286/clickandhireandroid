package com.example.naeemasghar.clickandhire;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ActivityNewsDetail extends AppCompatActivity {
    public TextView title, date, detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        title = (TextView)  findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);
        date = (TextView)   findViewById(R.id.date);
        title.setText(getIntent().getStringExtra("title"));
        detail.setText(getIntent().getStringExtra("detail"));
        date.setText(getIntent().getStringExtra("date"));
    }

}
