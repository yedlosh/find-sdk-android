package com.orchestral.findsdksample;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdksample.internal.Utils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by akshay on 30/12/16.
 */
public class TrackFragment extends Fragment {

    private String strUsername;
    private String strServer;
    private String strGroup;
    private int trackInterval;

    private TextView currLocView;

    private Handler handler = new Handler();

    // Required empty public constructor
    public TrackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting values from Shared prefs for Tracking
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        strGroup = sharedPreferences.getString(Constants.GROUP_NAME, Constants.DEFAULT_GROUP);
        strUsername = sharedPreferences.getString(Constants.USER_NAME, Constants.DEFAULT_USERNAME);
        strServer = sharedPreferences.getString(Constants.SERVER_NAME, Constants.DEFAULT_SERVER);
        trackInterval = sharedPreferences.getInt(Constants.TRACK_INTERVAL, Constants.DEFAULT_TRACKING_INTERVAL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.showLocationServicePromptIfNeeded(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track, container, false);
        currLocView = (TextView) rootView.findViewById(R.id.labelLocationName);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            startTrackingLocation();
        } else {
            stopTrackingLocation();
        }
    }

    private void startTrackingLocation() {
        handler.post(runnableCode);
    }

    private void stopTrackingLocation() {
        handler.removeCallbacks(runnableCode);
    }

    // Timers to keep track of our Tracking period
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (!Utils.isWifiAvailable(getContext())) {
                return;
            }

            if (Build.VERSION.SDK_INT >= 23 && Utils.hasAnyLocationPermission(getContext())) {
                pollTracker();
            } else {
                pollTracker();
            }

            handler.postDelayed(runnableCode, trackInterval * 1000);
        }
    };

    private void pollTracker() {
        WifiToolApplication app = (WifiToolApplication) getActivity().getApplication();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO inform user that permission is needed
            return;
        }

        app.getFindClient()
                .track()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("subscribed to track API service");
                    }

                    @Override
                    public void onSuccess(String location) {
                        setCurrentLocationText(location);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.e("onError: " + e.toString(), e);
                    }
                });
    }

    private void setCurrentLocationText(String currLocation) {
        currLocView.setTextColor(getResources().getColor(R.color.currentLocationColor));
        currLocView.setText(currLocation);
    }

}
