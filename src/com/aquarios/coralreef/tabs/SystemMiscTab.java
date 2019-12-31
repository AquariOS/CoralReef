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
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settingslib.widget.LayoutPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

public class SystemMiscTab extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String ANIMATIONS_CATEGORY = "animations_category";
    private static final String GENERAL_NOTIFICATIONS = "general_notifications";
    private static final String MISCELLANEOUS_CATEGORY = "miscellaneous_category";
    private static final String CHANGELOG_CATEGORY = "changelog";
    private static final String LED_SETTINGS_CATEGORY = "led_settings";

    private LayoutPreference mAnimations;
    private LayoutPreference mGeneral;
    private LayoutPreference mMiscellaneous;
    private LayoutPreference mChangelog;
    private LayoutPreference mLedSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_misc_tab);

        mAnimations = (LayoutPreference) findPreference(ANIMATIONS_CATEGORY);
        if (!getResources().getBoolean(R.bool.animations_category_isVisible)) {
        mAnimations.setTitle(R.string.animations_category_title);
        }

        mGeneral = (LayoutPreference) findPreference(GENERAL_NOTIFICATIONS);
        if (!getResources().getBoolean(R.bool.general_notifications_category_isVisible)) {
        mGeneral.setTitle(R.string.general_notifications_title);
        }

        mMiscellaneous = (LayoutPreference) findPreference(MISCELLANEOUS_CATEGORY);
        if (!getResources().getBoolean(R.bool.miscellaneous_category_isVisible)) {
        mMiscellaneous.setTitle(R.string.miscellaneous_title);
        }

        mChangelog = (LayoutPreference) findPreference(CHANGELOG_CATEGORY);
        if (!getResources().getBoolean(R.bool.changelog_category_isVisible)) {
        mChangelog.setTitle(R.string.changelog_title);
        }

        mLedSettings = (LayoutPreference) findPreference(LED_SETTINGS_CATEGORY);
        if (!getResources().getBoolean(R.bool.led_settings_category_isVisible)) {
        mLedSettings.setTitle(R.string.led_settings_title);
        }

/* COMMENT OUT UNTIL SUPPORT IS ADDED
        // Only show if device supports either notification or battery LED
        Preference mLedSettings = findPreference(LED_SETTINGS_CATEGORY);
        if ((!getResources().getBoolean(com.android.internal.R.bool.config_intrusiveNotificationLed)) || 
            (!getResources().getBoolean(com.android.internal.R.bool.config_intrusiveBatteryLed))) {
            //Make sure Notification LED category is also visible with the following check
            getPreferenceScreen().removePreference(mLedSettings);
        }*/
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
