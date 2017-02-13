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

    private FindClient findClient;

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogger();
        setupFindClient();
    }

    private void setupFindClient() {
        boolean httpLoggingEnabled = (BuildConfig.DEBUG == true);

        // TODO get these values from user settings
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
