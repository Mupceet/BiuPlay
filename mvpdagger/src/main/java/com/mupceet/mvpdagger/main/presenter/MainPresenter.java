package com.mupceet.mvpdagger.main.presenter;

import com.mupceet.mvpdagger.main.contract.MainContract;
import com.mupceet.mvpdagger.main.model.User;


/**
 * Created by lgz on 1/10/18.
 */

public class MainPresenter extends MainContract.PresenterImpl<MainContract.View>
        implements MainContract.Presenter {

    public MainPresenter() {
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
