package com.mupceet.mvpdagger;

/**
 * Created by lgz on 1/10/18.
 */

public interface BasePresenter<T> {
    void attachView(T view) ;
    void detachView() ;
}
