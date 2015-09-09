package com.example.yudiandrean.socioblood;
/**
 * Created by yudiandrean on 15/7/2015.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yudiandrean.socioblood.databases.DatabaseHandler;
import com.example.yudiandrean.socioblood.databases.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChangePassword extends Activity{

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";



    EditText newpass, verifynewpass;
    TextView alert;
    Button changepass;
    Button cancel;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.changepassword);

        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent login = new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(login);
                finish();
            }

        });



        newpass = (EditText) findViewById(R.id.newpass);
        verifynewpass = (EditText) findViewById(R.id.verifynewpass);
        alert = (TextView) findViewById(R.id.alertpass);
        changepass = (Button) findViewById(R.id.btchangepass);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newpass.getText().toString().equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Password field is empty");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
                    alertDialog.show();
                } else if (!verifynewpass.getText().toString().equals(newpass.getText().toString())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Passwords do not match, try again!");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
                    alertDialog.show();
                } else {
                    NetAsync(view);
                }
            }
        });}

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject>{


        private ProgressDialog pDialog;

        String newpas,email,verifynewpas;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<>();
            user = db.getUserDetails();




            newpas = newpass.getText().toString();
            email = user.get("email");



            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.chgPass(newpas, email);
            Log.d("Button", "Register");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        alert.setText("Your Password is successfully changed.");


                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        alert.setText("Invalid old Password.");
                    } else {
                        pDialog.dismiss();
                        alert.setText("Error occured in changing Password.");
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}


