package com.orchestral.findsdksample.learn.location;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orchestral.findsdksample.R;

import java.util.List;

/**
 * Created by akshay on 30/12/16.
 */

public class LocationArrayAdapter extends ArrayAdapter<Location> {

    public LocationArrayAdapter(Context mContext, @LayoutRes int layoutResourceId, List<Location> objects) {
        super(mContext, layoutResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Location locationItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_list_item, parent, false);
        }

        // Getting UI components
        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        TextView locationGroup = (TextView) convertView.findViewById(R.id.fieldGrpName);
        TextView locationUser = (TextView) convertView.findViewById(R.id.fieldUsrName);

        // Setting UI components
        locationName.setText(locationItem.wifiName);
        locationGroup.setText(locationItem.grpName);
        locationUser.setText(locationItem.userName);

        return convertView;
    }
}
