/*
 * Copyright (C) 2020 AquariOS
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
import android.preference.Preference.OnPreferenceChangeListener;

import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.CardPreference;
import com.android.settingslib.widget.LayoutPreference;

public class LockScreenTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String HEADER_IMAGE_KEY = "top_header_image";
    private static final String LOCKSCREEN_DATE_AND_TIME_CATEGORY = "lockscreen_date_and_time_category";
    private static final String LOCKSCREEN_GENERAL_CATEGORY = "lockscreen_general_category";
    private static final String LOCKSCREEN_TUNER_CATEGORY = "lockscreen_tuner_category";
    private static final String LOCKSCREEN_WEATHER_CATEGORY = "lockscreen_weather";

    private LayoutPreference mHeaderImage;
    private CardPreference mLockscreenDateTime;
    private CardPreference mLockscreenGeneral;
    private CardPreference mLockscreenTuner;
    private CardPreference mLockscreenWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_lock_screen);

        /**
         * Header image in tab
         */
        mHeaderImage = (LayoutPreference) findPreference(HEADER_IMAGE_KEY);
        mHeaderImage.setEnabled(false);

        /**
         * Lockscreen date & time
         */
        CardPreference mLockscreenDateTime = findPreference("lockscreen_date_and_time_category");
        if (!getResources().getBoolean(R.bool.lockclocks_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenDateTime);
        } else {
            mLockscreenDateTime = (CardPreference) findPreference(LOCKSCREEN_DATE_AND_TIME_CATEGORY);
        }

        /**
         * General lockscreen options
         */
        CardPreference mLockscreenGeneral = findPreference("lockscreen_general_category");
        if (!getResources().getBoolean(R.bool.lockscreen_general_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenGeneral);
        } else {
            mLockscreenGeneral = (CardPreference) findPreference(LOCKSCREEN_GENERAL_CATEGORY);
        }

        /**
         * Lockscreen tuner
         */
        CardPreference mLockscreenTuner = findPreference("lockscreen_tuner_category");
        if (!getResources().getBoolean(R.bool.lockscreen_tuner_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenTuner);
        } else {
            mLockscreenTuner = (CardPreference) findPreference(LOCKSCREEN_TUNER_CATEGORY);
        }

        /**
         * Lockscreen weather
         */
        CardPreference mLockscreenWeather = findPreference("lockscreen_weather");
        if (!getResources().getBoolean(R.bool.lockscreen_weather_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenWeather);
        } else {
            mLockscreenWeather = (CardPreference) findPreference(LOCKSCREEN_WEATHER_CATEGORY);
        }
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
