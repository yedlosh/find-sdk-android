package com.orchestral.findsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orchestral.findsdk.api.common.AccessPoint;
import com.orchestral.findsdk.api.learn.LearnApiService;
import com.orchestral.findsdk.api.learn.LearnRequest;
import com.orchestral.findsdk.api.learn.LearnResponse;
import com.orchestral.findsdk.api.track.TrackApiService;
import com.orchestral.findsdk.api.track.TrackRequest;
import com.orchestral.findsdk.api.track.TrackResponse;
import com.orchestral.findsdk.wifi.AndroidWifiFingerprintAgent;
import com.orchestral.findsdk.wifi.WifiFingerprintAgent;
import com.orchestral.findsdk.wifi.model.WifiAccessPoint;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */

public class FindClient {

    final String baseUrl;
    final String group;
    final String username;
    final WifiFingerprintAgent wifiFingerprintAgent;

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private TrackApiService trackApiService;
    private LearnApiService learnApiService;

    public FindClient(String baseUrl,
                      String group,
                      String username,
                      WifiFingerprintAgent wifiFingerprintAgent,
                      OkHttpClient okHttpClient) {
        this.baseUrl = baseUrl;
        this.group = group;
        this.username = username;
        this.wifiFingerprintAgent = wifiFingerprintAgent;
        this.okHttpClient = okHttpClient;
    }

    /**
     * Attempts to determines the device's location using data on the currently visible WiFi
     * access points ("fingerprint").
     *
     * Requires the ACCESS_FINE_LOCATION permission to be granted.
     */
    public Single<String> track() {
        return getTrackService().track(buildTrackRequest())
                .flatMap(new Function<TrackResponse, SingleSource<TrackResponse>>() {
                    @Override
                    public SingleSource<TrackResponse> apply(TrackResponse trackResponse) throws Exception {
                        if (!trackResponse.success) {
                            return Single.error(new RuntimeException("Track request failed (success = false)"));
                        }
                        return Single.just(trackResponse);
                    }
                })
                .map(new Function<TrackResponse, String>() {
                    @Override
                    public String apply(TrackResponse trackResponse) throws Exception {
                        return trackResponse.location;
                    }
                });
    }

    @NonNull
    private TrackApiService getTrackService() {
        if (trackApiService == null) {
            trackApiService = getApiServiceGenerator().create(TrackApiService.class);
        }

        return trackApiService;
    }

    @NonNull
    private Retrofit getApiServiceGenerator() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    @NonNull
    private TrackRequest buildTrackRequest() {
        return new TrackRequest(group, username, getCurrentTimeInSeconds(), getAccessPoints());
    }

    private long getCurrentTimeInSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    private List<AccessPoint> getAccessPoints() {
        return Observable.fromIterable(wifiFingerprintAgent.getWifiFingerprint())
                .map(new Function<WifiAccessPoint, AccessPoint>() {
                    @Override
                    public AccessPoint apply(WifiAccessPoint wifiAccessPoint) throws Exception {
                        return new AccessPoint(
                                wifiAccessPoint.macAddress(),
                                wifiAccessPoint.signalStrength());
                    }
                })
                .toList()
                .blockingGet();
    }

    /**
     * Trains the indoor location model by associating the current WiFi fingerprint (visible access
     * points and signal strengths) with a descriptive location string.
     *
     * Note that a single learn call will not be sufficient to train the server for a location, optimally
     * you want to send ~100 wifi fingerprints.
     *
     * @see <a href="https://www.internalpositioning.com/faq/">FIND FAQ</a>
     *
     * Requires the ACCESS_FINE_LOCATION permission to be granted.
     */
    public Completable learn(String location) {
        return getLearnService()
                .learn(buildLearnRequest(location))
                .doOnSuccess(new Consumer<LearnResponse>() {
                    @Override
                    public void accept(LearnResponse learnResponse) throws Exception {
                        if (!learnResponse.success) {
                            throw new RuntimeException("Learn request failed (success = false)");
                        }
                    }
                })
                .toCompletable();
    }

    private LearnApiService getLearnService() {
        if (learnApiService == null) {
            learnApiService = getApiServiceGenerator().create(LearnApiService.class);
        }

        return learnApiService;
    }

    private LearnRequest buildLearnRequest(String location) {
        return new LearnRequest(group, username, location, getCurrentTimeInSeconds(), getAccessPoints());
    }

    public static final class Builder {

        String baseUrl;
        String group;
        String username;
        OkHttpClient okHttpClient;
        WifiFingerprintAgent wifiFingerprintAgent;

        public Builder() {
        }

        public Builder(Context applicationContext) {
            wifiFingerprintAgent = new AndroidWifiFingerprintAgent(applicationContext);
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder wifiFingerprintAgent(WifiFingerprintAgent wifiFingerprintAgent) {
            this.wifiFingerprintAgent = wifiFingerprintAgent;
            return this;
        }

        public FindClient build() {
            if (baseUrl == null || group == null || username == null || wifiFingerprintAgent == null) {
                throw new IllegalStateException("One or more required fields are null");
            }

            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }

            return new FindClient(baseUrl, group, username, wifiFingerprintAgent, okHttpClient);
        }
    }

}
