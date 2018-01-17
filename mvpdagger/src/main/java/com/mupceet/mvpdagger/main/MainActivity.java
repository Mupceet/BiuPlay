package com.mupceet.mvpdagger.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mupceet.mvpdagger.BaseActivity;
import com.mupceet.mvpdagger.R;
import com.mupceet.mvpdagger.main.contract.MainContract;
import com.mupceet.mvpdagger.main.di.Apple;
import com.mupceet.mvpdagger.main.di.DaggerMainComponent;
import com.mupceet.mvpdagger.main.di.MainModule;
import com.mupceet.mvpdagger.main.di.scope.Type;
import com.mupceet.mvpdagger.main.model.User;
import com.mupceet.mvpdagger.main.presenter.MainPresenter;

import javax.inject.Inject;

/**
 * Created by lgz on 1/10/18.
 */

public class MainActivity extends BaseActivity implements MainContract.View {

    private EditText mEtName;
    private EditText mEtPassword;
    private Button mBtnLogin;

    // 这里要注意使用 MainPresenter
    @Inject
    public MainPresenter mPresenter;

    @Inject
    @Type("color")
    public Apple mApple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mPresenter = new MainPresenter(this);
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
        mPresenter.attachView(this);

        mEtName = findViewById(R.id.et_name);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                String pwd = mEtPassword.getText().toString();
                User user = new User(name, pwd);
                mPresenter.login(user);
            }
        });

    }

    @Override
    public void loginSuccess() {
        showToast("Success");
    }

    @Override
    public void loginFail() {
        showToast("Fail");
    }
}
