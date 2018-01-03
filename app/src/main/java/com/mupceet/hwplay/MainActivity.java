package com.mupceet.hwplay;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.mupceet.hwplay.base.BaseActivity;
import com.mupceet.hwplay.home.HomeActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_to_home)
    Button mBtnToHome;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBtnToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });
    }
}
