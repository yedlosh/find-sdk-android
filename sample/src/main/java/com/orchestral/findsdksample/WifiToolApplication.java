package com.orchestral.findsdksample;

import android.app.Application;

import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdk.FindClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */

public class WifiToolApplication extends Application {

    private FindClient findClient;

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogger();
        setupFindClient();
    }

    private void setupFindClient() {
        boolean httpLoggingEnabled = (BuildConfig.DEBUG == true);

        findClient = new FindClient.Builder(this)
                    .okHttpClient(buildOkHttpClient(httpLoggingEnabled))
                    .baseUrl(Constants.DEFAULT_SERVER)
                    .group("LucidLynx")
                    .username("user")
                    .build();
    }

    private OkHttpClient buildOkHttpClient(boolean loggingEnabled) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (loggingEnabled) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    private void setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public FindClient getFindClient() {
        return findClient;
    }

}
