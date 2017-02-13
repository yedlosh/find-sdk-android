package com.orchestral.findsdksample;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdksample.learn.LearnFragment;
import com.orchestral.findsdksample.settings.view.SettingsFragment;
import com.orchestral.findsdksample.track.TrackFragment;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ViewGroup rootView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = (ViewGroup) findViewById(R.id.root_view);

        initToolbar();
        initViewPager();

        requestRuntimePermissionsIfNeeded();

        // Calling function to set some default values if its our first run
        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        initDefaultPreferencesIfNeeded();

        initNavigationMenu();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MainScreensPagerAdapter(getSupportFragmentManager()));
    }

    private void initNavigationMenu() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_learn:
                                viewPager.setCurrentItem(0, true);
                                break;
                            case R.id.action_track:
                                viewPager.setCurrentItem(1, true);
                                break;
                            case R.id.action_settings:
                                viewPager.setCurrentItem(2, true);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void requestRuntimePermissionsIfNeeded() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(SnackbarOnDeniedPermissionListener.Builder
                        .with(rootView, R.string.location_permission_rationale)
                        .withOpenSettingsButton(R.string.settings)
                        .build())
                .check();
    }

    // Setting default values in case fo 1st run
    private void initDefaultPreferencesIfNeeded() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isFirstRun = !sharedPreferences.contains(Constants.IS_FIRST_RUN);

        if (isFirstRun) {
            editor.putString(Constants.USER_NAME, Constants.DEFAULT_USERNAME);
            editor.putString(Constants.SERVER_NAME, Constants.DEFAULT_SERVER);
            editor.putString(Constants.GROUP_NAME, Constants.DEFAULT_GROUP);
            editor.putInt(Constants.TRACK_INTERVAL, Constants.DEFAULT_TRACKING_INTERVAL);
            editor.putInt(Constants.LEARN_PERIOD, Constants.DEFAULT_LEARNING_PERIOD);
            editor.putInt(Constants.LEARN_INTERVAL, Constants.DEFAULT_LEARNING_INTERVAL);
            editor.putBoolean(Constants.IS_FIRST_RUN, false);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_FIND_github:
                launchBrowserWithUrl(Constants.FIND_GITHUB_URL);
                break;
            case R.id.action_app_github:
                launchBrowserWithUrl(Constants.FIND_APP_URL);
                break;
            case R.id.action_issue:
                launchBrowserWithUrl(Constants.FIND_ISSUES_URL);
                break;
            case R.id.action_Find:
                launchBrowserWithUrl(Constants.FIND_WEB_URL);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchBrowserWithUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private static class MainScreensPagerAdapter extends FragmentStatePagerAdapter {

        public MainScreensPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LearnFragment();
                case 1:
                    return new TrackFragment();
                case 2:
                    return new SettingsFragment();
            }
            throw new IllegalArgumentException("illegal view pager position");
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
