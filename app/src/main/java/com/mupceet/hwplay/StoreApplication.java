package com.mupceet.hwplay;

import android.app.Application;
import android.os.Handler;

import com.blankj.utilcode.util.Utils;

/**
 * Created by lgz on 1/7/18.
 */

public class StoreApplication extends Application {

    private static int sMainThreadTid;
    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        sMainThreadTid = android.os.Process.myTid();
        sHandler = new Handler();
    }

    public static int getMainThreadTid() {
        return sMainThreadTid;
    }
    public static Handler getHandler() {
        return sHandler;
    }
}
