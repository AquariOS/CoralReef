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

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.aquarios.AquaUtils;

import java.util.ArrayList;
import java.util.List;

public class Recents extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String RECENTS_CLEAR_ALL_LOCATION = "recents_clear_all_location";
    private static final String RECENTS_COMPONENT_TYPE = "recents_component";
    private static final String CATEGORY_OREO_STYLE_OPTIONS = "category_oreo_style_options";

    private ListPreference mRecentsClearAllLocation;
    private SwitchPreference mRecentsClearAll;
    private ListPreference mRecentsComponentType;
    private PreferenceCategory mOreoStyleOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.recents);

        ContentResolver resolver = getActivity().getContentResolver();

        // clear all recents
        mRecentsClearAllLocation = (ListPreference) findPreference(RECENTS_CLEAR_ALL_LOCATION);
        int location = Settings.System.getIntForUser(resolver,
                Settings.System.RECENTS_CLEAR_ALL_LOCATION, 3, UserHandle.USER_CURRENT);
        mRecentsClearAllLocation.setValue(String.valueOf(location));
        mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntry());
        mRecentsClearAllLocation.setOnPreferenceChangeListener(this);

        // recents component type
        mRecentsComponentType = (ListPreference) findPreference(RECENTS_COMPONENT_TYPE);
        int type = Settings.System.getInt(resolver,
                Settings.System.RECENTS_COMPONENT, 0);
        mRecentsComponentType.setValue(String.valueOf(type));
        mRecentsComponentType.setSummary(mRecentsComponentType.getEntry());
        mRecentsComponentType.setOnPreferenceChangeListener(this);

        // Hide clear-all options if set to Pie/horizontal style
        mOreoStyleOptions = (PreferenceCategory) findPreference("category_oreo_style_options");

        final PreferenceCategory mOreoStyleOptions = (PreferenceCategory) getPreferenceScreen()
                .findPreference(CATEGORY_OREO_STYLE_OPTIONS);
        if (isUsingPieRecents()) {
            getPreferenceScreen().removePreference(mOreoStyleOptions);
        } else {
            getPreferenceScreen().addPreference(mOreoStyleOptions);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    private boolean isUsingPieRecents() {
        final ContentResolver resolver = getContext().getContentResolver();
        return Settings.System.getIntForUser(resolver,
               Settings.System.RECENTS_COMPONENT, 0, UserHandle.USER_CURRENT) == 0;
    }

    @Override
    public void onResume() {
        super.onResume();
            if (isUsingPieRecents()) {
                getPreferenceScreen().removePreference(mOreoStyleOptions);
            } else {
                getPreferenceScreen().addPreference(mOreoStyleOptions);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mRecentsClearAllLocation) {
            int location = Integer.valueOf((String) newValue);
            int index = mRecentsClearAllLocation.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.RECENTS_CLEAR_ALL_LOCATION, location, UserHandle.USER_CURRENT);
            mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntries()[index]);
            return true;
       } else if (preference == mRecentsComponentType) {
            int type = Integer.valueOf((String) newValue);
            int index = mRecentsComponentType.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.RECENTS_COMPONENT, type);
            mRecentsComponentType.setSummary(mRecentsComponentType.getEntries()[index]);
            if (type == 1) { // Disable swipe up gesture, if oreo type selected
               Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.SWIPE_UP_TO_SWITCH_APPS_ENABLED, 0);
            }
            AquaUtils.showSystemUiRestartDialog(getContext());
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
                    sir.xmlResId = R.xml.recents;
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
