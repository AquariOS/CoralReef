/*
 * Copyright (C) 2017 AquariOS
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

package com.aquarios.settings.tabs;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class SystemSettings extends SettingsPreferenceFragment
        implements OnPreferenceChangeListener {

    private static final String RECENTS_CLEAR_ALL_LOCATION = "recents_clear_all_location";

    private ListPreference mRecentsClearAllLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_settings_tab);
        ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefScreen = getPreferenceScreen();

        mRecentsClearAllLocation = (ListPreference) findPreference(RECENTS_CLEAR_ALL_LOCATION);
        int location = Settings.System.getInt(resolver,
                Settings.System.RECENTS_CLEAR_ALL_LOCATION, 3);
        mRecentsClearAllLocation.setValue(String.valueOf(location));
        mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntry());
        mRecentsClearAllLocation.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.AQUARIOS;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mRecentsClearAllLocation) {
            int location = Integer.valueOf((String) newValue);
            int index = mRecentsClearAllLocation.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.RECENTS_CLEAR_ALL_LOCATION, location);
            mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntries()[index]);
            return true;
        }
        return false;
    }
}
