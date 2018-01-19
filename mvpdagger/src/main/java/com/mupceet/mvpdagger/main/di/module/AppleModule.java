package com.mupceet.mvpdagger.main.di.module;

import com.mupceet.mvpdagger.main.di.Apple;
import com.mupceet.mvpdagger.main.di.scope.Type;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lgz on 18-1-17.
 */
@Module
public class AppleModule {

    @Provides
    @Singleton
    @Type("normal")
    Apple provideApple() {
        return new Apple();
    }

    @Provides
    @Singleton
    @Type("color")
    Apple provideColorApple() {
        return new Apple("red");
    }
}
