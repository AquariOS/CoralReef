/*
 * Copyright (C) 2018 AquariOS
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

package com.aquarios.coralreef.fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.UserHandle;
import android.os.Bundle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.SearchIndexableResource;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settingslib.graph.BatteryMeterDrawableBase;

import java.util.ArrayList;
import java.util.List;

public class BatteryOptions extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";

    private static final int STATUS_BAR_BATTERY_STYLE_TEXT = 3;
    private static final int STATUS_BAR_BATTERY_STYLE_HIDDEN = 4;

    private ListPreference mStatusBarBatteryShowPercent;
    private ListPreference mStatusBarBattery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.battery_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        mStatusBarBatteryShowPercent =
                (ListPreference) findPreference(SHOW_BATTERY_PERCENT);
        int batteryShowPercent = Settings.System.getIntForUser(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 2, UserHandle.USER_CURRENT);
        mStatusBarBatteryShowPercent.setValue(Integer.toString(batteryShowPercent));
        mStatusBarBatteryShowPercent.setOnPreferenceChangeListener(this);

        mStatusBarBattery = (ListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        int batteryStyle = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 4, UserHandle.USER_CURRENT);
        mStatusBarBattery.setValue(Integer.toString(batteryStyle));
        mStatusBarBattery.setOnPreferenceChangeListener(this);

        boolean hideForcePercentage =
                batteryStyle == 5 || batteryStyle == 6; /*text or hidden style*/
        mStatusBarBatteryShowPercent.setEnabled(!hideForcePercentage);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mStatusBarBatteryShowPercent) {
            int batteryShowPercent = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.SHOW_BATTERY_PERCENT, batteryShowPercent,
                    UserHandle.USER_CURRENT);
            int index = mStatusBarBatteryShowPercent.findIndexOfValue((String) newValue);
            mStatusBarBatteryShowPercent.setSummary(
                    mStatusBarBatteryShowPercent.getEntries()[index]);
            boolean hideForcePercentage = batteryShowPercent == 5
            || batteryShowPercent == 6; /*text or hidden style*/
            mStatusBarBatteryShowPercent.setEnabled(!hideForcePercentage);
            return true;
        } else if (preference == mStatusBarBattery) {
            int batteryStyle = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, batteryStyle,
                    UserHandle.USER_CURRENT);
            int index = mStatusBarBattery.findIndexOfValue((String) newValue);
            mStatusBarBattery.setSummary(
                    mStatusBarBattery.getEntries()[index]);
            boolean hideForcePercentage = batteryStyle == 5
            || batteryStyle == 6; /*text or hidden style*/
            mStatusBarBatteryShowPercent.setEnabled(!hideForcePercentage);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.battery_options;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
