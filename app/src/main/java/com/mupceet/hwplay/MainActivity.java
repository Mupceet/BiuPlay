package com.mupceet.hwplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mupceet.hwplay.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_to_home)
    Button mBtnToHome;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
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
