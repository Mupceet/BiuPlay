package com.mupceet.hwplay.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mupceet.hwplay.base.BaseFragment;

import static com.mupceet.hwplay.widget.state.StateView.STATE_ERROR;

/**
 * Created by lgz on 1/3/18.
 */

public class MyFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContent();
    }

    @Override
    protected View createSuccessView() {
        TextView textView = new TextView(getContext());
        textView.setText("我的");
        return textView;
    }

    @Override
    protected void loadData() {
        // 网络加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                setState(STATE_ERROR);
            }
        }).start();
    }
}