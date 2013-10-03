package com.wiredin.anagramsolver.screens;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.wiredin.anagramsolver.R;

/**
 * Created by Thomas on 9/19/13.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
}
