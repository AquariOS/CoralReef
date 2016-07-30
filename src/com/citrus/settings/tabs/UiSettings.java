 /*
 * Copyright (C) 2016 Citrus-AOSP Project
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

package com.citrus.settings.tabs;

import android.app.UiModeManager; 
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.DropDownPreference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;

public class UiSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "UiSettings";

    private static final String KEY_NIGHT_MODE = "night_mode"; 

    private DropDownPreference mNightModePreference; 
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ui_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();
       
        mNightModePreference = (DropDownPreference) findPreference(KEY_NIGHT_MODE);
        final UiModeManager uiManager = (UiModeManager) getSystemService(
                Context.UI_MODE_SERVICE);
        final int currentNightMode = uiManager.getNightMode();
        mNightModePreference.setSelectedValue(String.valueOf(currentNightMode));
        mNightModePreference.setCallback(new DropDownPreference.Callback() {
            
            @Override
            public boolean onItemSelected(int pos, Object newValue) {
                try {
                    final int value = Integer.parseInt((String) newValue);
                    final UiModeManager uiManager = (UiModeManager) getSystemService(
                            Context.UI_MODE_SERVICE);
                    uiManager.setNightMode(value);
                    return true;
                } catch (NumberFormatException e) {
                    Log.e(TAG, "could not persist night mode setting", e);
                    return false;
                }
            }
        });
     }
    
    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.CITRUS_SETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return true;
    }

}
