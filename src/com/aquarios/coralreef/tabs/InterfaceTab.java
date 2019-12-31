/*
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

public class InterfaceTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String QUICK_SETTINGS_CATEGORY = "quick_settings_category";
    private static final String HEADSUP_CATEGORY = "headsup_category";
    private static final String RECENTS_CATEGORY = "recents_category";

    private LayoutPreference mQuickSettings;
    private LayoutPreference mHeadsup;
    private LayoutPreference mRecents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.interface_tab);

        mQuickSettings = (LayoutPreference) findPreference(QUICK_SETTINGS_CATEGORY);
        if (!getResources().getBoolean(R.bool.quick_settings_category_isVisible)) {
        mQuickSettings.setTitle(R.string.quick_settings_title);
        }

        mHeadsup = (LayoutPreference) findPreference(HEADSUP_CATEGORY);
        if (!getResources().getBoolean(R.bool.headsup_category_isVisible)) {
        mQuickSettings.setTitle(R.string.headsup_title);
        }

        mRecents = (LayoutPreference) findPreference(RECENTS_CATEGORY);
        if (!getResources().getBoolean(R.bool.recents_category_isVisible)) {
        mQuickSettings.setTitle(R.string.recents_title);
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
