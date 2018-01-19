package com.mupceet.mvpdagger.main.di;

import android.util.Log;

/**
 * Created by lgz on 18-1-17.
 */
// @Singleton
// singleton 注解没有作用，因为这里要起效果，需要使用 @Inject 构造函数的方式
public class Apple {

    public static final String TAG = "Apple";

    public Apple() {
        Log.e(TAG, "normal apple");
    }

    public Apple(String color) {
        Log.e(TAG, color + " apple");
    }
}
