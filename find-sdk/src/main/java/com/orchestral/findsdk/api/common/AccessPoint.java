package com.orchestral.findsdk.api.common;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class AccessPoint {
    public AccessPoint(String mac, Integer rssi) {
        this.mac = mac;
        this.rssi = rssi;
    }

    public final String mac;
    public final Integer rssi;
}
