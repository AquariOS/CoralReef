/*
 * Copyright (C) 2018 AquariOS
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

package com.aquarios.coralreef.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

import com.aquarios.support.colorpicker.ColorPickerPreference;
import com.aquarios.support.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

public class LedBattery extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private ColorPickerPreference mLowColor;
    private ColorPickerPreference mMediumColor;
    private ColorPickerPreference mFullColor;
    private ColorPickerPreference mReallyFullColor;
    private SystemSettingSwitchPreference mLowBatteryBlinking;

    private PreferenceCategory mColorCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.led_battery);

        PreferenceScreen prefSet = getPreferenceScreen();
        mColorCategory = (PreferenceCategory) findPreference("battery_light_cat");

        mLowBatteryBlinking = (SystemSettingSwitchPreference)prefSet.findPreference("battery_light_low_blinking");
        if (getResources().getBoolean(
                        com.android.internal.R.bool.config_ledCanPulse)) {
            mLowBatteryBlinking.setChecked(Settings.System.getIntForUser(getContentResolver(),
                            Settings.System.BATTERY_LIGHT_LOW_BLINKING, 0, UserHandle.USER_CURRENT) == 1);
            mLowBatteryBlinking.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mLowBatteryBlinking);
        }

        if (getResources().getBoolean(com.android.internal.R.bool.config_multiColorBatteryLed)) {
            int color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_COLOR, 0xFFFF0000,
                            UserHandle.USER_CURRENT);
            mLowColor = (ColorPickerPreference) findPreference("battery_light_low_color");
            mLowColor.setAlphaSliderEnabled(false);
            mLowColor.setNewPreviewColor(color);
            mLowColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_MEDIUM_COLOR, 0xFFFFFF00,
                            UserHandle.USER_CURRENT);
            mMediumColor = (ColorPickerPreference) findPreference("battery_light_medium_color");
            mMediumColor.setAlphaSliderEnabled(false);
            mMediumColor.setNewPreviewColor(color);
            mMediumColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_FULL_COLOR, 0xFF00FF00,
                            UserHandle.USER_CURRENT);
            mFullColor = (ColorPickerPreference) findPreference("battery_light_full_color");
            mFullColor.setAlphaSliderEnabled(false);
            mFullColor.setNewPreviewColor(color);
            mFullColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_REALLYFULL_COLOR, 0xFF00FF00,
                            UserHandle.USER_CURRENT);
            mReallyFullColor = (ColorPickerPreference) findPreference("battery_light_reallyfull_color");
            mReallyFullColor.setAlphaSliderEnabled(false);
            mReallyFullColor.setNewPreviewColor(color);
            mReallyFullColor.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mColorCategory);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mLowColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mMediumColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_MEDIUM_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mFullColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_FULL_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mReallyFullColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_REALLYFULL_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mLowBatteryBlinking) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_BLINKING, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mLowBatteryBlinking.setChecked(value);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.led_battery;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
