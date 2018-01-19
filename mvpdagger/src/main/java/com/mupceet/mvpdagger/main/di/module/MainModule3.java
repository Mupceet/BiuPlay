package com.mupceet.mvpdagger.main.di.module;

import com.mupceet.mvpdagger.main.di.Apple;
import com.mupceet.mvpdagger.main.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lgz on 18-1-19.
 */
@Module
public class MainModule3 {

    @Provides
    @ActivityScope
    public Apple privideApple() {
        return new Apple();
    }
}
