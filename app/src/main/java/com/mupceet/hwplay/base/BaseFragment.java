package com.mupceet.hwplay.base;

import android.os.Bundle;
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
import com.mupceet.hwplay.utils.UiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lgz on 1/5/18.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 定义状态
     */
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_SUCCESS = 3;
    public static final int STATE_EMPTY = 4;
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

    private int mState = STATE_UNKNOWN;


    private View mLoadingView;
    private View mErrorView;
    private View mSuccessView;
    private View mEmptyView;

    private FrameLayout mFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mFrameLayout == null) {
            mFrameLayout = new FrameLayout(getContext());
            initStateView();
        }
        return mFrameLayout;
    }

    /**
     * 请求显示内容，先请求网络再显示结果更改状态
     */
    public void showContent() {
        if (mState == STATE_UNKNOWN || mState == STATE_ERROR || mState == STATE_EMPTY) {
            mState = STATE_LOADING;
            loadData();
        }
        updateStateView();
    }

    /**
     * 将布局加载到布局中
     */
    private void initStateView() {
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
        updateStateView();
    }

    /**
     * 根据不同的状态显示不同的布局
     */
    private void updateStateView() {
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

    protected void setState(@STATE int result) {
        mState = result;
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateStateView();
            }
        });
    }

    protected abstract View createSuccessView();

    protected abstract void loadData();
}
