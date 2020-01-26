/*
 * Copyright (C) 2019 AquariOS
 * Copyright (C) 2018 The Dirty Unicorns Project
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
import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.widget.VideoPreference;

import com.aquarios.support.preferences.CustomSeekBarPreference;

import java.util.ArrayList;
import java.util.List;

public class ActiveEdge extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private int shortSqueezeActions;
    private int longSqueezeActions;

    private CustomSeekBarPreference mActiveEdgeSensitivity;
    private ListPreference mShortSqueezeActions;
    private ListPreference mLongSqueezeActions;
    private SwitchPreference mActiveEdgeWake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.active_edge);

        final ContentResolver resolver = getActivity().getContentResolver();

        shortSqueezeActions = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SHORT_SQUEEZE_SELECTION, 0,
                UserHandle.USER_CURRENT);
        mShortSqueezeActions = (ListPreference) findPreference("short_squeeze_selection");
        mShortSqueezeActions.setValue(Integer.toString(shortSqueezeActions));
        mShortSqueezeActions.setSummary(mShortSqueezeActions.getEntry());
        mShortSqueezeActions.setOnPreferenceChangeListener(this);

        longSqueezeActions = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.LONG_SQUEEZE_SELECTION, 0,
                UserHandle.USER_CURRENT);
        mLongSqueezeActions = (ListPreference) findPreference("long_squeeze_selection");
        mLongSqueezeActions.setValue(Integer.toString(longSqueezeActions));
        mLongSqueezeActions.setSummary(mLongSqueezeActions.getEntry());
        mLongSqueezeActions.setOnPreferenceChangeListener(this);

        int sensitivity = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.ASSIST_GESTURE_SENSITIVITY, 2, UserHandle.USER_CURRENT);
        mActiveEdgeSensitivity = (CustomSeekBarPreference) findPreference(
                "gesture_assist_sensitivity");
        mActiveEdgeSensitivity.setValue(sensitivity);
        mActiveEdgeSensitivity.setOnPreferenceChangeListener(this);

        mActiveEdgeWake = (SwitchPreference) findPreference("gesture_assist_wake");
        mActiveEdgeWake.setChecked((Settings.Secure.getIntForUser(resolver,
                Settings.Secure.ASSIST_GESTURE_WAKE_ENABLED, 1,
                UserHandle.USER_CURRENT) == 1));
        mActiveEdgeWake.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mShortSqueezeActions) {
            int shortSqueezeActions = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.SHORT_SQUEEZE_SELECTION, shortSqueezeActions,
                    UserHandle.USER_CURRENT);
            int index = mShortSqueezeActions.findIndexOfValue((String) newValue);
            mShortSqueezeActions.setSummary(
                    mShortSqueezeActions.getEntries()[index]);
            return true;
        } else if (preference == mLongSqueezeActions) {
            int longSqueezeActions = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.LONG_SQUEEZE_SELECTION, longSqueezeActions,
                    UserHandle.USER_CURRENT);
            int index = mLongSqueezeActions.findIndexOfValue((String) newValue);
            mLongSqueezeActions.setSummary(
                    mLongSqueezeActions.getEntries()[index]);
            return true;
        } else if (preference == mActiveEdgeSensitivity) {
            int val = (Integer) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.ASSIST_GESTURE_SENSITIVITY, val,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mActiveEdgeWake) {
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.ASSIST_GESTURE_WAKE_ENABLED,
                    (Boolean) newValue ? 1 : 0,
                    UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ensure preferences sensible to change get updated
        actionPreferenceReload();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Ensure preferences sensible to change gets updated
        actionPreferenceReload();
    }

    /* Helper for reloading both short and long gesture as they might change on
       package uninstallation */
    private void actionPreferenceReload() {
        int shortSqueezeActions = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.SHORT_SQUEEZE_SELECTION, 0,
                UserHandle.USER_CURRENT);

        int longSqueezeActions = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.LONG_SQUEEZE_SELECTION, 0,
                UserHandle.USER_CURRENT);

        // Reload the action preferences
        mShortSqueezeActions.setValue(Integer.toString(shortSqueezeActions));
        mShortSqueezeActions.setSummary(mShortSqueezeActions.getEntry());

        mLongSqueezeActions.setValue(Integer.toString(longSqueezeActions));
        mLongSqueezeActions.setSummary(mLongSqueezeActions.getEntry());
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
                    sir.xmlResId = R.xml.active_edge;

                    if (context.getPackageManager().hasSystemFeature(
                            "android.hardware.sensor.assist")) {
                        result.add(sir);
                    }
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };
}
