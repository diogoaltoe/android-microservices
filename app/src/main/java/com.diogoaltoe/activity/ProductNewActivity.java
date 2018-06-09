package com.diogoaltoe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.LoadingController;
import com.diogoaltoe.controller.Oauth2Controller;
import com.diogoaltoe.model.Product;

public class ProductNewActivity extends AppCompatActivity {

    // EditTexts of screen
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextCost;
    // Progress Bar
    private LoadingController loading;
    private View viewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new);

        //link graphical items to variables
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextCost = (EditText) findViewById(R.id.editTextCost);
        viewLoading = findViewById(R.id.progressBarLoading);
    }

    /**
     * Runs when you click the Save button
     * */
    public void buttonSave(View view) {

        Product product = new Product(
                editTextName.getText().toString(),
                editTextDescription.getText().toString(),
                Double.parseDouble(editTextCost.getText().toString())
        );

        new BackgroundTask(product).execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        private final Product params;

        public BackgroundTask(Product params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            // Instance a progress spinner
            loading = new LoadingController();
            // Show a progress spinner
            loading.showProgress(ProductNewActivity.this, viewLoading, true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                // Get instance from authenticate User
                Oauth2Controller oauth2 = Oauth2Controller.getInstance();
                // Call Web Service of Product List
                String result = oauth2.callPostService(ProductNewActivity.this, true,"product/", this.params);
                //System.out.println("String - Product: " + stringResponse);

                return result;

            } catch (Exception e) {
                //System.out.println("Exception: " + e.getMessage());

                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hidden a progress spinner
            loading.showProgress(ProductNewActivity.this, viewLoading, false);

            // If returned string is success (200)
            if(result.equals("200")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductNewActivity.this);
                builder.setMessage(R.string.text_save_message)
                        .setTitle(R.string.text_success_title)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(((Dialog)dialog).getContext(), ProductMainActivity.class));
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
