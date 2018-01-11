package com.mupceet.mvpdagger.main.di;

import com.mupceet.mvpdagger.main.contract.MainContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lgz on 1/10/18.
 */
@Module
public class MainModule {
    private MainContract.View mainView;

    public MainModule(MainContract.View view){
        this.mainView = view ;
    }

    @Provides MainContract.View provideMainView(){
        return mainView;
    }

}
