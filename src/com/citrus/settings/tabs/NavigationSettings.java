package com.citrus.settings.tabs;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.MetricsLogger;

public class NavigationSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String VOLUME_ROCKER_WAKE = "volume_rocker_wake";

    private SwitchPreference mVolumeRockerWake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navigation_settings);
        final ContentResolver resolver = getActivity().getContentResolver();

        PreferenceScreen prefSet = getPreferenceScreen();


        mVolumeRockerWake = (SwitchPreference) findPreference(VOLUME_ROCKER_WAKE);
        mVolumeRockerWake.setOnPreferenceChangeListener(this);
        int volumeRockerWake = Settings.System.getInt(getContentResolver(),
                VOLUME_ROCKER_WAKE, 0);
        mVolumeRockerWake.setChecked(volumeRockerWake != 0);
     }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.CITRUS_SETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mVolumeRockerWake) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getContentResolver(), VOLUME_ROCKER_WAKE,
                    value ? 1 : 0);
            return true;
             }
        return false;
    }
}
