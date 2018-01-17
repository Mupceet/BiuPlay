package com.mupceet.mvpdagger.main.di;

import android.util.Log;

/**
 * Created by lgz on 18-1-17.
 */

public class Apple {

    public static final String TAG = "Apple";

    public Apple() {
        Log.e(TAG, "normal apple");
    }

    public Apple(String color) {
        Log.e(TAG, color + " apple");
    }
}
