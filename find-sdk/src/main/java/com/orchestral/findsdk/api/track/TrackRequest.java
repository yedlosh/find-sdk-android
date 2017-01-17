package com.orchestral.findsdk.api.track;

import com.google.gson.annotations.SerializedName;
import com.orchestral.findsdk.api.common.AccessPoint;

import java.util.List;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class TrackRequest {

    public TrackRequest(String group, String username, Long time, List<AccessPoint> wifiFingerprint) {
        this.group = group;
        this.username = username;
        this.time = time;
        this.wifiFingerprint = wifiFingerprint;
    }

    public final String group;
    public final String username;
    public final Long time;

    @SerializedName("wifi-fingerprint")
    public final List<AccessPoint> wifiFingerprint;

    @Override
    public String toString() {
        return "TrackRequest{" +
                "group='" + group + '\'' +
                ", username='" + username + '\'' +
                ", time=" + time +
                ", wifiFingerprint=" + wifiFingerprint +
                '}';
    }
}
