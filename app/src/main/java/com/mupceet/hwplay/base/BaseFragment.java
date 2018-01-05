package com.mupceet.hwplay.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mupceet.hwplay.R;
import com.mupceet.hwplay.widget.state.StateLayout;
import com.mupceet.hwplay.widget.state.StateLayoutManager;

/**
 * Created by lgz on 1/5/18.
 */

public abstract class BaseFragment extends Fragment {

    private StateLayout mStateView;
    private StateLayoutManager mStateLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStateLayoutManager = StateLayoutManager.with(getContext())
                .contentView(setSuccessLayoutId())
                .loadingView(setLoadingLayoutId())
                .emptyDataView(setEmptyLayoutId())
                .errorView(setErrorLayoutId())
                .onShowHideViewListener(new StateLayoutManager.OnShowHideViewListener() {
                    @Override
                    public void onShowView(View view, int id) {

                    }

                    @Override
                    public void onHideView(View view, int id) {

                    }
                })
                // 点击空/错误视图时会调用该方法进行加载
                .onRetryListener(new StateLayoutManager.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        loading();
                        mStateLayoutManager.showLoading();
                    }
                })
                .build();

        return mStateLayoutManager.getRootLayout();
    }


    protected abstract int setSuccessLayoutId();
    protected abstract int setLoadingLayoutId();
    protected abstract int setEmptyLayoutId();
    protected abstract int setErrorLayoutId();

    protected abstract void loading();

    protected void setState(int state) {
        //
    }
}
