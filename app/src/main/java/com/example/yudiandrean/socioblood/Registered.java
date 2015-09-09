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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yudiandrean.socioblood.databases.DatabaseHandler;

import java.util.HashMap;

public class Registered extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap<String,String> user = db.getUserDetails();

        /**
         * Displays the registration details in Text view
         **/

        final TextView firstname = (TextView)findViewById(R.id.firstname);
        final TextView lastname = (TextView)findViewById(R.id.lastname);
        final TextView uname = (TextView)findViewById(R.id.username);
        final TextView email = (TextView)findViewById(R.id.email);
        final TextView bloodtype = (TextView)findViewById(R.id.bloodtype);
        final TextView rhesus = (TextView)findViewById(R.id.rhesus);
        final TextView gender = (TextView)findViewById(R.id.gender);
        final TextView created_at = (TextView)findViewById(R.id.registeredat);
        firstname.setText(user.get("firstname"));
        lastname.setText(user.get("lastname"));
        uname.setText(user.get("uname"));
        email.setText(user.get("email"));
        bloodtype.setText(user.get("blood_type"));
        rhesus.setText(user.get("rhesus"));
        gender.setText(user.get("gender"));
        created_at.setText(user.get("created_at"));


        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


    }}

