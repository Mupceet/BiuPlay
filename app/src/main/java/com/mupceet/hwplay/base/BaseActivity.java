package com.mupceet.hwplay.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.gyf.barlibrary.ImmersionBar;
import com.mupceet.hwplay.R;
import com.mupceet.hwplay.utils.AppActivityManager;

import butterknife.ButterKnife;

/**
 * Created by lgz on 8/5/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ImmersionBar mImmersionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppActivityManager.getInstance().addActivity(this);
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

        initView();
    }


    @Override
    protected void onDestroy() {
        AppActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
    }

    /**
     * 初始化布局
     */
    protected abstract int setLayoutId();

    /**
     * 初始化View
     */
    protected abstract void initView();
    /// example:
    // protected void initView() {
    //     sp = getSharedPreferences("appStore",Context.MODE_PRIVATE);
    //     if(!sp.getBoolean("isFirst",true)){
    //         startActivity(new Intent(this,HomeActivity.class));
    //         finish();
    //     }
    // }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    private void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }
}
