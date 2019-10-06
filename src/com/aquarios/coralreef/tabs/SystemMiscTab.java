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

    private static final String RECENTS_CATEGORY = "recents_category";
    private static final String AUDIO_DISPLAY_OPTIONS_CATEGORY = "audio_display_options";
    private static final String MISCELLANEOUS_CATEGORY = "miscellaneous_category";
    private static final String CHANGELOG_CATEGORY = "changelog";
    private static final String DEVICE_CATEGORY = "device_extras_category";

    private LayoutPreference mRecents;
    private LayoutPreference mMiscellaneous;
    private LayoutPreference mChangelog;
    private LayoutPreference mAudioDisplay;
    private LayoutPreference mDeviceExtras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_misc_tab);

        mRecents = (LayoutPreference) findPreference(RECENTS_CATEGORY);
        if (!getResources().getBoolean(R.bool.recents_category_isVisible)) {
        mRecents.setTitle(R.string.recents_title);
        } 

        mMiscellaneous = (LayoutPreference) findPreference(MISCELLANEOUS_CATEGORY);
        if (!getResources().getBoolean(R.bool.miscellaneous_category_isVisible)) {
        mMiscellaneous.setTitle(R.string.miscellaneous_title);
        } 

        mChangelog = (LayoutPreference) findPreference(CHANGELOG_CATEGORY);
        if (!getResources().getBoolean(R.bool.changelog_category_isVisible)) {
        mChangelog.setTitle(R.string.changelog_title);
        } 

        mAudioDisplay = (LayoutPreference) findPreference(AUDIO_DISPLAY_OPTIONS_CATEGORY);
        if (!getResources().getBoolean(R.bool.audio_display_category_isVisible)) {
        mAudioDisplay.setTitle(R.string.audio_display_options_title);
        } 

        mDeviceExtras = (LayoutPreference) findPreference(DEVICE_CATEGORY);
        if // COMMENTED OUT FOR BRING-UP
          //((getResources().getInteger(com.android.internal.R.integer.has_device_extras) == 0) &&
            (!getResources().getBoolean(R.bool.device_extras_category_isVisible)) {
            PreferenceScreen prefScreen = getPreferenceScreen();
            prefScreen.removePreference(mDeviceExtras);
        } else {
            mDeviceExtras.setTitle(R.string.hw_keys_title);
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
