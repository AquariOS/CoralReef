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

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.ListPreference;
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
import com.aquarios.support.colorpicker.ColorPickerPreference;
import com.aquarios.support.preferences.CustomSeekBarPreference;

import java.util.ArrayList;
import java.util.List;

public class BatteryOptions extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String STATUS_BAR_BATTERY_TEXT_CHARGING = "status_bar_battery_text_charging";
    private static final String BATTERY_PERCENTAGE_HIDDEN = "0";
    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
    private static final String PREF_BATT_BAR = "battery_bar_list";
    private static final String PREF_BATT_BAR_STYLE = "battery_bar_style";
    private static final String PREF_BATT_BAR_COLOR = "battery_bar_color";
    private static final String PREF_BATT_BAR_CHARGING_COLOR = "battery_bar_charging_color";
    private static final String PREF_BATT_BAR_LOW_COLOR_WARNING = "battery_bar_battery_low_color_warning";
    private static final String PREF_BATT_BAR_USE_GRADIENT_COLOR = "battery_bar_use_gradient_color";
    private static final String PREF_BATT_BAR_LOW_COLOR = "battery_bar_low_color";
    private static final String PREF_BATT_BAR_HIGH_COLOR = "battery_bar_high_color";
    private static final String PREF_BATT_BAR_WIDTH = "battery_bar_thickness";
    private static final String PREF_BATT_ANIMATE = "battery_bar_animate";

    private static final int BATTERY_STYLE_Q = 0;
    private static final int BATTERY_STYLE_DOTTED_CIRCLE = 1;
    private static final int BATTERY_STYLE_CIRCLE = 2;
    private static final int BATTERY_STYLE_TEXT = 3;
    private static final int BATTERY_STYLE_HIDDEN = 4;

    private ListPreference mBatteryPercent;
    private ListPreference mBatteryStyle;
    private SwitchPreference mBatteryCharging;
    private ListPreference mBatteryBar;
    private ListPreference mBatteryBarStyle;
    private CustomSeekBarPreference mBatteryBarThickness;
    private SwitchPreference mBatteryBarChargingAnimation;
    private SwitchPreference mBatteryBarUseGradient;
    private ColorPickerPreference mBatteryBarColor;
    private ColorPickerPreference mBatteryBarChargingColor;
    private ColorPickerPreference mBatteryBarBatteryLowColor;
    private ColorPickerPreference mBatteryBarBatteryLowColorWarn;
    private ColorPickerPreference mBatteryBarBatteryHighColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_options);

        final ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefSet = getPreferenceScreen();

        mBatteryPercent = (ListPreference) findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
        mBatteryCharging = (SwitchPreference) findPreference(STATUS_BAR_BATTERY_TEXT_CHARGING);
        mBatteryStyle = (ListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        int batterystyle = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_BATTERY_STYLE, BATTERY_STYLE_Q);
        mBatteryStyle.setOnPreferenceChangeListener(this);

        updateBatteryOptions(batterystyle);
                int intColor;
        String hexColor;

        mBatteryBar = (ListPreference) findPreference(PREF_BATT_BAR);
        mBatteryBar.setOnPreferenceChangeListener(this);
        mBatteryBar.setValue((Settings.System.getInt(resolver, Settings.System.BATTERY_BAR_LOCATION, 0)) + "");
        mBatteryBar.setSummary(mBatteryBar.getEntry());

        mBatteryBarStyle = (ListPreference) findPreference(PREF_BATT_BAR_STYLE);
        mBatteryBarStyle.setOnPreferenceChangeListener(this);
        mBatteryBarStyle.setValue((Settings.System.getInt(resolver,
                Settings.System.BATTERY_BAR_STYLE, 0)) + "");
        mBatteryBarStyle.setSummary(mBatteryBarStyle.getEntry());

        mBatteryBarColor = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_COLOR);
        intColor = Settings.System.getInt(resolver,
            Settings.System.BATTERY_BAR_COLOR, Color.WHITE);
        hexColor = String.format("#%08x", (0xffffff & intColor));
        mBatteryBarColor.setNewPreviewColor(intColor);
        mBatteryBarColor.setSummary(hexColor);
        mBatteryBarColor.setOnPreferenceChangeListener(this);

        mBatteryBarChargingColor = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_CHARGING_COLOR);
        intColor = Settings.System.getInt(resolver,
            Settings.System.BATTERY_BAR_CHARGING_COLOR, Color.WHITE);
        hexColor = String.format("#%08x", (0xffffff & intColor));
        mBatteryBarChargingColor.setNewPreviewColor(intColor);
        mBatteryBarChargingColor.setSummary(hexColor);
        mBatteryBarChargingColor.setOnPreferenceChangeListener(this);

        mBatteryBarBatteryLowColorWarn = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_LOW_COLOR_WARNING);
        intColor = Settings.System.getInt(resolver,
            Settings.System.BATTERY_BAR_BATTERY_LOW_COLOR_WARNING,Color.WHITE);
        hexColor = String.format("#%08x", (0xffffff & intColor));
        mBatteryBarBatteryLowColorWarn.setNewPreviewColor(intColor);
        mBatteryBarBatteryLowColorWarn.setSummary(hexColor);
        mBatteryBarBatteryLowColorWarn.setOnPreferenceChangeListener(this);

        mBatteryBarBatteryLowColor = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_LOW_COLOR);
        intColor = Settings.System.getInt(resolver,
            Settings.System.BATTERY_BAR_LOW_COLOR, Color.WHITE);
        hexColor = String.format("#%08x", (0xffffff & intColor));
        mBatteryBarBatteryLowColor.setNewPreviewColor(intColor);
        mBatteryBarBatteryLowColor.setSummary(hexColor);
        mBatteryBarBatteryLowColor.setOnPreferenceChangeListener(this);

        mBatteryBarBatteryHighColor = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_HIGH_COLOR);
        intColor = Settings.System.getInt(resolver,
            Settings.System.BATTERY_BAR_HIGH_COLOR, Color.WHITE);
        hexColor = String.format("#%08x", (0xff99CC00 & intColor));
        mBatteryBarBatteryHighColor.setNewPreviewColor(intColor);
        mBatteryBarBatteryHighColor.setSummary(hexColor);
        mBatteryBarBatteryHighColor.setOnPreferenceChangeListener(this);

        mBatteryBarUseGradient = (SwitchPreference) findPreference(PREF_BATT_BAR_USE_GRADIENT_COLOR);
        mBatteryBarUseGradient.setChecked(Settings.System.getInt(resolver,
                Settings.System.BATTERY_BAR_USE_GRADIENT_COLOR, 0) == 1);

        mBatteryBarChargingAnimation = (SwitchPreference) findPreference(PREF_BATT_ANIMATE);
        mBatteryBarChargingAnimation.setChecked(Settings.System.getInt(resolver,
                Settings.System.BATTERY_BAR_ANIMATE, 0) == 1);

        mBatteryBarThickness = (CustomSeekBarPreference) prefSet.findPreference(PREF_BATT_BAR_WIDTH);
        mBatteryBarThickness.setValue(Settings.System.getInt(resolver,
                Settings.System.BATTERY_BAR_THICKNESS, 1));
        mBatteryBarThickness.setOnPreferenceChangeListener(this);

        updateBatteryBarOptions();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		AlertDialog dialog;
		ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            updateBatteryOptions(value);
            return true;
        } else if (preference == mBatteryBarColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                .parseInt(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBarChargingColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                .parseInt(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_CHARGING_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBarBatteryLowColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                .parseInt(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_LOW_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBarBatteryLowColorWarn) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                .parseInt(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_BATTERY_LOW_COLOR_WARNING, intHex);
            return true;
        } else if (preference == mBatteryBarBatteryHighColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                .parseInt(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_HIGH_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBar) {
            int val = Integer.valueOf((String) newValue);
            int index = mBatteryBar.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_LOCATION, val);
            mBatteryBar.setSummary(mBatteryBar.getEntries()[index]);
            updateBatteryBarOptions();
            return true;
        } else if (preference == mBatteryBarStyle) {
            int val = Integer.parseInt((String) newValue);
            int index = mBatteryBarStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_STYLE, val);
            mBatteryBarStyle.setSummary(mBatteryBarStyle.getEntries()[index]);
            return true;
        } else if (preference == mBatteryBarThickness) {
            int val =  (Integer) newValue;
            Settings.System.putInt(resolver,
                Settings.System.BATTERY_BAR_THICKNESS, val);
            return true;
        }
        return false;
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        ContentResolver resolver = getActivity().getContentResolver();
        boolean value;

        if (preference == mBatteryBarChargingAnimation) {
            value = mBatteryBarChargingAnimation.isChecked();
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_BAR_ANIMATE, value ? 1 : 0);
            return true;
         } else if (preference == mBatteryBarUseGradient) {
            value = mBatteryBarUseGradient.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.BATTERY_BAR_USE_GRADIENT_COLOR, value ? 1 : 0);
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

    private void updateBatteryBarOptions() {
        if (Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.BATTERY_BAR_LOCATION, 0) == 0) {
            mBatteryBarStyle.setEnabled(false);
            mBatteryBarThickness.setEnabled(false);
            mBatteryBarChargingAnimation.setEnabled(false);
            mBatteryBarColor.setEnabled(false);
            mBatteryBarChargingColor.setEnabled(false);
            mBatteryBarUseGradient.setEnabled(false);
            mBatteryBarBatteryLowColor.setEnabled(false);
            mBatteryBarBatteryHighColor.setEnabled(false);
            mBatteryBarBatteryLowColorWarn.setEnabled(false);
        } else {
            mBatteryBarStyle.setEnabled(true);
            mBatteryBarThickness.setEnabled(true);
            mBatteryBarChargingAnimation.setEnabled(true);
            mBatteryBarColor.setEnabled(true);
            mBatteryBarChargingColor.setEnabled(true);
            mBatteryBarUseGradient.setEnabled(true);
            mBatteryBarBatteryLowColor.setEnabled(true);
            mBatteryBarBatteryHighColor.setEnabled(true);
            mBatteryBarBatteryLowColorWarn.setEnabled(true);
        }
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
