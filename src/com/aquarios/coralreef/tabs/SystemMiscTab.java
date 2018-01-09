/*
 * Copyright (C) 2017 The Dirty Unicorns Project
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

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.applications.LayoutPreference;
import com.android.settings.development.DevelopmentSettings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

public class SystemMiscTab extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String RECENTS_CATEGORY = "recents_category";
    private static final String GESTURE_OPTIONS_CATEGORY = "gesture_options";
    private static final String MISCELLANEOUS_CATEGORY = "miscellaneous_category";
    private static final String CHANGELOG_CATEGORY = "changelog";
    private static final String DISPLAY_OPTIONS_CATEGORY = "display_options";

    private LayoutPreference mRecents;
    private LayoutPreference mGestures;
    private LayoutPreference mMiscellaneous;
    private LayoutPreference mChangelog;
    private LayoutPreference mDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_misc_tab);

        mRecents = (LayoutPreference) findPreference(RECENTS_CATEGORY);
        mRecents.setTitle(R.string.recents_title);

        mGestures = (LayoutPreference) findPreference(GESTURE_OPTIONS_CATEGORY);
        mGestures.setTitle(R.string.gesture_options_title);

        mMiscellaneous = (LayoutPreference) findPreference(MISCELLANEOUS_CATEGORY);
        mMiscellaneous.setTitle(R.string.miscellaneous_title);

        mChangelog = (LayoutPreference) findPreference(CHANGELOG_CATEGORY);
        mChangelog.setTitle(R.string.changelog_title);

        mDisplay = (LayoutPreference) findPreference(GESTURE_OPTIONS_CATEGORY);
        mDisplay.setTitle(R.string.gesture_options_title);

         ContentResolver resolver = getActivity().getContentResolver();
         final PreferenceScreen prefScreen = getPreferenceScreen();
         final String KEY_DEVICE_PART = "device_part";
         final String KEY_DEVICE_PART_PACKAGE_NAME = "org.omnirom.device";

 // DeviceParts
        if (!DevelopmentSettings.isPackageInstalled(getActivity(), KEY_DEVICE_PART_PACKAGE_NAME)) {
            getPreferenceScreen().removePreference(findPreference(KEY_DEVICE_PART));
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
