package com.thescore.dagger;

import android.content.Context;

import com.thescore.database.DBHelper;
import com.thescore.utils.RequestHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private final Context appContext;

    public AppContextModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @AppContext
    @Singleton
    Context appContext() {
        return appContext;
    }

    @Provides
    RequestHelper provideRequestHelper() {
        return new RequestHelper();
    }

    @Provides
    @Singleton
    DBHelper provideDbHelper() {
        return new DBHelper(appContext);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface AppContext {
    }
}
