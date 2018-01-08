package com.mupceet.hwplay.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mupceet.hwplay.base.BaseFragment;

/**
 * Created by lgz on 1/3/18.
 */

public class AppManagerFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContent();
    }

    @Override
    protected View createSuccessView() {
        TextView textView = new TextView(getContext());
        textView.setText("AppManager");
        return textView;
    }

    @Override
    protected void loadData() {
        // 网络加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                setState(STATE_EMPTY);
            }
        }).start();
    }
}