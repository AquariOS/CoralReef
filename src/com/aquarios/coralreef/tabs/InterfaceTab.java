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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.Preference.OnPreferenceChangeListener;

import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.CardPreference;
import com.android.settingslib.widget.LayoutPreference;

public class InterfaceTab extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String HEADER_IMAGE_KEY = "top_header_image";
    private static final String QUICK_SETTINGS_CATEGORY = "quick_settings_category";
    private static final String HEADSUP_CATEGORY = "headsup_category";
    private static final String RECENTS_CATEGORY = "recents_category";
    private static final String THEMER_CATEGORY = "themer_category";

    private LayoutPreference mHeaderImage;
    private CardPreference mQuickSettings;
    private CardPreference mHeadsup;
    private CardPreference mRecents;
    private CardPreference mThemer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_interface);

        /**
         * Header image in tab
         */
        mHeaderImage = (LayoutPreference) findPreference(HEADER_IMAGE_KEY);
        mHeaderImage.setEnabled(false);

        /**
         * Quick settings
         */
        CardPreference mQuickSettings = findPreference("quick_settings_category");
        if (!getResources().getBoolean(R.bool.quick_settings_category_isVisible)) {
            getPreferenceScreen().removePreference(mQuickSettings);
        } else {
            mQuickSettings = (CardPreference) findPreference(QUICK_SETTINGS_CATEGORY);
        }

        /**
         * Heads-up controls
         */
        CardPreference mHeadsup = findPreference("headsup_category");
        if (!getResources().getBoolean(R.bool.headsup_category_isVisible)) {
            getPreferenceScreen().removePreference(mHeadsup);
        } else {
            mHeadsup = (CardPreference) findPreference(HEADSUP_CATEGORY);
        }

        /**
         * Recents options
         */
        CardPreference mRecents = findPreference("recents_category");
        if (!getResources().getBoolean(R.bool.recents_category_isVisible)) {
            getPreferenceScreen().removePreference(mRecents);
        } else {
            mRecents = (CardPreference) findPreference(RECENTS_CATEGORY);
        }

        /**
         * Theming options
         */
        CardPreference mThemer = findPreference("themer_category");
        // The following 2 checks need to pass in order for Themer to show
        if ((!hasCustomThemesAvailable()) &&
            (!getResources().getBoolean(R.bool.themer_category_isVisible))) {
            getPreferenceScreen().removePreference(mThemer);
        } else {
            mThemer = (CardPreference) findPreference(THEMER_CATEGORY);
            mThemer.setEnabled(hasCustomThemesAvailable());
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean hasCustomThemesAvailable() {
        PackageManager pm = getPackageManager();
        Intent browse = new Intent();
        browse.setClassName("com.android.wallpaper",
                "com.android.customization.picker.theme.CustomThemeActivity");
        return pm.resolveActivity(browse, 0) != null;
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
