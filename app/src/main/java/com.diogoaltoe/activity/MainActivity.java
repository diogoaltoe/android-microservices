package com.diogoaltoe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.diogoaltoe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Runs when you click the New User button
     * */
    public void buttonUserNew(View view) {
        Intent intent = new Intent(this, UserNewActivity.class);
        intent.putExtra("visitor", "y");
        startActivity(intent);
    }

    /**
     * Runs when you click the Login button
     * */
    public void buttonLogin(View view) {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }


}
