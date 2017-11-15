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

package com.aquarios.coralreef.tabs;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import com.android.settings.Utils;
import com.android.internal.util.aquarios.AquaUtils;
import android.text.TextUtils;
import android.view.View;

import java.util.List;
import java.util.ArrayList;

public class QuickSettingsTab extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String PREF_SMART_PULLDOWN = "smart_pulldown";

    private static final String CATEGORY_WEATHER = "weather_category";
    private static final String WEATHER_ICON_PACK = "weather_icon_pack";
    private static final String DEFAULT_WEATHER_ICON_PACKAGE = "org.omnirom.omnijaws";
    private static final String WEATHER_SERVICE_PACKAGE = "org.omnirom.omnijaws";
    private static final String CHRONUS_ICON_PACK_INTENT = "com.dvtonder.chronus.ICON_PACK";

    private ListPreference mSmartPulldown;
    private Preference mWeatherPreference;
    private ListPreference mWeatherIconPack;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.quick_settings_tab);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        final PreferenceScreen prefSet = getPreferenceScreen();

        mSmartPulldown = (ListPreference) findPreference(PREF_SMART_PULLDOWN);
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldown = Settings.System.getInt(resolver,
                Settings.System.QS_SMART_PULLDOWN, 0);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);

        mWeatherPreference = (Preference) prefSet.findPreference(CATEGORY_WEATHER);
        if (mWeatherPreference != null && !isOmniJawsServiceInstalled()) {
            prefSet.removePreference(mWeatherPreference);
        } else {
            String settingHeaderPackage = Settings.System.getString(getContentResolver(),
                    Settings.System.OMNIJAWS_WEATHER_ICON_PACK);
            if (settingHeaderPackage == null) {
                settingHeaderPackage = DEFAULT_WEATHER_ICON_PACKAGE;
            }
            mWeatherIconPack = (ListPreference) findPreference(WEATHER_ICON_PACK);

            List<String> entries = new ArrayList<String>();
            List<String> values = new ArrayList<String>();
            getAvailableWeatherIconPacks(entries, values);
            mWeatherIconPack.setEntries(entries.toArray(new String[entries.size()]));
            mWeatherIconPack.setEntryValues(values.toArray(new String[values.size()]));

            int valueIndex = mWeatherIconPack.findIndexOfValue(settingHeaderPackage);
            if (valueIndex == -1) {
                // no longer found
                settingHeaderPackage = DEFAULT_WEATHER_ICON_PACKAGE;
                Settings.System.putString(getContentResolver(),
                        Settings.System.OMNIJAWS_WEATHER_ICON_PACK, settingHeaderPackage);
                valueIndex = mWeatherIconPack.findIndexOfValue(settingHeaderPackage);
            }
            mWeatherIconPack.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
            mWeatherIconPack.setSummary(mWeatherIconPack.getEntry());
            mWeatherIconPack.setOnPreferenceChangeListener(this);
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, Settings.System.QS_SMART_PULLDOWN, smartPulldown);
            updateSmartPulldownSummary(smartPulldown);
            return true;
        } else if (preference == mWeatherIconPack) {
            String value = (String) newValue;
            Settings.System.putString(getContentResolver(),
                    Settings.System.OMNIJAWS_WEATHER_ICON_PACK, value);
            int valueIndex = mWeatherIconPack.findIndexOfValue(value);
            mWeatherIconPack.setSummary(mWeatherIconPack.getEntries()[valueIndex]);
        }

        return false;
    }

    private void updateSmartPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // Smart pulldown deactivated
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
        } else if (value == 3) {
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_none_summary));
        } else {
            String type = res.getString(value == 1
                    ? R.string.smart_pulldown_dismissable
                    : R.string.smart_pulldown_ongoing);
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
        }
    }

    private boolean isOmniJawsServiceInstalled() {
        return AquaUtils.isAvailableApp(WEATHER_SERVICE_PACKAGE, getActivity());
    }

    private void getAvailableWeatherIconPacks(List<String> entries, List<String> values) {
        Intent i = new Intent();
        PackageManager packageManager = getPackageManager();
        i.setAction("org.omnirom.WeatherIconPack");
        for (ResolveInfo r : packageManager.queryIntentActivities(i, 0)) {
            String packageName = r.activityInfo.packageName;
            if (packageName.equals(DEFAULT_WEATHER_ICON_PACKAGE)) {
                values.add(0, r.activityInfo.name);
            } else {
                values.add(r.activityInfo.name);
            }
            String label = r.activityInfo.loadLabel(getPackageManager()).toString();
            if (label == null) {
                label = r.activityInfo.packageName;
            }
            if (packageName.equals(DEFAULT_WEATHER_ICON_PACKAGE)) {
                entries.add(0, label);
            } else {
                entries.add(label);
            }
        }
        i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(CHRONUS_ICON_PACK_INTENT);
        for (ResolveInfo r : packageManager.queryIntentActivities(i, 0)) {
            String packageName = r.activityInfo.packageName;
            values.add(packageName + ".weather");
            String label = r.activityInfo.loadLabel(getPackageManager()).toString();
            if (label == null) {
                label = r.activityInfo.packageName;
            }
            entries.add(label);
        }
    }

    private boolean isOmniJawsEnabled() {
        final Uri SETTINGS_URI
            = Uri.parse("content://org.omnirom.omnijaws.provider/settings");

        final String[] SETTINGS_PROJECTION = new String[] {
            "enabled"
        };

        final Cursor c = getContentResolver().query(SETTINGS_URI, SETTINGS_PROJECTION,
                null, null, null);
        if (c != null) {
            int count = c.getCount();
            if (count == 1) {
                c.moveToPosition(0);
                boolean enabled = c.getInt(0) == 1;
                return enabled;
            }
        }
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

}
