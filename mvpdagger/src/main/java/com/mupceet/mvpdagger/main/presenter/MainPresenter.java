package com.mupceet.mvpdagger.main.presenter;

import android.util.Log;

import com.mupceet.mvpdagger.main.contract.MainContract;
import com.mupceet.mvpdagger.main.model.User;

import javax.inject.Inject;


/**
 * Created by lgz on 1/10/18.
 */

public class MainPresenter extends MainContract.PresenterImpl<MainContract.View>
        implements MainContract.Presenter {

    @Inject
    public MainPresenter(MainContract.View view) {
        // 假设对View有一个操作
        Log.e("MainPresenter: ", view.toString());
    }

    @Override
    public void login(User user) {
        if (user.getName().equals("abc") && user.getPassword().equals("123")) {
            mBaseView.loginSuccess();
        } else {
            mBaseView.loginFail();
        }
    }
}
