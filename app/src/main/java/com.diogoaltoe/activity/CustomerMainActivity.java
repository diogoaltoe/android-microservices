package com.diogoaltoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import com.diogoaltoe.R;


public class CustomerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link the XML layout to this JAVA class
        setContentView(R.layout.activity_customer_main);
    }

    /**
     * Runs when you click the New button
     * */
    public void buttonNew(View view) {
        Intent intent = new Intent(this, CustomerNewActivity.class);
        startActivity(intent);
    }

    /**
     * Runs when you click the List button
     * */
    public void buttonList(View view) {
        Intent intent = new Intent(this, CustomerListActivity.class);
        startActivity(intent);
    }

}
