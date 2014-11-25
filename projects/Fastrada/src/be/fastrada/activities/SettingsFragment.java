package be.fastrada.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import be.fastrada.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.xml_settings);
    }
}