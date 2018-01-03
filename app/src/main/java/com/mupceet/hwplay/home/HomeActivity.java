package com.mupceet.hwplay.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mupceet.hwplay.R;
import com.mupceet.hwplay.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mMainViewpager;

    private List<Fragment> mFragments;
    private String[] mTitles = {"推荐", "分类", "排行", "管理", "我的"};
    private FixPagerAdapter mFixPagerAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        initViewPagerFragment();
    }

    private void initViewPagerFragment() {
        mFixPagerAdapter = new FixPagerAdapter(getSupportFragmentManager());
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mFragments.add(FragmentFactory.createFragment(i));
        }
        mFixPagerAdapter.setTitles(mTitles);
        mFixPagerAdapter.setFragments(mFragments);

        mMainViewpager.setAdapter(mFixPagerAdapter);

        mTabLayout.setupWithViewPager(mMainViewpager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

}
