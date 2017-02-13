package com.orchestral.findsdksample;

import android.app.Application;
import android.content.SharedPreferences;

import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdk.FindClient;
import com.orchestral.findsdksample.settings.UserSettings;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */

public class WifiToolApplication extends Application implements UserSettings {

    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogger();
    }

    private void setupLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public FindClient getFindClient() {
        return new FindClient.Builder(this)
                .okHttpClient(getOkHttpClient())
                .baseUrl(getServerUrl())
                .group(getGroup())
                .username(getUsername())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (shouldEnableHttpLogging()) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

    private boolean shouldEnableHttpLogging() {
        return BuildConfig.DEBUG == true;
    }

    @Override
    public String getServerUrl() {
        return getSharedPreferencesForUserSettings().getString(Constants.SERVER_NAME, Constants.DEFAULT_SERVER);
    }

    @Override
    public String getGroup() {
        return getSharedPreferencesForUserSettings().getString(Constants.GROUP_NAME, Constants.DEFAULT_GROUP);
    }

    @Override
    public String getUsername() {
        return getSharedPreferencesForUserSettings().getString(Constants.USER_NAME, Constants.DEFAULT_USERNAME);
    }

    @Override
    public Integer getLearnScanPeriodInSeconds() {
        return getSharedPreferencesForUserSettings().getInt(Constants.LEARN_INTERVAL, Constants.DEFAULT_LEARNING_INTERVAL);
    }

    @Override
    public Integer getTotalLearnTimeInMinutes() {
        return getSharedPreferencesForUserSettings().getInt(Constants.LEARN_PERIOD, Constants.DEFAULT_LEARNING_PERIOD);
    }

    @Override
    public Integer getTrackScanPeriodInSeconds() {
        return getSharedPreferencesForUserSettings().getInt(Constants.TRACK_INTERVAL, Constants.DEFAULT_TRACKING_INTERVAL);
    }

    private SharedPreferences getSharedPreferencesForUserSettings() {
        return getSharedPreferences(Constants.PREFS_NAME, 0);
    }

}
