package com.mupceet.hwplay.widget.state;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class StateLayout extends FrameLayout {

    /**
     * loading 加载id
     */
    public static final int LAYOUT_LOADING_ID = 1;

    /**
     * 内容id
     */
    public static final int LAYOUT_CONTENT_ID = 2;

    /**
     * 异常id，可重试
     */
    public static final int LAYOUT_ERROR_ID = 3;

    /**
     * 网络异常id，可重试
     */
    public static final int LAYOUT_NETWORK_ERROR_ID = 4;

    /**
     * 空数据id，可重试
     */
    public static final int LAYOUT_EMPTY_ID = 5;

    /**
     * 存放布局集合
     */
    private SparseArray<View> layoutSparseArray = new SparseArray<>();

    /**
     * 布局管理器
     */
    private StateLayoutManager mStateLayoutManager;


    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStateLayoutManager(StateLayoutManager stateLayoutManager) {
        mStateLayoutManager = stateLayoutManager;

        addAllLayoutToRootLayout();
    }

    private void addAllLayoutToRootLayout() {
        if (mStateLayoutManager.contentLayoutResId != 0) {
            addLayoutResId(mStateLayoutManager.contentLayoutResId, StateLayout.LAYOUT_CONTENT_ID);
        }
        if (mStateLayoutManager.loadingLayoutResId != 0) {
            addLayoutResId(mStateLayoutManager.loadingLayoutResId, StateLayout.LAYOUT_LOADING_ID);
        }

        if (mStateLayoutManager.emptyDataVs != null) {
            addView(mStateLayoutManager.emptyDataVs);
        }
        if (mStateLayoutManager.errorVs != null) {
            addView(mStateLayoutManager.errorVs);
        }
        if (mStateLayoutManager.netWorkErrorVs != null) {
            addView(mStateLayoutManager.netWorkErrorVs);
        }
    }

    private void addLayoutResId(@LayoutRes int layoutResId, int id) {
        View resView = LayoutInflater.from(mStateLayoutManager.context).inflate(layoutResId, null);
        layoutSparseArray.put(id, resView);
        addView(resView);
    }

    /**
     * 显示loading
     */
    public void showLoading() {
        if (layoutSparseArray.get(LAYOUT_LOADING_ID) != null) {
            showHideViewById(LAYOUT_LOADING_ID);
        }
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (layoutSparseArray.get(LAYOUT_CONTENT_ID) != null) {
            showHideViewById(LAYOUT_CONTENT_ID);
        }
    }

    /**
     * 显示空数据
     */
    public void showEmptyData(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_EMPTY_ID)) {
            showHideViewById(LAYOUT_EMPTY_ID);
            emptyDataViewAddData(iconImage, textTip);
        }
    }

    private void emptyDataViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return;
        }
        View emptyDataView = layoutSparseArray.get(LAYOUT_EMPTY_ID);
        View iconImageView = emptyDataView.findViewById(mStateLayoutManager.emptyDataIconImageId);
        View textView = emptyDataView.findViewById(mStateLayoutManager.emptyDataTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }

        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    public void showLayoutEmptyData(Object... objects) {
        if (inflateLayout(LAYOUT_EMPTY_ID)) {
            showHideViewById(LAYOUT_EMPTY_ID);

            AbstractLayout emptyDataLayout = mStateLayoutManager.emptyDataLayout;
            if (emptyDataLayout != null) {
                emptyDataLayout.setData(objects);
            }
        }
    }

    /**
     * 显示网络异常
     */
    public void showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID)) {
            showHideViewById(LAYOUT_NETWORK_ERROR_ID);
        }
    }

    /**
     * 显示异常
     */
    public void showError(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);
            errorViewAddData(iconImage, textTip);
        }
    }

    private void errorViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return;
        }
        View errorView = layoutSparseArray.get(LAYOUT_ERROR_ID);
        View iconImageView = errorView.findViewById(mStateLayoutManager.emptyDataIconImageId);
        View textView = errorView.findViewById(mStateLayoutManager.emptyDataTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }

        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    public void showLayoutError(Object... objects) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);

            AbstractLayout errorLayout = mStateLayoutManager.errorLayout;
            if (errorLayout != null) {
                errorLayout.setData(objects);
            }
        }
    }

    /**
     * 根据ID显示隐藏布局
     *
     * @param id 对应布局
     */
    private void showHideViewById(int id) {
        for (int i = 0; i < layoutSparseArray.size(); i++) {
            int key = layoutSparseArray.keyAt(i);
            View valueView = layoutSparseArray.valueAt(i);
            //显示该view
            if (key == id) {
                valueView.setVisibility(View.VISIBLE);
                if (mStateLayoutManager.onShowHideViewListener != null) {
                    mStateLayoutManager.onShowHideViewListener.onShowView(valueView, key);
                }
            } else {
                if (valueView.getVisibility() != View.GONE) {
                    valueView.setVisibility(View.GONE);
                    if (mStateLayoutManager.onShowHideViewListener != null) {
                        mStateLayoutManager.onShowHideViewListener.onHideView(valueView, key);
                    }
                }
            }
        }
    }

    private boolean inflateLayout(int id) {
        boolean isShow = true;
        if (layoutSparseArray.get(id) != null) {
            return true;
        }
        switch (id) {
            case LAYOUT_NETWORK_ERROR_ID:
                if (mStateLayoutManager.netWorkErrorVs != null) {
                    View view = mStateLayoutManager.netWorkErrorVs.inflate();
                    initRetyrView(view, mStateLayoutManager.netWorkErrorRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            case LAYOUT_ERROR_ID:
                if (mStateLayoutManager.errorVs != null) {
                    View view = mStateLayoutManager.errorVs.inflate();
                    if (mStateLayoutManager.errorLayout != null) {
                        mStateLayoutManager.errorLayout.setView(view);
                    }
                    initRetyrView(view, mStateLayoutManager.errorRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            case LAYOUT_EMPTY_ID:
                if (mStateLayoutManager.emptyDataVs != null) {
                    View view = mStateLayoutManager.emptyDataVs.inflate();
                    if (mStateLayoutManager.emptyDataLayout != null) {
                        mStateLayoutManager.emptyDataLayout.setView(view);
                    }
                    initRetyrView(view, mStateLayoutManager.emptyDataRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            default:
                // do nothing
        }
        return isShow;
    }

    /**
     * 重试加载
     */
    private void initRetyrView(View view, int id) {
        View retryView = view.findViewById(mStateLayoutManager.retryViewId != 0 ? mStateLayoutManager.retryViewId : id);
        if (retryView == null || mStateLayoutManager.onRetryListener == null) {
            return;
        }
        retryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStateLayoutManager.onRetryListener.onRetry();
            }
        });
    }
}
