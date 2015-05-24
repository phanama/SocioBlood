package com.example.yudiandrean.socioblood.Views;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by yudiandrean on 5/25/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    public SpinnerAdapter(Context context, int resource, List<SpinnerItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount() - 1; // This makes the trick: do not show last item
    }

    @Override
    public SpinnerItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
