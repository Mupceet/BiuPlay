package com.mupceet.mvpdagger.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mupceet.mvpdagger.MvpApplication;
import com.mupceet.mvpdagger.R;
import com.mupceet.mvpdagger.main.di.Apple;

import javax.inject.Inject;

/**
 * Created by lgz on 1/10/18.
 */

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    @Inject
    public Apple mApple;

    @Inject
    public Apple mApple1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mPresenter = new MainPresenter(this);
        // 可以看出 自定义 Scope 的作用域作用的Module时，其绑定的对象的生命周期
        // 是与 该Scope绑定的Component的生命周期相同的。
        // 我的说法是我自己的理解，说的可能不标准，主要是一个概念
        ((MvpApplication)getApplication()).getMainComponent().inject(this);

        Log.e(TAG, mApple.toString());
        Log.e(TAG, mApple1.toString());

        findViewById(R.id.btn_next_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            }
        });

    }
}
