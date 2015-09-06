package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/9/2015.
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

        final TextView fullname = (TextView)findViewById(R.id.fullname);
        final TextView uname = (TextView)findViewById(R.id.username);
        final TextView email = (TextView)findViewById(R.id.email);
        final TextView bloodtype = (TextView)findViewById(R.id.bloodtype);
        final TextView rhesus = (TextView)findViewById(R.id.rhesus);
        final TextView gender = (TextView)findViewById(R.id.gender);
        final TextView created_at = (TextView)findViewById(R.id.registeredat);
        fullname.setText(user.get("fullname"));
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

