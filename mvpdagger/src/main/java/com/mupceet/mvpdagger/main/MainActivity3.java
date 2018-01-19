package com.mupceet.mvpdagger.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.mupceet.mvpdagger.MvpApplication;
import com.mupceet.mvpdagger.R;
import com.mupceet.mvpdagger.main.di.Apple;

import javax.inject.Inject;

/**
 * Created by lgz on 1/10/18.
 */

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    public Apple mApple;

    @Inject
    public Apple mApple1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mPresenter = new MainPresenter(this);
        ((MvpApplication)getApplication()).getMainComponent().inject(this);

        Log.e(TAG, mApple.toString());
        Log.e(TAG, mApple1.toString());

    }
}
