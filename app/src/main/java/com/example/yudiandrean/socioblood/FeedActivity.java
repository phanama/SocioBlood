package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/24/2015.
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yudiandrean.socioblood.Twitter.TwitterActivity;
import com.example.yudiandrean.socioblood.Views.SpinnerAdapter;
import com.example.yudiandrean.socioblood.Views.SpinnerItem;
import com.example.yudiandrean.socioblood.databases.DatabaseHandler;
import com.example.yudiandrean.socioblood.databases.SessionManager;
import com.example.yudiandrean.socioblood.databases.UserFunctions;
import com.example.yudiandrean.socioblood.feeds.FeedController;
import com.example.yudiandrean.socioblood.feeds.FeedItem;
import com.example.yudiandrean.socioblood.feeds.FeedListAdapter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

public class FeedActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = FeedActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
//    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    private String URL_FEED = "http://www.socioblood.com/socioblood/read_post.php";
    private TextView postrequest; //edittext for post request
    final Context context = this;

    private static String KEY_SUCCESS = "success";
    private static String KEY_PID = "p_id";
    private static String KEY_MESSAGE = "message";
    private static String KEY_BLOODTYPE = "post_bloodtype";
    private static String KEY_POSTED_AT = "posted_at";
    private static String KEY_RHESUS = "post_rhesus";
    private static String KEY_ERROR = "error";
    private SessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Dialog d = new Dialog(context);
        setContentView(R.layout.feed_activity);
        postrequest = (TextView) findViewById(R.id.editText);


        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        final int width, height;
        ActionBar.LayoutParams params;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }


        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        // add button listener
        postrequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.post_request);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(d.getWindow().getAttributes());
                    lp.width = width;
                    lp.height = height;
                    d.getWindow().setAttributes(lp);
                } catch (AndroidRuntimeException e) {
                } catch (Exception e) {
                }


                final Spinner rhesusspinner = (Spinner) d.findViewById(R.id.rhesus_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FeedActivity.this, android.R.layout.simple_spinner_dropdown_item) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView) v.findViewById(android.R.id.text1)).setText("");
                            ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount() - 1; // you dont display last item. It is used as hint.
                    }

                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.add("+");
                adapter.add("-");
                adapter.add("Rhesus");

                rhesusspinner.setAdapter(adapter);
                rhesusspinner.setSelection(adapter.getCount()); //display hint


                final Spinner bloodspinner = (Spinner) d.findViewById(R.id.bloodtype_spinner);
                ArrayAdapter<String> bloodadapter = new ArrayAdapter<String>(FeedActivity.this, android.R.layout.simple_spinner_dropdown_item) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView) v.findViewById(android.R.id.text1)).setText("");
                            ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount() - 1; // you dont display last item. It is used as hint.
                    }

                };

                bloodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bloodadapter.add("O");
                bloodadapter.add("A");
                bloodadapter.add("B");
                bloodadapter.add("AB");
                bloodadapter.add("Desired Type");

                bloodspinner.setAdapter(bloodadapter);
                bloodspinner.setSelection(bloodadapter.getCount()); //display hint


                //Buttons-Editexts
                Button btnpost = (Button) d.findViewById(R.id.post);
                final EditText userInput = (EditText) d.findViewById(R.id.editTextDialogUserInput);

                d.show();

                btnpost.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   if (bloodspinner.getSelectedItem().toString().equals("Desired Type")) {
                                                       Toast.makeText(getApplicationContext(),
                                                               "Input Blood Type!", Toast.LENGTH_SHORT).show();
                                                   } else if (rhesusspinner.getSelectedItem().toString().equals("Rhesus")) {
                                                       Toast.makeText(getApplicationContext(),
                                                               "Input Rhesus!", Toast.LENGTH_SHORT).show();
                                                   } else if (userInput.getText().toString().equals("")) {
                                                       Toast.makeText(getApplicationContext(),
                                                               "Input your request message!", Toast.LENGTH_SHORT).show();
                                                   } else {

                                                       int uid = session.currentUID();

                                                       String message = userInput.getText().toString();
                                                       String post_bloodtype = bloodspinner.getSelectedItem().toString();
                                                       String post_rhesus = rhesusspinner.getSelectedItem().toString();

                                                       NetAsync(d, view, uid, message, post_bloodtype, post_rhesus);


                                                   }
                                               }
                                           }
                );


            }
        });


