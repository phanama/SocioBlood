package com.example.yudiandrean.socioblood.Views;

/**
 * Created by yudiandrean on 5/25/2015.
 */
public class SpinnerItem {
    private final String text;
    private final boolean isHint;

    public SpinnerItem(String strItem, boolean flag) {
        this.isHint = flag;
        this.text = strItem;
    }

    public String getItemString() {
        return text;
    }

    public boolean isHint() {
        return isHint;
    }
}
