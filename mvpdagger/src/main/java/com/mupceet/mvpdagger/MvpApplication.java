package com.mupceet.mvpdagger;

import android.app.Application;

import com.mupceet.mvpdagger.main.di.MainModule;
import com.mupceet.mvpdagger.main.di.component.DaggerMainComponent3;
import com.mupceet.mvpdagger.main.di.component.MainComponent;
import com.mupceet.mvpdagger.main.di.component.MainComponent3;

/**
 * Created by lgz on 18-1-19.
 */

public class MvpApplication extends Application {

    private MainComponent3 mMainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainComponent = DaggerMainComponent3.create();
    }

    public MainComponent3 getMainComponent() {
        return mMainComponent;
    }
}
