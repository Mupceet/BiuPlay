package com.mupceet.mvpdagger.main.di.component;

import com.mupceet.mvpdagger.main.MainActivity2;
import com.mupceet.mvpdagger.main.MainActivity3;
import com.mupceet.mvpdagger.main.di.MainModule;
import com.mupceet.mvpdagger.main.di.module.MainModule3;
import com.mupceet.mvpdagger.main.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by lgz on 18-1-19.
 */
@ActivityScope
@Component(modules = {MainModule3.class})
public interface MainComponent3 {

    void inject(MainActivity2 mainActivity2);
    void inject(MainActivity3 mainActivity3);
}
