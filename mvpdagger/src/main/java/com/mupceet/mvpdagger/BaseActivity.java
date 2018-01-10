package com.mupceet.mvpdagger;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by lgz on 1/10/18.
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
