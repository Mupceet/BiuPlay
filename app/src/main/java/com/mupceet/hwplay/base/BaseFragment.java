package com.mupceet.hwplay.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mupceet.hwplay.widget.state.StateView;

/**
 * Created by lgz on 1/5/18.
 */

public abstract class BaseFragment extends Fragment {

    private StateView mStateView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mStateView == null) {
            mStateView = new StateView(getContext()) {
                @Override
                protected View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                protected void loadData() {
                    BaseFragment.this.loadData();
                }
            };
        }
        return mStateView;
    }

    /**
     * 请求显示内容，先请求网络再显示结果更改状态
     */
    public void showContent() {
        if (mStateView != null) {
            mStateView.showContent();
        }
    }

    protected void setState(@StateView.STATE int result) {
        if (mStateView != null) {
            mStateView.setState(result);
        }
    }

    protected abstract View createSuccessView();

    protected abstract void loadData();
}
