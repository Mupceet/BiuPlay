package com.mupceet.mvpdagger.main.model;

/**
 * Created by lgz on 1/10/18.
 */

public class User {
    private String mName;
    private String mPassword;

    public User(String name, String password) {
        mName = name;
        mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}

