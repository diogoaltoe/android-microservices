package com.diogoaltoe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.LoadingController;
import com.diogoaltoe.controller.Oauth2Controller;
import com.diogoaltoe.controller.ValidateController;
import com.diogoaltoe.model.User;

import java.util.regex.Pattern;

public class UserNewActivity extends AppCompatActivity {

    // EditTexts of screen
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    // Progress Bar
    private LoadingController loading;
    private View viewLoading;

    private String visitor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new);

        //link graphical items to variables
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordRepeat = (EditText) findViewById(R.id.editTextPasswordRepeat);
        viewLoading = findViewById(R.id.progressBarLoading);

        // Get params pass from another activity
        Bundle extras = getIntent().getExtras();
        // Verify if exists params
        if (extras != null) {
            visitor = extras.getString("visitor");
        }
    }

    /**
     * Runs when you click the Save button
     * */
    public void buttonSave(View view) {

        // Reset errors.
        editTextName.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextPasswordRepeat.setError(null);

        // Store values at the time of the new user attempt.
        final String name = editTextName.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String passwordRepeat = editTextPasswordRepeat.getText().toString();

        boolean cancel = false;
        View focusView = null;
        ValidateController validate = new ValidateController();

        // Check for a valid passwordRepeat.
        if (TextUtils.isEmpty(passwordRepeat)) {
            editTextPasswordRepeat.setError(getString(R.string.error_field_required));
            focusView = editTextPasswordRepeat;
            cancel = true;
        }
        // Check for a valid passwordRepeat, if the user entered one.
        else if (!validate.isPasswordValid(passwordRepeat)) {
            editTextPasswordRepeat.setError(getString(R.string.error_invalid_password));
            focusView = editTextPasswordRepeat;
            cancel = true;
        }
        // Check if the password and the password repeated are different
        else if( !password.equals(passwordRepeat)) {
            editTextPasswordRepeat.setError(getString(R.string.error_different_password));
            focusView = editTextPasswordRepeat;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (!validate.isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!validate.isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        }
        // Check for a valid name, if the user entered one.
        else if (!validate.isNameValid(name)) {
            editTextName.setError(getString(R.string.error_invalid_name));
            focusView = editTextName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            User user = new User(
                    editTextName.getText().toString(),
                    editTextEmail.getText().toString(),
                    editTextPassword.getText().toString()
            );

            new BackgroundTask(user).execute();
        }

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        private final User params;

        public BackgroundTask(User params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            // Instance a progress spinner
            loading = new LoadingController();
            // Show a progress spinner
            loading.showProgress(UserNewActivity.this, viewLoading, true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                // Get instance from authenticate User
                Oauth2Controller oauth2 = Oauth2Controller.getInstance();
                // Call Web Service of User List
                String result = oauth2.callPostService(UserNewActivity.this, false,"user", this.params);
                //System.out.println("String - User: " + stringResponse);

                return result;

            } catch (Exception e) {
                //System.out.println("Exception: " + e.getMessage());

                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hidden a progress spinner
            loading.showProgress(UserNewActivity.this, viewLoading, false);

            // If returned string is success (201)
            if(result.equals("201")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserNewActivity.this);
                builder.setMessage(R.string.text_save_message)
                    .setTitle(R.string.text_success_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        // Verify if the previous screen was Main ou User Activity
                        // If was User Activity
                        if( visitor == null ) {
                            startActivity(new Intent(((Dialog) dialog).getContext(), UserMainActivity.class));
                        }
                        // If was Main Activity
                        else {
                            startActivity(new Intent(((Dialog) dialog).getContext(), MainActivity.class));
                        }
                        }
                    });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            // If returned string is NetworkException
            else if(result == "NetworkException") {
                // Show message about exception return
                Toast.makeText(
                    getApplicationContext(),
                    R.string.exception_network,
                    Toast.LENGTH_LONG)
                        .show();
            }
            // If returned string is Exception
            // Or return "401"
            else {
                // Show message about exception return
                Toast.makeText(
                    getApplicationContext(),
                    R.string.exception_service,
                    Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}
