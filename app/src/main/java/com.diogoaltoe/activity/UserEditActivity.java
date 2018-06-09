package com.diogoaltoe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.LoadingController;
import com.diogoaltoe.controller.Oauth2Controller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserEditActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;

    // URL to get contacts JSON
    private String paramName;
    private String paramEmail;
    private String paramHref;
    // Progress Bar
    private LoadingController loading;
    private View viewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        viewLoading = findViewById(R.id.progressBarLoading);

        // Get params pass from another activity
        Bundle extras = getIntent().getExtras();
        // Verify if exists params
        if (extras != null) {
            paramName = extras.getString("name");
            paramEmail = extras.getString("email");
            paramHref = extras.getString("href");

            // Update the fields on screen
            editTextName.setText(paramName, TextView.BufferType.EDITABLE);
            editTextEmail.setText(paramEmail, TextView.BufferType.EDITABLE);
        }
    }


    /**
     * Runs when you click the Save button
     * */
    public void buttonSave(View view) {

        Map<String, String> params = new HashMap();
        params.put("name", editTextName.getText().toString());
        params.put("email", editTextEmail.getText().toString());

        JSONObject jsonParams = new JSONObject(params);
        String stringParams = jsonParams.toString();

        new BackgroundEditTask(stringParams).execute();
    }

    /**
     * Runs when you click the Delete button
     * */
    public void buttonDelete(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditActivity.this);
        builder.setMessage(R.string.text_delete_confirmation)
                .setTitle(R.string.text_attention_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the record confirmed
                        deleteRecord();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Delete the confirmed record
     * */
    public void deleteRecord() {

        new BackgroundDeleteTask().execute();
    }


    class BackgroundEditTask extends AsyncTask<Void, Void, String> {

        private final String params;

        public BackgroundEditTask(String params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            // Instance a progress spinner
            loading = new LoadingController();
            // Show a progress spinner
            loading.showProgress(UserEditActivity.this, viewLoading, true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                // Get instance from authenticate User
                Oauth2Controller oauth2 = Oauth2Controller.getInstance();
                // Call Web Service of User List
                String result = oauth2.callPatchService(UserEditActivity.this, true, paramHref, this.params);
                //System.out.println("String - User: " + result);

                return result;

            } catch (Exception e) {
                //System.out.println("Exception: " + e.getMessage());

                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hidden a progress spinner
            loading.showProgress(UserEditActivity.this, viewLoading, false);

            // If returned string is success (200)
            if(result.equals("200")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserEditActivity.this);
                builder.setMessage(R.string.text_edit_message)
                        .setTitle(R.string.text_success_title)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(((Dialog)dialog).getContext(), UserListActivity.class));
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


    class BackgroundDeleteTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // Instance a progress spinner
            loading = new LoadingController();
            // Show a progress spinner
            loading.showProgress(UserEditActivity.this, viewLoading, true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                // Get instance from authenticate User
                Oauth2Controller oauth2 = Oauth2Controller.getInstance();
                // Call Web Service of User List
                String result = oauth2.callDeleteService(UserEditActivity.this, true, paramHref);
                //System.out.println("String - User: " + result);

                return result;

            } catch (Exception e) {
                //System.out.println("Exception: " + e.getMessage());

                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hidden a progress spinner
            loading.showProgress(UserEditActivity.this, viewLoading, false);

            // If returned string is success (204)
            if(result.equals("204")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserEditActivity.this);
                builder.setMessage(R.string.text_delete_message)
                        .setTitle(R.string.text_success_title)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(((Dialog) dialog).getContext(), UserListActivity.class));
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
