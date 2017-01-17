package com.find.wifitool.location;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by akshay on 31/12/16.
 */

public class Location implements Parcelable {

    public String wifiName;
    public String grpName;
    public String userName;

    // constructor
    public Location(String wifi, String group, String user) {
        this.wifiName = wifi;
        this.grpName = group;
        this.userName = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
