/*
 * Copyright (C) 2016 Citrus-CAF Project
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

package com.citrus.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.MultiSelectListPreferenceFix;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import android.text.TextUtils;
import android.util.Log;

public class QuickSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
    private static final String TAG = QuickSettings.class.getSimpleName();

    private static final String PREF_THEMES_TILE = "themes_tile_components";

    private ListPreference mNumColumns;
    private MultiSelectListPreferenceFix mThemesTile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.quick_settings);
        final ContentResolver resolver = getActivity().getContentResolver();

        PreferenceScreen prefSet = getPreferenceScreen();

        mNumColumns = (ListPreference) findPreference("sysui_qs_num_columns");
        int numColumns = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.QS_NUM_TILE_COLUMNS, getDefaultNumColums(),
                UserHandle.USER_CURRENT);
        mNumColumns.setValue(String.valueOf(numColumns));
        updateNumColumnsSummary(numColumns);
        mNumColumns.setOnPreferenceChangeListener(this);
        
        mThemesTile = (MultiSelectListPreferenceFix) findPreference(PREF_THEMES_TILE);
        mThemesTile.setValues(getThemesTileValues());
        mThemesTile.setOnPreferenceChangeListener(this);
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
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
         if (preference == mNumColumns) {
            int numColumns = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(), Settings.System.QS_NUM_TILE_COLUMNS,
                    numColumns, UserHandle.USER_CURRENT);
            updateNumColumnsSummary(numColumns);
            return true;
        } else if (preference == mThemesTile) {
            Set<String> vals = (Set<String>) objValue;
//            Log.e(TAG, "mThemesTileChanged " + vals.toString());
            setThemesTileValues(vals);
            return true;
        }
        return false;
    }

    private void setThemesTileValues(Set<String> vals) {
        if (vals.isEmpty()) {
            // if user unchecks everything, reset to default
            vals.addAll(Arrays.asList(getResources().getStringArray(
                    R.array.themes_tile_default_values)));
//            Log.e(TAG, "setThemesTileValues called but is empty list = " + vals.toString());
            mThemesTile.setValues(vals);
        }
//        Log.e(TAG, "setThemesTileValues called = " + vals.toString());
        StringBuilder b = new StringBuilder();
        for (String val : vals) {
            b.append(val);
            b.append("|");
        }
        String newVal = b.toString();
        if (newVal.endsWith("|")) {
            newVal = removeLastChar(newVal);
        }
//        Log.e(TAG, "Themes tile components writing to provider = " + newVal);
        Settings.Secure.putStringForUser(getContentResolver(),
                Settings.Secure.THEMES_TILE_COMPONENTS,
                newVal, UserHandle.USER_CURRENT);
    }

    private Set<String> getThemesTileValues() {
        Set<String> vals = new HashSet<>();
        String components = Settings.Secure.getStringForUser(getContentResolver(),
                Settings.Secure.THEMES_TILE_COMPONENTS,
                UserHandle.USER_CURRENT);
        if (components != null) {
//            Log.e(TAG, "Themes tile components from provider raw = " + components);
        }
        if (TextUtils.isEmpty(components)) {
            vals.addAll(Arrays.asList(getResources().getStringArray(
                    R.array.themes_tile_default_values)));
//            Log.e(TAG, "Themes tile components from provider is empty. get defaults = " + vals.toString());
        } else {
            vals.addAll(Arrays.asList(components.split("\\|")));
//            Log.e(TAG, "Themes tile components from provider = " + vals.toString());
        }
        return vals;
    }

    static String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private void updateNumColumnsSummary(int numColumns) {
        String prefix = (String) mNumColumns.getEntries()[mNumColumns.findIndexOfValue(String
                .valueOf(numColumns))];
        mNumColumns.setSummary(getResources().getString(R.string.qs_num_columns_showing, prefix));
    }

    private int getDefaultNumColums() {
        try {
            Resources res = getPackageManager()
                    .getResourcesForApplication("com.android.systemui");
            int val = res.getInteger(res.getIdentifier("quick_settings_num_columns", "integer",
                    "com.android.systemui")); // better not be larger than 5, that's as high as the
                                              // list goes atm
            return Math.max(1, val);
        } catch (Exception e) {
            return 3;
        }
    }
}