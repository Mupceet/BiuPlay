package com.mupceet.hwplay.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mupceet.hwplay.R;
import com.mupceet.hwplay.base.BaseFragment;
import com.mupceet.hwplay.utils.UiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lgz on 1/3/18.
 */

public class RecommendFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContent();
    }

    @Override
    protected View createSuccessView() {
        TextView textView = new TextView(getContext());
        textView.setText("Hahahahaha");
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