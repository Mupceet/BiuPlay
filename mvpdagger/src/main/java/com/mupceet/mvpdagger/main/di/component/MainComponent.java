package com.mupceet.mvpdagger.main.di.component;

import com.mupceet.mvpdagger.main.MainActivity;
import com.mupceet.mvpdagger.main.di.MainModule;
import com.mupceet.mvpdagger.main.di.module.AppleModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lgz on 1/10/18.
 */

@Singleton
@Component(modules = {MainModule.class, AppleModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
