package com.orchestral.findsdksample.settings.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.orchestral.findsdksample.R;
import com.orchestral.findsdksample.internal.Constants;
import com.orchestral.findsdksample.settings.UserSettings;

/**
 * Created by akshay on 30/12/16.
 */

public class SettingsFragment extends Fragment {

    private TextView labelUserName;
    private TextView labelGroupName;
    private TextView labelServerName;
    private TextView learnInterval;
    private TextView trackInterval;
    private TextView learnPeriod;
    private TextView labelLearnInterval;
    private TextView labelTrackInterval;
    private TextView labelLearnPeriod;

    private UserSettings userSettings;
    private SharedPreferences sharedPreferences;

    // Required empty public constructor
    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSettings = (UserSettings) getActivity().getApplication();
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Getting all the UI components
        labelUserName = (TextView) rootView.findViewById(R.id.fieldUserName);
        labelGroupName = (TextView) rootView.findViewById(R.id.fieldGroupName);
        labelServerName = (TextView) rootView.findViewById(R.id.fieldServerName);
        labelLearnInterval = (TextView) rootView.findViewById(R.id.labelLearnInterval);
        labelTrackInterval = (TextView) rootView.findViewById(R.id.labelTrackInterval);
        labelLearnPeriod = (TextView) rootView.findViewById(R.id.labelLearnPeriod);
        learnInterval = (TextView) rootView.findViewById(R.id.fieldLearnInterval);
        learnPeriod = (TextView) rootView.findViewById(R.id.fieldLearnPeriod);
        trackInterval = (TextView) rootView.findViewById(R.id.fieldTrackInterval);

        // Rendering the setting page
        drawUi();

        // UserName click listener
        labelUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit User name");
                final EditText editText = new EditText(getActivity());
                editText.setText(labelUserName.getText());
                builder.setView(editText);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strUserName = editText.getText().toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.USER_NAME, strUserName);
                        labelUserName.setText(strUserName);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // GroupName click listener
        labelGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit Group name");
                final EditText editText = new EditText(getActivity());
                editText.setText(labelGroupName.getText());
                builder.setView(editText);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strGrpName = editText.getText().toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.GROUP_NAME, strGrpName);
                        labelGroupName.setText(strGrpName);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // ServerName click listener
        labelServerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit Server address");
                final EditText editText = new EditText(getActivity());
                editText.setText(labelServerName.getText());
                builder.setView(editText);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strServerName = editText.getText().toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.SERVER_NAME, strServerName);
                        labelServerName.setText(strServerName);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // Learning Interval click listener
        labelLearnInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit Learning Period");

                final NumberPicker numberPicker = new NumberPicker(getActivity());
                builder.setView(numberPicker);
                String[] nums = new String[11];
                for (int i = 0; i < nums.length; i++)
                    nums[i] = Integer.toString(i);

                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(nums.length - 1);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setDisplayedValues(nums);
                numberPicker.setValue(5);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int learnIntervalVal = numberPicker.getValue();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Constants.LEARN_INTERVAL, learnIntervalVal);
                        learnInterval.setText(String.valueOf(learnIntervalVal));
                        editor.apply();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // Learn Period click listener
        labelLearnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit Learning Peri");

                final NumberPicker numberPicker = new NumberPicker(getActivity());
                builder.setView(numberPicker);
                String[] nums = new String[11];
                for (int i = 0; i < nums.length; i++)
                    nums[i] = Integer.toString(i);

                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(nums.length - 1);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setDisplayedValues(nums);
                numberPicker.setValue(5);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int learnPeriodlVal = numberPicker.getValue();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Constants.LEARN_PERIOD, learnPeriodlVal);
                        learnPeriod.setText(String.valueOf(learnPeriodlVal));
                        editor.apply();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // Track Interval click listener
        labelTrackInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity()).setTitle("Edit Tracking Period");

                final NumberPicker numberPicker = new NumberPicker(getActivity());
                builder.setView(numberPicker);
                String[] nums = new String[16];
                for (int i = 0; i < nums.length; i++)
                    nums[i] = Integer.toString(i);

                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(nums.length - 1);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setDisplayedValues(nums);
                numberPicker.setValue(5);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int trackIntervalVal = numberPicker.getValue();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Constants.TRACK_INTERVAL, trackIntervalVal);
                        trackInterval.setText(String.valueOf(trackIntervalVal));
                        editor.apply();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }

    private void drawUi() {
        if (userSettings.getUsername() != null && !userSettings.getUsername().isEmpty()) {
            labelUserName.setText(userSettings.getUsername());

        } else {
            labelUserName.setText(Constants.DEFAULT_USERNAME);
        }

        if (userSettings.getGroup() != null && !userSettings.getGroup().isEmpty()) {
            labelGroupName.setText(userSettings.getGroup());
        } else {
            labelGroupName.setText(Constants.DEFAULT_GROUP);
        }

        if (userSettings.getServerUrl() != null && !userSettings.getServerUrl().isEmpty()) {
            labelServerName.setText(userSettings.getServerUrl());
        } else {
            labelServerName.setText(Constants.DEFAULT_SERVER);
        }

        trackInterval.setText(String.valueOf(userSettings.getTrackScanPeriodInSeconds()));

        learnInterval.setText(String.valueOf(userSettings.getLearnScanPeriodInSeconds()));

        learnPeriod.setText(String.valueOf(userSettings.getTotalLearnTimeInMinutes()));
    }

}