//        // We first check for cached request
//        Cache cache = FeedController.getInstance().getRequestQueue().getCache();
//        Entry entry = cache.get(URL_FEED);
//        if (entry != null) {
//            // fetch the data from cache
//            try {
//                String data = new String(entry.data, "UTF-8");
//                try {
//                    parseJsonFeed(new JSONObject(data));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        } else {
        // making fresh volley request and getting json


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getTimelineAsync();
                                    }
                                }
        );

    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        getTimelineAsync();

    }

    //Fetch the timeline by requesting http
public void getTimelineAsync()
{
    listAdapter.clearAdapter();

    // showing refresh animation before making http call
    swipeRefreshLayout.setRefreshing(true);

    //Volley's json array request object
    JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
            URL_FEED, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.d(TAG, "Response: " + response.toString());
            if (response != null) {
                parseJsonFeed(response);
                listAdapter.notifyDataSetChanged();
            }
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }
    });

    // Adding request to volley request queue
    FeedController.getInstance().addToRequestQueue(jsonReq);

    listAdapter.notifyDataSetChanged();

}



    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("post");

            for (int i = feedArray.length()-1; i >= 0 ; i--) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("p_id"));
                String items = feedObj.getString("firstname") + " " + feedObj.getString("lastname") ;
                item.setName(items);
                item.setUserName(feedObj.getString("username"));

                // Image might be null sometimes
//                String image = feedObj.isNull("image") ? null : feedObj
//                        .getString("image");
//                item.setImge(image);

                item.setStatus(feedObj.getString("message"));
//
//                 //may be null
//                String imaget = feedObj.isNull("profilePic") ? null : feedObj
//                        .getString("profilePic");
//                item.setProfilePic(feedObj.getString(imaget));

                item.setTimeStamp(feedObj.getString("posted_at"));

//                // url might be null sometimes
//                String feedUrl = feedObj.isNull("url") ? null : feedObj
//                        .getString("url");ifcon
//                item.setUrl(feedUrl);

                item.setBloodtype(feedObj.getString("post_bloodtype"));
                item.setRhesus(feedObj.getString("post_rhesus"));

                item.setUserBloodtype(feedObj.getString("blood_type"));
                item.setUserRhesus(feedObj.getString("rhesus"));

                feedItems.add(item);

            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }



    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;
        private View view;
        int uid;
        String message, post_bloodtype, post_rhesus;
        Dialog d;

        public NetCheck(Dialog d, View view, int uid,String message,String post_bloodtype,String post_rhesus) {
            this.view = view;
            this.uid = uid;
            this.message = message;
            this.post_bloodtype=post_bloodtype;
            this.post_rhesus=post_rhesus;
            this.d = d;

        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(FeedActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("https://www.google.com");
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
                ProcessPost post = new ProcessPost(d, view, uid, message, post_bloodtype,post_rhesus);
                post.execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Error in internet connection!", Toast.LENGTH_SHORT).show();

            }
        }
    }



    private class ProcessPost extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        private View view;
        Dialog d;



        String message,post_bloodtype,post_rhesus;
        int uid;

        public ProcessPost(Dialog d, View view, int uid,String message,String post_bloodtype,String post_rhesus) {
            this.view = view;
            this.uid = uid;
            this.message = message;
            this.post_bloodtype=post_bloodtype;
            this.post_rhesus=post_rhesus;
            this.d = d;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FeedActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Posting ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.addPost(uid, message, post_bloodtype, post_rhesus);

            return json;


        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.dismiss();
                        d.dismiss();

                        Toast.makeText(getApplicationContext(),
                                "Post Success!", Toast.LENGTH_SHORT).show();
                        getTimelineAsync();
                        getTimelineAsync();
                    }

                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Posting failed!", Toast.LENGTH_SHORT).show();
                    }

            }


                else{
                    pDialog.dismiss();

                    Toast.makeText(getApplicationContext(),
                            "Error occured in posting", Toast.LENGTH_SHORT).show();
                }

                onCreate(null);

            } catch (JSONException e) {
                e.printStackTrace();



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }}






    public void NetAsync(Dialog d,View view, int uid, String message, String post_bloodtype, String post_rhesus){
        NetCheck check = new NetCheck(d, view, uid, message, post_bloodtype,post_rhesus);
        check.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_twitter:

                    Intent intentTweet = new Intent(this, TwitterActivity.class);
                    startActivity(intentTweet);
                    return true;

            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.user_panel:
                Intent intentUserPanel = new Intent(this, UserPanel.class);
                startActivity(intentUserPanel);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
