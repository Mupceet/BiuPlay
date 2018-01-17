package com.mupceet.mvpdagger.main.di;

import com.mupceet.mvpdagger.main.MainActivity;
import com.mupceet.mvpdagger.main.di.module.AppleModule;

import dagger.Component;

/**
 * Created by lgz on 1/10/18.
 */
@Component(modules = {MainModule.class, AppleModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
