package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/24/2015.
 */

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yudiandrean.socioblood.Views.SpinnerAdapter;
import com.example.yudiandrean.socioblood.Views.SpinnerItem;
import com.example.yudiandrean.socioblood.databases.SessionManager;
import com.example.yudiandrean.socioblood.feeds.FeedController;
import com.example.yudiandrean.socioblood.feeds.FeedItem;
import com.example.yudiandrean.socioblood.feeds.FeedListAdapter;

public class FeedActivity extends Activity {
    private static final String TAG = FeedActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
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



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        // add button listener
        postrequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog d = new Dialog(FeedActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.post_request);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = width;
                lp.height = height;
                d.getWindow().setAttributes(lp);

                Button btnpost = (Button) d.findViewById(R.id.post);
                EditText userInput = (EditText) d.findViewById(R.id.editTextDialogUserInput);

                Spinner rhesusspinner = (Spinner) d.findViewById(R.id.rhesus_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FeedActivity.this, android.R.layout.simple_spinner_dropdown_item) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount()-1; // you dont display last item. It is used as hint.
                    }

                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.add("+");
                adapter.add("-");
                adapter.add("Rhesus");

                rhesusspinner.setAdapter(adapter);
                rhesusspinner.setSelection(adapter.getCount()); //display hint



                Spinner bloodspinner = (Spinner) d.findViewById(R.id.bloodtype_spinner);
                ArrayAdapter<String> bloodadapter = new ArrayAdapter<String>(FeedActivity.this, android.R.layout.simple_spinner_dropdown_item) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount()-1; // you dont display last item. It is used as hint.
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


                btnpost.setOnClickListener(new View.OnClickListener()
                {
                                               @Override
                                               public void onClick(View v)
                                               {
                                                   Toast.makeText(getApplicationContext(),
                                                           "Clicked", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                );



                d.show();
            }
        });

        // We first check for cached request
        Cache cache = FeedController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            FeedController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
