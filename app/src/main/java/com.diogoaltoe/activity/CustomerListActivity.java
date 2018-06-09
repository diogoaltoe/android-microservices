package com.diogoaltoe.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.diogoaltoe.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.diogoaltoe.R;
import com.diogoaltoe.controller.LoadingController;
import com.diogoaltoe.controller.Oauth2Controller;

public class CustomerListActivity extends AppCompatActivity {

    private ListView listViewCustomerList;

    private ArrayList<Customer> arrayListCustomer;

    // Progress Bar
    private LoadingController loading;
    private View viewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        // Instance the user array list
        arrayListCustomer = new ArrayList<>();
        // Set the user list with element on the screen
        listViewCustomerList = (ListView) findViewById(R.id.listViewCustomerList);
        // Set the progress spinner with element on the screen
        viewLoading = findViewById(R.id.progressBarLoading);

        // Call the background task to set the list
        new BackgroundTask().execute();
    }

    /**
     * Class that execute in background the task of set the list
     */
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // Instance a progress spinner
            loading = new LoadingController();
            // Show a progress spinner
            loading.showProgress(CustomerListActivity.this, viewLoading, true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                // Get instance from authenticate User
                Oauth2Controller oauth2 = Oauth2Controller.getInstance();
                // Call Web Service of Customer List
                Object response = oauth2.callGetService(CustomerListActivity.this, true, "customer/?sort=firstName&firstName.dir=asc", false);
                //System.out.println("String - Customer: " + stringResponse);

                // Map the response into a String
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String result = ow.writeValueAsString(response);

                return result;

            } catch (Exception e) {
                //System.out.println("Exception: " + e.getMessage());

                return "Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hidden a progress spinner
            loading.showProgress(CustomerListActivity.this, viewLoading, false);

            // If returned string is NOT NetworkException
            if(result != "NetworkException") {
                // If returned string is NOT Exception
                // And NOT return "401"
                if((result != "Exception") && (!result.equals("401"))) {
                    try {
                        // Convert the result String into a Json
                        JSONObject jsonResult = new JSONObject(result);

                        // Verify if the list has items
                        if (jsonResult.has("_embedded")) {
                            // Get the "_embedded" node
                            JSONObject jsonEmbedded = jsonResult.getJSONObject("_embedded");
                            // Get the "customer" node
                            JSONArray jsonPeople = jsonEmbedded.getJSONArray("customer");

                            // Looping through All People
                            for (int i = 0; i < jsonPeople.length(); i++) {
                                // Get the Customer from the current position
                                JSONObject jsonCustomer = jsonPeople.getJSONObject(i);

                                // Get the value from "firstName" field
                                String firstName = jsonCustomer.getString("firstName");
                                // Get the value from "lastName" field
                                String lastName = jsonCustomer.getString("lastName");

                                // Get the "_links" node
                                JSONObject objLink = jsonCustomer.getJSONObject("_links");
                                // Get the "self" node
                                JSONObject objHref = objLink.getJSONObject("self");

                                // Get the value from "href" field
                                String href = objHref.getString("href");

                                // Set the User with the values of fields
                                Customer customer = new Customer();
                                customer.setFirstName(firstName);
                                customer.setLastName(lastName);
                                customer.setHref(href);

                                // Adding Customer to People List
                                arrayListCustomer.add(customer);
                            }

                            // Instance custom adapter
                            // And set with the People List
                            CustomerAdapter adapter = new CustomerAdapter(arrayListCustomer, CustomerListActivity.this);
                            listViewCustomerList.setAdapter(adapter);

                            // Create a click listener from the item of the list
                            listViewCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    // Get the position of the item clicked
                                    Object itemList = listViewCustomerList.getItemAtPosition(position);

                                    //System.out.println("itemList: "+ itemList);

                                    // Instance Gson variable
                                    Gson gson = new Gson();
                                    // Convert Object to Json (String)
                                    String jsonStrItem = gson.toJson(itemList);

                                    try {
                                        // Convert JsonString to JsonObject
                                        JSONObject jsonObjItem = new JSONObject(jsonStrItem);

                                        // Set items of List
                                        String jsonFirstName = jsonObjItem.getString("firstName");
                                        String jsonLastName = jsonObjItem.getString("lastName");
                                        String jsonHref = jsonObjItem.getString("href");

                                        // Redirect to the edit screen
                                        // Passing the item's url as param
                                        Intent intent = new Intent(CustomerListActivity.this, CustomerEditActivity.class);
                                        intent.putExtra("firstName", jsonFirstName);
                                        intent.putExtra("lastName", jsonLastName);
                                        intent.putExtra("href", jsonHref);
                                        startActivity(intent);

                                    } catch (final JSONException e) {
                                        //System.out.println("JSONException: " + e.getMessage());

                                        // Show message about exception return
                                        Toast.makeText(
                                            getApplicationContext(),
                                            R.string.exception_json,
                                            Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                        }
                        // If returned string is Empty
                        else {
                            // Show message about exception return
                            Toast.makeText(
                                getApplicationContext(),
                                R.string.exception_empty,
                                Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (final JSONException e) {
                        //System.out.println("JSONException: " + e.getMessage());

                        // Show message about exception return
                        Toast.makeText(
                            getApplicationContext(),
                            R.string.exception_json,
                            Toast.LENGTH_LONG)
                                .show();
                    }
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
            // If returned string is NetworkException
            else {
                // Show message about exception return
                Toast.makeText(
                        getApplicationContext(),
                        R.string.exception_network,
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}
