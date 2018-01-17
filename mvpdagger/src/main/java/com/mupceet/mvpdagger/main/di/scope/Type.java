package com.mupceet.mvpdagger.main.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by lgz on 18-1-17.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    String value() default "";
}
