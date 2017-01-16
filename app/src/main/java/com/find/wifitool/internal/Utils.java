package com.find.wifitool.internal;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

/**
 * Created by akshay on 30/12/16.
 */

public class Utils {

    private Utils() {
    }

    public static boolean isWifiAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    // Checking Location service status
    public static boolean isLocationAvailable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return (gps_enabled || network_enabled);
    }

    // Checking if we have location permission or not
    public static boolean hasAnyLocationPermission(Context context) {
        int fineLocationPermission = context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        int coarseLocationPermission = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
        return fineLocationPermission == 0 || coarseLocationPermission == 0;
    }

}
