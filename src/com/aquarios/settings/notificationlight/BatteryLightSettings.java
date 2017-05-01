/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aquarios.settings.notificationlight;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.aquarios.settings.preference.SystemSettingSwitchPreference;

public class BatteryLightSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "BatteryLightSettings";

    private static final String GENERAL_CAT = "general_section";

    private static final String LOW_COLOR_PREF = "low_color";
    private static final String MEDIUM_COLOR_PREF = "medium_color";
    private static final String FULL_COLOR_PREF = "full_color";
    private static final String REALLY_FULL_COLOR_PREF = "really_full_color";
    private static final String LIGHT_ENABLED_PREF = "battery_light_enabled";
    private static final String PULSE_ENABLED_PREF = "battery_light_pulse";
    private static final String BLEND_COLOR_PREF = "battery_light_blend_color";
    private static final String BLEND_COLOR_REVERSE_PREF = "battery_light_blend_color_reverse";

    private PreferenceGroup mColorPrefs;
    private ApplicationLightPreference mLowColorPref;
    private ApplicationLightPreference mMediumColorPref;
    private ApplicationLightPreference mFullColorPref;
    private ApplicationLightPreference mReallyFullColorPref;
    private SystemSettingSwitchPreference mLightEnabledPref;
    private SystemSettingSwitchPreference mPulseEnabledPref;
    private SystemSettingSwitchPreference mBlendColorPref;
    private SystemSettingSwitchPreference mBlendColorReversePref;

    private static final int MENU_RESET = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_light_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        PreferenceCategory generalCategory = (PreferenceCategory) findPreference(GENERAL_CAT);

        mLightEnabledPref = (SystemSettingSwitchPreference) prefSet.findPreference(LIGHT_ENABLED_PREF);
        mPulseEnabledPref = (SystemSettingSwitchPreference) prefSet.findPreference(PULSE_ENABLED_PREF);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_ledCanPulse)) {
            generalCategory.removePreference(prefSet.findPreference(PULSE_ENABLED_PREF));
        }

        // Does the Device support changing battery LED colors?
        if (getResources().getBoolean(com.android.internal.R.bool.config_multiColorBatteryLed)) {
            setHasOptionsMenu(true);

            // Low, Medium and full color preferences
            mLowColorPref = (ApplicationLightPreference) prefSet.findPreference(LOW_COLOR_PREF);
            mLowColorPref.setOnPreferenceChangeListener(this);

            mMediumColorPref = (ApplicationLightPreference) prefSet.findPreference(MEDIUM_COLOR_PREF);
            mMediumColorPref.setOnPreferenceChangeListener(this);

            mFullColorPref = (ApplicationLightPreference) prefSet.findPreference(FULL_COLOR_PREF);
            mFullColorPref.setOnPreferenceChangeListener(this);

            mReallyFullColorPref = (ApplicationLightPreference) prefSet.findPreference(REALLY_FULL_COLOR_PREF);
            mReallyFullColorPref.setOnPreferenceChangeListener(this);

            mBlendColorPref = (SystemSettingSwitchPreference) prefSet.findPreference(BLEND_COLOR_PREF);
            mBlendColorPref.setOnPreferenceChangeListener(this);

            mBlendColorReversePref =
                    (SystemSettingSwitchPreference) prefSet.findPreference(BLEND_COLOR_REVERSE_PREF);
            mBlendColorReversePref.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(prefSet.findPreference("colors_list"));
            resetColors();
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.AQUARIOS;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDefault();
    }

    private void refreshDefault() {
        ContentResolver resolver = getContentResolver();
        Resources res = getResources();

        if (mLowColorPref != null) {
            int lowColor = Settings.System.getInt(resolver, Settings.System.BATTERY_LIGHT_LOW_COLOR,
                    res.getInteger(com.android.internal.R.integer.config_notificationsBatteryLowARGB));
            mLowColorPref.setAllValues(lowColor, 0, 0, false);
        }

        if (mMediumColorPref != null) {
            int mediumColor = Settings.System.getInt(resolver, Settings.System.BATTERY_LIGHT_MEDIUM_COLOR,
                    res.getInteger(com.android.internal.R.integer.config_notificationsBatteryMediumARGB));
            mMediumColorPref.setAllValues(mediumColor, 0, 0, false);
        }

        if (mFullColorPref != null) {
            int fullColor = Settings.System.getInt(resolver, Settings.System.BATTERY_LIGHT_FULL_COLOR,
                    res.getInteger(com.android.internal.R.integer.config_notificationsBatteryFullARGB));
            mFullColorPref.setAllValues(fullColor, 0, 0, false);
        }

        if (mReallyFullColorPref != null) {
            int reallyFullColor = Settings.System.getInt(resolver, Settings.System.BATTERY_LIGHT_REALLY_FULL_COLOR,
                    res.getInteger(com.android.internal.R.integer.config_notificationsBatteryFullARGB));
            mReallyFullColorPref.setAllValues(reallyFullColor, 0, 0, false);
        }

        if (mBlendColorPref != null) {
            mBlendColorPref.setChecked(Settings.System.getInt(resolver,
                    Settings.System.BATTERY_LIGHT_BLEND_COLOR, 0) != 0);
        }

        if (mBlendColorReversePref != null) {
            mBlendColorReversePref.setChecked(Settings.System.getInt(resolver,
                    Settings.System.BATTERY_LIGHT_BLEND_COLOR_REVERSE, 0) != 0);
        }

        setBlendColorDependencies();
    }

    /**
     * Updates the default or application specific notification settings.
     *
     * @param key of the specific setting to update
     * @param color
     */
    protected void updateValues(String key, Integer color) {
        ContentResolver resolver = getContentResolver();

        if (key.equals(LOW_COLOR_PREF)) {
            Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_LOW_COLOR, color);
        } else if (key.equals(MEDIUM_COLOR_PREF)) {
            Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_MEDIUM_COLOR, color);
        } else if (key.equals(FULL_COLOR_PREF)) {
            Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_FULL_COLOR, color);
        } else if (key.equals(REALLY_FULL_COLOR_PREF)) {
            Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_REALLY_FULL_COLOR, color);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_reset)
                .setAlphabeticShortcut('r')
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefaults();
                return true;
        }
        return false;
    }

    protected void resetColors() {
        ContentResolver resolver = getContentResolver();
        Resources res = getResources();

        // Reset to the framework default colors
        Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_LOW_COLOR,
                res.getInteger(com.android.internal.R.integer.config_notificationsBatteryLowARGB));
        Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_MEDIUM_COLOR,
                res.getInteger(com.android.internal.R.integer.config_notificationsBatteryMediumARGB));
        Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_FULL_COLOR,
                res.getInteger(com.android.internal.R.integer.config_notificationsBatteryFullARGB));
        Settings.System.putInt(resolver, Settings.System.BATTERY_LIGHT_REALLY_FULL_COLOR,
                res.getInteger(com.android.internal.R.integer.config_notificationsBatteryFullARGB));
        refreshDefault();
    }

    protected void resetToDefaults() {
        final Resources res = getResources();
        final boolean batteryLightEnabled = res.getBoolean(R.bool.def_battery_light_enabled);
        final boolean batteryLightPulseEnabled = res.getBoolean(R.bool.def_battery_light_pulse);

        if (mLightEnabledPref != null) mLightEnabledPref.setChecked(batteryLightEnabled);
        if (mPulseEnabledPref != null) mPulseEnabledPref.setChecked(batteryLightPulseEnabled);

        resetColors();

        if (mBlendColorPref != null) mBlendColorPref.setChecked(false);
        if (mBlendColorReversePref != null) mBlendColorReversePref.setChecked(false);
        setBlendColorDependencies();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mBlendColorPref) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_BLEND_COLOR,
                    ((boolean) objValue) ? 1 : 0);
            setBlendColorDependencies((boolean) objValue);
            return true;
        } else if (preference == mBlendColorReversePref) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_BLEND_COLOR_REVERSE,
                    ((boolean) objValue) ? 1 : 0);
            return true;
        }
        ApplicationLightPreference lightPref = (ApplicationLightPreference) preference;
        updateValues(lightPref.getKey(), lightPref.getColor());

        return true;
    }

    private void setBlendColorDependencies() {
        setBlendColorDependencies(mBlendColorPref.isChecked());
    }

    private void setBlendColorDependencies(boolean blendColors) {
        boolean enabled = !blendColors;
        if (mMediumColorPref != null) mMediumColorPref.setEnabled(enabled);
        if (mFullColorPref != null) mFullColorPref.setEnabled(enabled);
    }
}
