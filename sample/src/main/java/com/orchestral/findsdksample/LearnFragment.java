package com.orchestral.findsdksample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.orchestral.findsdksample.database.Event;
import com.orchestral.findsdksample.database.InternalDatabase;
import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdksample.internal.Utils;
import com.orchestral.findsdksample.location.Location;
import com.orchestral.findsdksample.location.LocationArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.orchestral.findsdksample.internal.Utils.showLocationServicePromptIfNeeded;

/**
 * Created by akshay on 30/12/16.
 */
public class LearnFragment extends Fragment {

    private Handler handler = new Handler();

    private SharedPreferences sharedPreferences;
    private String strUsername;
    private String strServer;
    private String strGroup;
    private int learnIntervalVal;
    private int learnPeriodVal;

    private List<Location> arrayList;
    private LocationArrayAdapter locationArrayAdapter;


    // Required empty public constructor
    public LearnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating WiFi list adapter
        arrayList = new ArrayList<>();
        locationArrayAdapter = new LocationArrayAdapter(getActivity(), R.layout.location_list_item, arrayList);

        // Getting values from Shared prefs for Learning
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        strGroup = sharedPreferences.getString(Constants.GROUP_NAME, Constants.DEFAULT_GROUP);
        strUsername = sharedPreferences.getString(Constants.USER_NAME, Constants.DEFAULT_USERNAME);
        strServer = sharedPreferences.getString(Constants.SERVER_NAME, Constants.DEFAULT_SERVER);
        learnIntervalVal = sharedPreferences.getInt(Constants.LEARN_INTERVAL, Constants.DEFAULT_LEARNING_INTERVAL);
        learnPeriodVal = sharedPreferences.getInt(Constants.LEARN_PERIOD, Constants.DEFAULT_LEARNING_PERIOD);

        // Initialising internal DB n retrieving values from it to fill our listView
        final InternalDatabase internalDatabase = new InternalDatabase(getActivity());

        List<Event> eventList = internalDatabase.getAllEvents();
        for (Event event : eventList) {
            Location wifi = new Location(event.getWifiName(), event.getWifiGroup(), event.getWifiUser());
            locationArrayAdapter.add(wifi);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showLocationServicePromptIfNeeded(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn, container, false);

        setupAddLocationButton(rootView);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(locationArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Add your custom Fragment here
            }
        });

        return rootView;
    }

    private void setupAddLocationButton(View rootView) {
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Add location");
                final EditText editText = new EditText(getActivity());
                editText.setHintTextColor(getResources().getColor(R.color.hintTextColor));
                editText.setHint(Constants.DEFAULT_LOCATION_NAME);
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strLocationName = editText.getText().toString();
                        InternalDatabase internalDatabase = new InternalDatabase(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.LOCATION_NAME, strLocationName).apply();

                        dialog.dismiss();
                        Location location = new Location(strLocationName, strGroup, strUsername);
                        insertIntoList(location);
                        internalDatabase.addEvent(new Event(strLocationName, strGroup, strUsername));
                        handler.post(runnableCode);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
            }
        });
    }

    // Insert new location into listView
    private void insertIntoList(Location wifi) {
        locationArrayAdapter.add(wifi);

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Learning");
        progress.setMessage("Please wait while we are collecting the Wifi APs around you...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
                handler.removeCallbacks(runnableCode);
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, learnPeriodVal * 60 * 1000);
    }

    // Timers to keep track of our Learning period
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (!Utils.isWifiAvailable(getContext())) {
                return;
            }

            if (Build.VERSION.SDK_INT >= 23 && Utils.hasAnyLocationPermission(getContext())) {
                postWifiDataForLocation();
            } else {
                postWifiDataForLocation();
            }

            handler.postDelayed(runnableCode, learnIntervalVal * 1000);
        }
    };

    private void postWifiDataForLocation() {
        WifiToolApplication app = (WifiToolApplication) getActivity().getApplication();
        app.getFindClient().learn(getCurrentLocationName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("subscribed to learn completable");
                    }

                    @Override
                    public void onComplete() {
                        Utils.logMeToast(getActivity(), "AP data submission success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Timber.e("onError: " + e.toString(), e);
                    }
                });
    }

    @NonNull
    private String getCurrentLocationName() {
        return sharedPreferences.getString(Constants.LOCATION_NAME, "");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(runnableCode);
    }

}
