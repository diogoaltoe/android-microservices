package com.diogoaltoe.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.Oauth2Controller;

public class UserHomeActivity extends AppCompatActivity {

    private TextView textViewUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        textViewUser = (TextView) findViewById(R.id.textViewWelcome);

        // Get instance from authenticate User
        Oauth2Controller oauth2 = Oauth2Controller.getInstance();
        userName = oauth2.getName();

        // Prepare the text
        Resources res = getResources();
        String textWelcomeUser = String.format(res.getString(R.string.text_user_welcome), userName);

        // Update the label on screen
        textViewUser.setText(textWelcomeUser);
    }

    /**
     * Runs when you click the User button
     * */
    public void buttonUserMain(View view) {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
    }

    /**
     * Runs when you click the Product button
     * */
    public void buttonProductMain(View view) {
        Intent intent = new Intent(this, ProductMainActivity.class);
        startActivity(intent);
    }

    /**
     * Runs when you click the Customer button
     * */
    public void buttonCustomerMain(View view) {
        Intent intent = new Intent(this, CustomerMainActivity.class);
        startActivity(intent);
    }

    /**
     * Runs when you click the Logoff button
     * */
    public void buttonLogoff(View view) {
        // Clear the Session
        Oauth2Controller.destroyInstance();

        Intent intent = new Intent(this, MainActivity.class);
        // Reset the Activity's historic
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
