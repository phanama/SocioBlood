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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.Calendar;


public class MainActivity extends Activity {


    static final String TAG = "MainActivity";
    private final int SPLASH_DISPLAY_LENGHT = 2500;
    private int SPLASH_DISPLAY_LENGHTT = 2500;

    /**
     * Time to do next feed fetch
     */
    private Calendar m_nextFetch = null;

    /**
     * Tablet UI mode?
     */
    private boolean m_isTablet = false;


    /**
     * Display mode
     */
    public enum Mode {
        /**
         * Showing feed
         */
        FEED,

        /**
         * Showing an object from the feed
         */
        FEED_OBJECT,

        /**
         * Showing an object
         */
        OBJECT
    }

    ;

    Mode m_displayMode = Mode.FEED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//
//
//            getFragmentManager().beginTransaction()
//                    .add(R.id.feed_fragment, new SplashFragment())
//                    .setTransition(FragmentTransaction.TRANSIT_NONE)
//                    .commit();
//
//
//        } else {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.feed_fragment, new SplashFragment())
//                    .setTransition(FragmentTransaction.TRANSIT_NONE)
//                    .commit();
//
//
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);

    }


    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                /* Create an Intent that will start the Menu-Activity. */
                                          SPLASH_DISPLAY_LENGHTT++;
                                      }
                                  },
                SPLASH_DISPLAY_LENGHT);

        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}





