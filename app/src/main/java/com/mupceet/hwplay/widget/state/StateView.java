package com.mupceet.hwplay.widget.state;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mupceet.hwplay.R;
import com.mupceet.hwplay.utils.UiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lgz on 1/8/18.
 */

public abstract class StateView extends FrameLayout {
    public StateView(@NonNull Context context) {
        super(context);
        initStateView();
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initStateView();
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStateView();
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStateView();
    }


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
    public @interface STATE {
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
            this.addView(mLoadingView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mErrorView == null) {
            mErrorView = createErrorView();
            this.addView(mErrorView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mEmptyView == null) {
            mEmptyView = createEmptyView();
            this.addView(mEmptyView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
            this.addView(mSuccessView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private View createLoadingView() {
        return UiUtils.inflate(R.layout.state_loading_page);
    }

    private View createErrorView() {
        View view = UiUtils.inflate(R.layout.state_error_page);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
            }
        });
        return view;
    }

    private View createEmptyView() {
        View view = UiUtils.inflate(R.layout.state_empty_page);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
            }
        });
        return view;
    }

    public void setState(@STATE int result) {
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
