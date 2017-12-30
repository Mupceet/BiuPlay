package com.mupceet.hwplay.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * 管理activity
 */

public class AppActivityManager {
    private static Stack<Activity> mActivityStack;
    private static AppActivityManager mAppManager;

    private AppActivityManager() {
    }

    /**
     * 单一实例
     */
    public static AppActivityManager getInstance() {
        if (mAppManager == null) {
            synchronized (AppActivityManager.class) {
                if (mAppManager == null) {
                    mAppManager = new AppActivityManager();
                }
            }
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }
    /**
     * 移除Activity到堆外
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }


    /**
     * 获取栈顶Activity
     */
    public Activity getTopActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束栈顶Activity
     */
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    private void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
//        try {
//            killAllActivity();
//            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());
//            System.exit(0);
//        } catch (Exception e) {
//            Log.e("AppActivityManager",""+e);
//        }

        // 1. 先清除所有 Activity
        killAllActivity();
        // 2. 退出当前进程
        System.exit(0);
        // System.exit(0)和System.exit(1)的区别
        // System.exit(0)：正常退出；
        // System.exit(1)：非正常退出，通常这种退出方式应该放在catch块中。
    }
}
