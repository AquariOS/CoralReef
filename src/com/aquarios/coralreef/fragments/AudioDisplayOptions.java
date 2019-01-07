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
import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.text.TextUtils;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

import com.aquarios.coralreef.preferences.AppMultiSelectListPreference;
import com.aquarios.coralreef.preferences.ScrollAppsViewPreference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class AudioDisplayOptions extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String KEY_ASPECT_RATIO_APPS_ENABLED = "aspect_ratio_apps_enabled";
    private static final String KEY_ASPECT_RATIO_APPS_LIST = "aspect_ratio_apps_list";
    private static final String KEY_ASPECT_RATIO_CATEGORY = "aspect_ratio_category";
    private static final String KEY_ASPECT_RATIO_APPS_LIST_SCROLLER = "aspect_ratio_apps_list_scroller";
    private static final String SCREEN_OFF_ANIMATION = "screen_off_animation"; 

    private AppMultiSelectListPreference mAspectRatioAppsSelect;
    private ScrollAppsViewPreference mAspectRatioApps;
    private ListPreference mScreenOffAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.audio_display_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        final PreferenceCategory aspectRatioCategory =
                (PreferenceCategory) getPreferenceScreen().findPreference(KEY_ASPECT_RATIO_CATEGORY);

        final boolean supportMaxAspectRatio = getResources().getBoolean(com.android.internal.R.bool.config_haveHigherAspectRatioScreen);

        if (!supportMaxAspectRatio) {
            getPreferenceScreen().removePreference(aspectRatioCategory);
        } else {
        mAspectRatioAppsSelect = (AppMultiSelectListPreference) findPreference(KEY_ASPECT_RATIO_APPS_LIST);
        mAspectRatioApps = (ScrollAppsViewPreference) findPreference(KEY_ASPECT_RATIO_APPS_LIST_SCROLLER);

        final String valuesString = Settings.System.getString(getContentResolver(), Settings.System.ASPECT_RATIO_APPS_LIST);
        List<String> valuesList = new ArrayList<String>();

        if (!TextUtils.isEmpty(valuesString)) {
             valuesList.addAll(Arrays.asList(valuesString.split(":")));
             mAspectRatioApps.setVisible(true);
             mAspectRatioApps.setValues(valuesList);
        } else {
             mAspectRatioApps.setVisible(false);
        }
        mAspectRatioAppsSelect.setValues(valuesList);
        mAspectRatioAppsSelect.setOnPreferenceChangeListener(this);
        }

       // Screen Off Animations 
        mScreenOffAnimation = (ListPreference) findPreference(SCREEN_OFF_ANIMATION); 
        int screenOffStyle = Settings.System.getInt(resolver, 
                 Settings.System.SCREEN_OFF_ANIMATION, 0); 
        mScreenOffAnimation.setValue(String.valueOf(screenOffStyle)); 
        mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntry()); 
        mScreenOffAnimation.setOnPreferenceChangeListener(this); 
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAspectRatioAppsSelect) {
            Collection<String> valueList = (Collection<String>) newValue;
            mAspectRatioApps.setVisible(false);
            if (valueList != null) {
                Settings.System.putString(getContentResolver(), Settings.System.ASPECT_RATIO_APPS_LIST,
                        TextUtils.join(":", valueList));
                mAspectRatioApps.setVisible(true);
                mAspectRatioApps.setValues(valueList);
            } else {
                Settings.System.putString(getContentResolver(), Settings.System.ASPECT_RATIO_APPS_LIST, "");
            }
            return true;
        }
      if (preference == mScreenOffAnimation) { 
            Settings.System.putInt(getContentResolver(), 
                    Settings.System.SCREEN_OFF_ANIMATION, Integer.valueOf((String) newValue)); 
            int valueIndex = mScreenOffAnimation.findIndexOfValue((String) newValue); 
            mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntries()[valueIndex]); 
            return true; 
        } 
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }
}
