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

public class StatusBarTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String BATTERY_CATEGORY = "battery_options_category";
    private static final String CLOCK_OPTIONS_CATEGORY = "clock_options_category";
    private static final String TRAFFIC_CATEGORY = "traffic_category";
    private static final String STATUS_BAR_ITEMS_CATEGORY = "status_bar_items_category";

    private LayoutPreference mBattery;
    private LayoutPreference mClockOptions;
    private LayoutPreference mTraffic;
    private LayoutPreference mStatusBarItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.status_bar_tab);

        mBattery = (LayoutPreference) findPreference(BATTERY_CATEGORY);
        if (!getResources().getBoolean(R.bool.battery_category_isVisible)) {
        mBattery.setTitle(R.string.battery_options_title);
        } 

        mClockOptions = (LayoutPreference) findPreference(CLOCK_OPTIONS_CATEGORY);
        if (!getResources().getBoolean(R.bool.clock_category_isVisible)) {
        mClockOptions.setTitle(R.string.clock_options_title);
        } 

        mTraffic = (LayoutPreference) findPreference(TRAFFIC_CATEGORY);
        if (!getResources().getBoolean(R.bool.traffic_category_isVisible)) {
        mTraffic.setTitle(R.string.traffic_title);
        } 

        mStatusBarItems = (LayoutPreference) findPreference(STATUS_BAR_ITEMS_CATEGORY);
        if (!getResources().getBoolean(R.bool.statusbar_icon_blacklist_category_isVisible)) {
        mStatusBarItems.setTitle(R.string.status_bar_items_title);
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
