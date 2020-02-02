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

package com.aquarios.coralreef.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import com.aquarios.support.preferences.SystemSettingListPreference;
import com.aquarios.support.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

public class BatteryOptions extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String STATUS_BAR_BATTERY_TEXT_CHARGING = "status_bar_battery_text_charging";
    private static final String BATTERY_PERCENTAGE_HIDDEN = "0";
    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";

    private static final int BATTERY_STYLE_Q = 0;
    private static final int BATTERY_STYLE_DOTTED_CIRCLE = 1;
    private static final int BATTERY_STYLE_CIRCLE = 2;
    private static final int BATTERY_STYLE_TEXT = 3;
    private static final int BATTERY_STYLE_HIDDEN = 4;

    private ListPreference mBatteryPercent;
    private ListPreference mBatteryStyle;
    private SwitchPreference mBatteryCharging;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        mBatteryPercent = (ListPreference) findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
        mBatteryCharging = (SwitchPreference) findPreference(STATUS_BAR_BATTERY_TEXT_CHARGING);
        mBatteryStyle = (ListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        int batterystyle = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_BATTERY_STYLE, BATTERY_STYLE_Q);
        mBatteryStyle.setOnPreferenceChangeListener(this);

        updateBatteryOptions(batterystyle);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            updateBatteryOptions(value);
            return true;
        }
        return false;
    }

    private void updateBatteryOptions(int batterystyle) {
        boolean enabled = batterystyle != BATTERY_STYLE_TEXT && batterystyle != BATTERY_STYLE_HIDDEN;
        if (batterystyle == BATTERY_STYLE_HIDDEN) {
            mBatteryPercent.setValue(BATTERY_PERCENTAGE_HIDDEN);
            mBatteryPercent.setSummary(mBatteryPercent.getEntry());
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT, 0);
        }
        mBatteryCharging.setEnabled(enabled);
        mBatteryPercent.setEnabled(enabled);
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
