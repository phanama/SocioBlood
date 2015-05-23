package com.example.yudiandrean.socioblood.Twitter;

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