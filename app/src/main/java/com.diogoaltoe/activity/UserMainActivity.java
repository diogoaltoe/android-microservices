package com.diogoaltoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.Oauth2Controller;


public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
    }

    /**
     * Runs when you click the New button
     * */
    public void buttonNew(View view) {
        Intent intent = new Intent(this, UserNewActivity.class);
        startActivity(intent);
    }

    /**
     * Runs when you click the List button
     * */
    public void buttonList(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

}
