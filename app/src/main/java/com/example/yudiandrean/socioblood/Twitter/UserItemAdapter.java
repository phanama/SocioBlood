package com.example.yudiandrean.socioblood.Twitter;

/**
 * Created by yudiandrean on 14/7/15.
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yudiandrean.socioblood.R;

import java.net.URL;
import java.util.ArrayList;

public class UserItemAdapter extends ArrayAdapter<Tweet> {
    private ArrayList<Tweet> tweets;
    Context mContext;

    public UserItemAdapter(Context context, int textViewResourceId, ArrayList<Tweet> tweets) {
        super(context, textViewResourceId, tweets);
        mContext = context;
        this.tweets = tweets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem1, parent, false);
        }

        Tweet tweet = tweets.get(position);
        if (tweet != null) {
            TextView username = (TextView) v.findViewById(R.id.username);
            TextView message = (TextView) v.findViewById(R.id.message);
            ImageView image = (ImageView) v.findViewById(R.id.avatar);

            if (username != null) {
                username.setText("@" + tweet.getName());
            }

            if (message != null) {
                message.setText(tweet.getText());
            }

            if (image != null) {
                image.setImageBitmap(getBitmap(tweet.getUrlImagemPerfil()));
            }
        }
        return v;
    }


    public Bitmap getBitmap(String bitmapUrl) {
        try {
            URL url = new URL(bitmapUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception ex) {
            return null;
        }
    }
}