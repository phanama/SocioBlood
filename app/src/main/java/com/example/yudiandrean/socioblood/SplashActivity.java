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


        import android.app.Fragment;
        import android.view.View;
        import android.os.Bundle;

        import android.os.Handler;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;

public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_fragment);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,FeedActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

//public class SplashFragment extends Fragment {
//
//
//
//    @Override
//    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//
////        new Handler().postDelayed(new Runnable() {
////                                      @Override
////                                      public void run() {
////                                /* Create an Intent that will start the Menu-Activity. */
////                                          inflater.inflate(R.layout.splash_fragment, container, false);
////                                      }
////                                  },
////                SPLASH_DISPLAY_LENGHT);
//
//        return inflater.inflate(R.layout.splash_fragment, container, false);
//    }
//}