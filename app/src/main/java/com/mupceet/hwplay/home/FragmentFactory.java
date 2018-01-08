package com.mupceet.hwplay.home;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.mupceet.hwplay.base.BaseFragment;

/**
 * Created by lgz on 1/3/18.
 */

public class FragmentFactory {
    /**
     * "推荐", "分类", "排行", "管理", "我的"
     */
    public static final int TAB_RECOMMEND = 0;
    public static final int TAB_CATEGORY = 1;
    public static final int TAB_TOP = 2;
    public static final int TAB_APP_MANAGER = 3;
    public static final int TAB_ME = 4;


    private static SparseArray<BaseFragment> mFragments = new SparseArray<>();

    public static Fragment createFragment(int index) {
        // TODO: 1/3/18 根据需求写BaseFragment，然后创建时创建具体的Fragment，这里先使用Fragment代替一下
        BaseFragment fragment = mFragments.get(index);
        if (fragment == null) {
            switch (index) {
                case TAB_RECOMMEND:
                    fragment = new RecommendFragment();
                    break;
                case TAB_CATEGORY:
                    fragment = new CategoryFragment();
                    break;
                case TAB_TOP:
                    fragment = new TopFragment();
                    break;
                case TAB_APP_MANAGER:
                    fragment = new AppManagerFragment();
                    break;
                case TAB_ME:
                    fragment = new MyFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Not support this type fragment");
            }
            mFragments.put(index, fragment);
        }
        return fragment;
    }

}
