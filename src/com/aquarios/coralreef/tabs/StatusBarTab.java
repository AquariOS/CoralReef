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

public class StatusBarTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String HEADER_IMAGE_KEY = "top_header_image";
    private static final String BATTERY_CATEGORY = "battery_options_category";
    private static final String CLOCK_OPTIONS_CATEGORY = "clock_options_category";
    private static final String TRAFFIC_CATEGORY = "traffic_category";
    private static final String STATUS_BAR_ITEMS_CATEGORY = "status_bar_items_category";

    private LayoutPreference mHeaderImage;
    private CardPreference mBattery;
    private CardPreference mClockOptions;
    private CardPreference mTraffic;
    private CardPreference mStatusBarItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_status_bar);

        /**
         * Header image in tab
         */
        mHeaderImage = (LayoutPreference) findPreference(HEADER_IMAGE_KEY);
        mHeaderImage.setEnabled(false);

        /**
         * Battery options
         */
        CardPreference mBattery = findPreference("battery_options_category");
        if (!getResources().getBoolean(R.bool.battery_category_isVisible)) {
            getPreferenceScreen().removePreference(mBattery);
        } else {
            mBattery = (CardPreference) findPreference(BATTERY_CATEGORY);
        } 

        /**
         * Clock options
         */
        CardPreference mClockOptions = findPreference("clock_options_category");
        if (!getResources().getBoolean(R.bool.clock_category_isVisible)) {
            getPreferenceScreen().removePreference(mClockOptions);
        } else {
            mClockOptions = (CardPreference) findPreference(CLOCK_OPTIONS_CATEGORY);
        }

        /**
         * Network traffic
         */
        CardPreference mTraffic = findPreference("traffic_category");
        if (!getResources().getBoolean(R.bool.traffic_category_isVisible)) {
            getPreferenceScreen().removePreference(mTraffic);
        } else {
            mTraffic = (CardPreference) findPreference(TRAFFIC_CATEGORY);
        }

        /**
         * Statusbar item blacklisting
         */
        CardPreference mStatusBarItems = findPreference("status_bar_items_category");
        if (!getResources().getBoolean(R.bool.statusbar_icon_blacklist_category_isVisible)) {
            getPreferenceScreen().removePreference(mStatusBarItems);
        } else {
            mStatusBarItems = (CardPreference) findPreference(STATUS_BAR_ITEMS_CATEGORY);
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
