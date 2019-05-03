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

import android.os.Bundle;
import android.os.UserHandle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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

import java.util.ArrayList;
import java.util.List;

public class AudioPanel extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.audio_panel);

        ListPreference locationPref = (ListPreference) findPreference("audio_panel_animation_side");
        locationPref.setValue(getDefaultPanelLocationValue());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    private String getDefaultPanelLocationValue() {
        try {
            Context context = getActivity().createPackageContext("com.android.systemui", 0);
            Resources systemuiRes = context.getResources();
            int id = systemuiRes.getIdentifier("config_audioPanelOnLeftSide",
                    "bool", "com.android.systemui");
            boolean isDefaultLeft = systemuiRes.getBoolean(id);
            boolean isLeft = Settings.System.getIntForUser(getActivity().getContentResolver(),
                    Settings.System.AUDIO_PANEL_ANIMATION_SIDE,
                    isDefaultLeft ? 1 : 0,
                    UserHandle.USER_CURRENT) == 1;
            return isLeft ? "1" : "0";
        } catch (PackageManager.NameNotFoundException e) {
            return "0";
        }
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.audio_panel;
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
