package com.orchestral.findsdk.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.orchestral.findsdk.wifi.model.WifiAccessPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class AndroidWifiFingerprintAgent implements WifiFingerprintAgent {

    private static final Set<Character> AD_HOC_HEX_VALUES =
            new HashSet<Character>(Arrays.asList('2','6', 'a', 'e', 'A', 'E'));

    private Context applicationContext;

    public AndroidWifiFingerprintAgent(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<WifiAccessPoint> getWifiFingerprint() {
        List<WifiAccessPoint> accessPoints = new ArrayList<>();
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        // TODO this will return an empty list on Android 6.0 and up if runtime location permissions
        // haven't been granted by the user. Location services must also be enabled, apparently.
        List<ScanResult> scanResults = wifiManager.getScanResults();

        for (ScanResult result : scanResults) {
            if (shouldLogAccessPoint(result)) {
                accessPoints.add(WifiAccessPoint.create(result.BSSID, result.level));
            }
        }

//        Timber.d("Access points: " + accessPoints.toString());

        return accessPoints;
    }

    /**
     * Returns true if the given scan should be logged, or false if it is an
     * ad-hoc AP or if it is an AP that has opted out of Google's collection
     * practices.
     */
    private static boolean shouldLogAccessPoint(final ScanResult scanResult) {
        // Only apply this test if we have exactly 17 character long BSSID which should
        // be the case.
        final char secondNybble = scanResult.BSSID.length() == 17 ? scanResult.BSSID.charAt(1) : ' ';

        return (!AD_HOC_HEX_VALUES.contains(secondNybble));
    }

}
