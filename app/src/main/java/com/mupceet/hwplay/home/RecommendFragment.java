package com.mupceet.hwplay.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mupceet.hwplay.R;
import com.mupceet.hwplay.utils.UiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lgz on 1/3/18.
 */

public class RecommendFragment extends Fragment {
    /**
     *
     */
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_SUCCESS = 3;
    public static final int STATE_EMPTY = 4;

    private int mState = STATE_UNKNOWN;


    private View mLoadingView;
    private View mErrorView;
    private View mSuccessView;
    private View mEmptyView;

    private FrameLayout mFrameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mFrameLayout == null) {
            mFrameLayout = new FrameLayout(getContext());
            init();
            show();
        }
        return mFrameLayout;
    }

    /**
     * 将布局加载到布局中
     */
    private void init() {
        if (mLoadingView == null) {
            mLoadingView = createLoadingView();
            mFrameLayout.addView(mLoadingView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mErrorView == null) {
            mErrorView = createErrorView();
            mFrameLayout.addView(mErrorView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mEmptyView == null) {
            mEmptyView = createEmptyView();
            mFrameLayout.addView(mEmptyView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        showPaper();
    }

    /**
     * 根据不同的状态显示不同的布局
     */
    private void showPaper() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mState == STATE_LOADING || mState == STATE_UNKNOWN ? View.VISIBLE : View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (mState == STATE_SUCCESS && mSuccessView == null) {
            mSuccessView = createSuccessView();
            mFrameLayout.addView(mSuccessView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private View createLoadingView() {
        TextView textView = new TextView(getContext());
        textView.setText("Loading");
        return textView;
    }

    private View createErrorView() {
        TextView textView = new TextView(getContext());
        textView.setText("Error");
        return textView;

    }

    private View createEmptyView() {
        return UiUtils.inflate(R.layout.fragment_1);
    }

    private View createSuccessView() {
        TextView textView = new TextView(getContext());
        textView.setText("Hahahahaha");
        return textView;
    }

    /**
     * 请求网络更改状态
     */
    public void show() {
        if (mState == STATE_UNKNOWN || mState == STATE_ERROR || mState == STATE_EMPTY) {
            mState = STATE_LOADING;
            load();
        }
        showPaper();
    }

    private void load() {
        // 网络加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                setState(STATE_ERROR);
            }
        }).start();
    }

    /**
     * 不使用Enum
     */
    @IntDef({STATE_UNKNOWN, STATE_LOADING, STATE_ERROR, STATE_SUCCESS, STATE_EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    private @interface STATE {
    }
//    public enum LoadResult {
//        error(STATE_ERROR), success(STATE_SUCCESS), empty(STATE_EMPTY);
//        int value;
//
//        LoadResult(int value) {
//            this.value = value;
//        }
//    }

    private void setState(@STATE int result) {
        mState = result;
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPaper();
            }
        });
    }

}