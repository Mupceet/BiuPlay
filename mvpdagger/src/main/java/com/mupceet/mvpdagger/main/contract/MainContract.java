package com.mupceet.mvpdagger.main.contract;

import com.mupceet.mvpdagger.BaseModel;
import com.mupceet.mvpdagger.BasePresenter;
import com.mupceet.mvpdagger.BaseView;
import com.mupceet.mvpdagger.main.model.User;

/**
 * Created by lgz on 1/10/18.
 */

public interface MainContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
        void loginSuccess();
        void loginFail();
    }

    interface Presenter extends BasePresenter<View> {
        void login(User user);
    }

    /**
     * Created by lgz on 1/10/18.
     */

    class PresenterImpl<T extends BaseView> implements BasePresenter<T> {

        protected T mBaseView;
        @Override
        public void attachView(T view) {
            mBaseView = view;
        }

        @Override
        public void detachView() {
            mBaseView = null;
        }
    }
}
