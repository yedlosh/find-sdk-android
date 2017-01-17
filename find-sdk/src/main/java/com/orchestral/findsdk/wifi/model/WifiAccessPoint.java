package com.orchestral.findsdk.wifi.model;


import com.google.auto.value.AutoValue;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */
@AutoValue
public abstract class WifiAccessPoint {
    public abstract String macAddress();
    public abstract int signalStrength();

    public static WifiAccessPoint create(String macAddress, int signalStrength) {
        return new AutoValue_WifiAccessPoint(macAddress, signalStrength);
    }
}
