package com.find.wifitool.internal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import timber.log.Timber;

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

    public static void showLocationServicePromptIfNeeded(final FragmentActivity activity) {
        if (!isLocationAvailable(activity)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Location service is not On. Users running Android M and above have to turn on location services for FIND to work properly");
            dialog.setPositiveButton("Enable Locations service", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    logMeToast(activity, "Thank you!! Getting things in place.");
                }
            });
            dialog.show();
        }
    }

    public static void logMeToast(final Activity activity, String message) {
        Timber.d(message);
        toast(activity, message);
    }

    public static void toast(final Activity activity, String s) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }
}
