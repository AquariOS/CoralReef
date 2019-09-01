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

import java.util.ArrayList;
import java.util.List;

public class BatteryOptions extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String SHOW_BATTERY_PERCENT = "show_battery_percent";
    private static final String KEY_BATTERY_PERCENTAGE = "battery_percentage";
    private static final String BATTERY_PERCENTAGE_HIDDEN = "0";
    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";

    private static final int BATTERY_STYLE_Q = 0;
    private static final int BATTERY_STYLE_DOTTED_CIRCLE = 1;
    private static final int BATTERY_STYLE_CIRCLE = 2;
    private static final int BATTERY_STYLE_TEXT = 3;
    private static final int BATTERY_STYLE_HIDDEN = 4;

    private ListPreference mBatteryPercent;
    private ListPreference mBatteryStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        mBatteryPercent = (ListPreference) findPreference(KEY_BATTERY_PERCENTAGE);
        int percentstyle = Settings.System.getInt(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 0);
        mBatteryPercent.setValue(String.valueOf(percentstyle));
        mBatteryPercent.setSummary(mBatteryPercent.getEntry());
        mBatteryPercent.setOnPreferenceChangeListener(this);

        mBatteryStyle = (ListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        int batterystyle = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, BATTERY_STYLE_Q,
                UserHandle.USER_CURRENT);
        mBatteryStyle.setOnPreferenceChangeListener(this);

        updateBatteryOptions(batterystyle);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryPercent) {
            int value = Integer.parseInt((String) newValue);
            int index = mBatteryPercent.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SHOW_BATTERY_PERCENT, value);
            mBatteryPercent.setSummary(mBatteryPercent.getEntries()[index]);
            return true;
        } else if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            updateBatteryOptions(value);
            return true;
        }
        return false;
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
