package com.mupceet.mvpdagger.main.di.module;

import com.mupceet.mvpdagger.main.di.Apple;
import com.mupceet.mvpdagger.main.di.scope.Type;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lgz on 18-1-17.
 */
@Module
public class AppleModule {

    @Provides
    @Type("normal")
    Apple provideApple() {
        return new Apple();
    }

    @Provides
    @Type("color")
    Apple provideColorApple() {
        return new Apple("red");
    }
}
