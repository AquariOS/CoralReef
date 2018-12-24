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

package com.aquarios.coralreef.tabs;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.applications.LayoutPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

public class LockScreenTab extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String LOCKSCREEN_DATE_AND_TIME_CATEGORY = "lockscreen_date_and_time_category";
    private static final String LOCKSCREEN_DISPLAY_CATEGORY = "lockscreen_display_category";
    private static final String LOCKSCREEN_WEATHER = "lockscreen_weather";

    private LayoutPreference mLockscreenDateTime;
    private LayoutPreference mLockscreenDisplay;
    private LayoutPreference mLockscreenWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lock_screen_tab);

        mLockscreenDateTime = (LayoutPreference) findPreference(LOCKSCREEN_DATE_AND_TIME_CATEGORY);
        mLockscreenDateTime.setTitle(R.string.lockscreen_date_and_time_title);

        mLockscreenDisplay = (LayoutPreference) findPreference(LOCKSCREEN_DISPLAY_CATEGORY);
        mLockscreenDisplay.setTitle(R.string.lockscreen_display_title);

        mLockscreenWeather = (LayoutPreference) findPreference(LOCKSCREEN_WEATHER);
        mLockscreenWeather.setTitle(R.string.lock_screen_weather_settings_title);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }
}
