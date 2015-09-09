package com.example.yudiandrean.socioblood.feeds;

/**
 * Created by yudiandrean on 5/24/2015.
 *
 * An adapter with swipe to refresh
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
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yudiandrean.socioblood.R;
import com.example.yudiandrean.socioblood.feeds.FeedController;
import com.example.yudiandrean.socioblood.feeds.FeedImageView;
import com.example.yudiandrean.socioblood.feeds.FeedItem;


public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;

    ImageLoader imageLoader = FeedController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems)
    {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = FeedController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView blood_type = (TextView) convertView
                .findViewById(R.id.blood_type);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView post_bloodtype = (TextView) convertView
                .findViewById(R.id.post_bloodtype);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);

        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);

        FeedItem item = feedItems.get(position);

        name.setText(item.getName());
        username.setText(item.getUserName());
        timestamp.setText(item.getTimeStamp());
        blood_type.setText(item.getUserBloodtype() + item.getUserRhesus());


        // Converting timestamp into x ago format
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//                Long.parseLong(item.getTimeStamp()),
//                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//        timestamp.setText(timeAgo);



        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        post_bloodtype.setText("Bloodtype needed : " +item.getBloodtype()+item.getRhesus());


        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }


        // Checking for null user profile pic
        if (item.getProfilePic() != null) {
            profilePic.setImageUrl(item.getProfilePic(), imageLoader);

            profilePic.setVisibility(View.VISIBLE);
        } else {
            // profilePic is null, remove from the view
            profilePic.setVisibility(View.GONE);
        }


        // Feed image
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void clearAdapter()
    {
        feedItems.clear();
        notifyDataSetChanged();
    }

}