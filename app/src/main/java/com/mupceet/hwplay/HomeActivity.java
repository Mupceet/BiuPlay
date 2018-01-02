package com.mupceet.hwplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mupceet.hwplay.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.btn_to_main)
    Button mBtnToMain;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void initView() {
        mBtnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
    }
}
