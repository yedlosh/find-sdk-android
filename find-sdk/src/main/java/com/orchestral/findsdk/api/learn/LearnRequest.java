package com.orchestral.findsdk.api.learn;

import com.google.gson.annotations.SerializedName;
import com.orchestral.findsdk.api.common.AccessPoint;

import java.util.List;

/**
 * //    {
 //        "group":"some group",
 //            "username":"some user",
 //            "location":"some place",
 //            "time":12309123,
 //            "wifi-fingerprint":[
 //        {
 //            "mac":"AA:AA:AA:AA:AA:AA",
 //                "rssi":-45
 //        },
 //        {
 //            "mac":"BB:BB:BB:BB:BB:BB",
 //                "rssi":-55
 //        }
 //        ]
 //    }

 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class LearnRequest {

    public LearnRequest(String group, String username, String location, Long time, List<AccessPoint> wifiFingerprint) {
        this.group = group;
        this.username = username;
        this.location = location;
        this.time = time;
        this.wifiFingerprint = wifiFingerprint;
    }

    public final String group;
    public final String username;
    public final String location;
    public final Long time;

    @SerializedName("wifi-fingerprint")
    public List<AccessPoint> wifiFingerprint;

    @Override
    public String toString() {
        return "LearnRequest{" +
                "group='" + group + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                ", time=" + time +
                ", wifiFingerprint=" + wifiFingerprint +
                '}';
    }
}
