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

public class ActionsTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String HEADER_IMAGE_KEY = "top_header_image";
    private static final String POWERBUTTON_CATEGORY = "powerbutton_category";
    private static final String NAVIGATION_CATEGORY = "navigationbar_settings";
    private static final String GESTURE_OPTIONS_CATEGORY = "gesture_options";
    private static final String VOLUME_ROCKER_CATEGORY = "volume_rocker_category";
    private static final String HWKEY_CATEGORY = "hw_keys_category";

    private LayoutPreference mHeaderImage;
    private CardPreference mPowerButton;
    private CardPreference mNavigation;
    private CardPreference mGestures;
    private CardPreference mVolumeRocker;
    private CardPreference mHwKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_actions);

        /**
         * Header image in tab
         */
        mHeaderImage = (LayoutPreference) findPreference(HEADER_IMAGE_KEY);
        mHeaderImage.setEnabled(false);

        /**
         * Power button options
         */
        CardPreference mPowerButton = findPreference("powerbutton_category");
        if (!getResources().getBoolean(R.bool.powerbutton_category_isVisible)) {
            getPreferenceScreen().removePreference(mPowerButton);
        } else {
            mPowerButton = (CardPreference) findPreference(POWERBUTTON_CATEGORY);
        }

        /**
         * System navigation
         */
        CardPreference mNavigation = findPreference("navigationbar_settings");
        if (!getResources().getBoolean(R.bool.navigationbar_category_isVisible)) {
            getPreferenceScreen().removePreference(mNavigation);
        } else {
            mNavigation = (CardPreference) findPreference(NAVIGATION_CATEGORY);
        }

        /**
         * Gestures
         */
        CardPreference mGestures = findPreference("gesture_options");
        if (!getResources().getBoolean(R.bool.gestures_category_isVisible)) {
            getPreferenceScreen().removePreference(mGestures);
        } else {
            mGestures = (CardPreference) findPreference(GESTURE_OPTIONS_CATEGORY);
        }

        /**
         * Volume rocker options
         */
        CardPreference mVolumeRocker = findPreference("volume_rocker_category");
        if (!getResources().getBoolean(R.bool.volumerocker_category_isVisible)) {
            getPreferenceScreen().removePreference(mVolumeRocker);
        } else {
            mVolumeRocker = (CardPreference) findPreference(VOLUME_ROCKER_CATEGORY);
        }

        /**
         * Hardware key controls
         */
        CardPreference mHwKeys = findPreference("hw_keys_category");
        if (!getResources().getBoolean(R.bool.hwkeys_category_isVisible)) {
            getPreferenceScreen().removePreference(mHwKeys);
        } else {
            mHwKeys = (CardPreference) findPreference(HWKEY_CATEGORY);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

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
