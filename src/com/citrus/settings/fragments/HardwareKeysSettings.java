/*
 * Copyright (C) 2016 Dirty Unicorns Project
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

package com.citrus.settings.fragments;

import android.os.Bundle;
import android.os.UserHandle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;
import com.android.internal.utils.du.ActionConstants;
import com.android.internal.utils.du.DUActionUtils;
import com.citrus.settings.fragments.ActionFragment;
import com.citrus.settings.fragments.ButtonBacklightBrightness;

public class HardwareKeysSettings extends ActionFragment implements OnPreferenceChangeListener {

    private static final String HWKEY_DISABLE = "hardware_keys_disable";
    private static final String KEY_BUTTON_BACKLIGHT = "button_backlight";

    // category keys
    private static final String CATEGORY_HWKEY = "hardware_keys";
    private static final String CATEGORY_BACK = "back_key";
    private static final String CATEGORY_HOME = "home_key";
    private static final String CATEGORY_MENU = "menu_key";
    private static final String CATEGORY_ASSIST = "assist_key";
    private static final String CATEGORY_APPSWITCH = "app_switch_key";
    private static final String CATEGORY_VOLUME = "volume_keys";
    private static final String CATEGORY_POWER = "power_key";

    // Masks for checking presence of hardware keys.
    // Must match values in frameworks/base/core/res/res/values/config.xml
    public static final int KEY_MASK_HOME = 0x01;
    public static final int KEY_MASK_BACK = 0x02;
    public static final int KEY_MASK_MENU = 0x04;
    public static final int KEY_MASK_ASSIST = 0x08;
    public static final int KEY_MASK_APP_SWITCH = 0x10;
    public static final int KEY_MASK_CAMERA = 0x20;
    public static final int KEY_MASK_VOLUME = 0x40;

    private SwitchPreference mHwKeyDisable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.hardwarekeys_settings);

        final PreferenceScreen prefScreen = getPreferenceScreen();

        final boolean needsNavbar = DUActionUtils.hasNavbarByDefault(getActivity());
        final PreferenceCategory hwkeyCat =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_HWKEY);
        int keysDisabled = 0;
        if (!needsNavbar) {
            mHwKeyDisable = (SwitchPreference) findPreference(HWKEY_DISABLE);
            mHwKeyDisable.setOnPreferenceChangeListener(this);
            keysDisabled = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.HARDWARE_KEYS_DISABLE, 0,
                    UserHandle.USER_CURRENT);
           mHwKeyDisable.setChecked(keysDisabled != 0);
        } else {
            prefScreen.removePreference(hwkeyCat);
        }

        final ButtonBacklightBrightness backlight =
                (ButtonBacklightBrightness) findPreference(KEY_BUTTON_BACKLIGHT);
        if (!backlight.isButtonSupported()) {
        getPreferenceScreen().removePreference(backlight);
        }

        // bits for hardware keys present on device
        final int deviceKeys = getResources().getInteger(
               com.android.internal.R.integer.config_deviceHardwareKeys);

        // read bits for present hardware keys
        final boolean hasHomeKey = (deviceKeys & KEY_MASK_HOME) != 0;
        final boolean hasBackKey = (deviceKeys & KEY_MASK_BACK) != 0;
        final boolean hasMenuKey = (deviceKeys & KEY_MASK_MENU) != 0;
        final boolean hasAssistKey = (deviceKeys & KEY_MASK_ASSIST) != 0;
        final boolean hasAppSwitchKey = (deviceKeys & KEY_MASK_APP_SWITCH) != 0;

        // load categories and init/remove preferences based on device
        // configuration
        final PreferenceCategory backCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_BACK);
        final PreferenceCategory homeCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_HOME);
        final PreferenceCategory menuCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_MENU);
       final PreferenceCategory assistCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_ASSIST);
        final PreferenceCategory appSwitchCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_APPSWITCH);

        // back key
        if (!hasBackKey) {
            prefScreen.removePreference(backCategory);
        }

        // home key
        if (!hasHomeKey) {
            prefScreen.removePreference(homeCategory);
        }

        // App switch key (recents)
        if (!hasAppSwitchKey) {
            prefScreen.removePreference(appSwitchCategory);
        }

        // menu key
        if (!hasMenuKey) {
            prefScreen.removePreference(menuCategory);
        }

        // search/assist key
        if (!hasAssistKey) {
            prefScreen.removePreference(assistCategory);
        }

        // let super know we can load ActionPreferences
        onPreferenceScreenLoaded(ActionConstants.getDefaults(ActionConstants.HWKEYS));

        // load preferences first
        setActionPreferencesEnabled(keysDisabled == 0);
    }

    @Override
    protected boolean usesExtendedActionsList() {
        return true;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.CITRUS_SETTINGS;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHwKeyDisable) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.HARDWARE_KEYS_DISABLE,
                    value ? 1 : 0);
            setActionPreferencesEnabled(!value);
            return true;
        }
        return false;
    }
}
