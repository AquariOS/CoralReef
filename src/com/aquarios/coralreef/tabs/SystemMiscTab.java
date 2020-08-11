/*
 * Copyright (C) 2017 The Dirty Unicorns Project
 * Copyright (C) 2019 AquariOS
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

public class SystemMiscTab extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String HEADER_IMAGE_KEY = "top_header_image";
    private static final String ANIMATIONS_CATEGORY = "animations_category";
    private static final String GENERAL_NOTIFICATIONS = "general_notifications";
    private static final String MISCELLANEOUS_CATEGORY = "miscellaneous_category";
    private static final String CHANGELOG_CATEGORY = "changelog";
    private static final String LED_SETTINGS_CATEGORY = "led_settings";

    private LayoutPreference mHeaderImage;
    private CardPreference mAnimations;
    private CardPreference mGeneral;
    private CardPreference mMiscellaneous;
    private CardPreference mChangelog;
    private CardPreference mLedSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_system_misc);

        /**
         * Header image in tab
         */
        mHeaderImage = (LayoutPreference) findPreference(HEADER_IMAGE_KEY);
        mHeaderImage.setEnabled(false);

        /**
         * Animations
         */
        CardPreference mAnimations = findPreference("animations_category");
        if (!getResources().getBoolean(R.bool.animations_category_isVisible)) {
            getPreferenceScreen().removePreference(mAnimations);
        } else {
            mAnimations = (CardPreference) findPreference(ANIMATIONS_CATEGORY);
        }

        /**
         * General notifications
         */
        CardPreference mGeneral = findPreference("general_notifications");
        if (!getResources().getBoolean(R.bool.general_notifications_category_isVisible)) {
            getPreferenceScreen().removePreference(mGeneral);
        } else {
            mGeneral = (CardPreference) findPreference(GENERAL_NOTIFICATIONS);
        }

        /**
         * Miscellaneous settings
         */
        CardPreference mMiscellaneous = findPreference("miscellaneous_category");
        if (!getResources().getBoolean(R.bool.miscellaneous_category_isVisible)) {
            getPreferenceScreen().removePreference(mMiscellaneous);
        } else {
            mMiscellaneous = (CardPreference) findPreference(MISCELLANEOUS_CATEGORY);
        }

        /**
         * Changelog 
         */
        CardPreference mChangelog = findPreference("changelog");
        if (!getResources().getBoolean(R.bool.changelog_category_isVisible)) {
            getPreferenceScreen().removePreference(mChangelog);
        } else {
            mChangelog = (CardPreference) findPreference(CHANGELOG_CATEGORY);
        }

        /**
         * Only show LED category if device supports LED options for either
         * battery OR notifications. Also needs boolean set "true" in device
         */
        CardPreference mLedSettings = findPreference("led_settings");
        if (!getResources().getBoolean(R.bool.led_settings_category_isVisible)) {
//            (!getResources().getBoolean(com.android.internal.R.bool.config_intrusiveNotificationLed)) ||
//            (!getResources().getBoolean(com.android.internal.R.bool.config_intrusiveBatteryLed))) {
            getPreferenceScreen().removePreference(mLedSettings);
        } else {
            mLedSettings = (CardPreference) findPreference(LED_SETTINGS_CATEGORY);
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
