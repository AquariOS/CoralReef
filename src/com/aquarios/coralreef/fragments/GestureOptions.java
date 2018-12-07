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
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

public class GestureOptions extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

     SwitchPreference mDoubleTapToSleepEnabled;
     SwitchPreference mDoubleTapToSleepAnywhere;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gesture_options);

        mDoubleTapToSleepEnabled = (SwitchPreference) findPreference("double_tap_sleep_gesture");
        mDoubleTapToSleepEnabled.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.DOUBLE_TAP_SLEEP_GESTURE, 0) == 1);
        mDoubleTapToSleepEnabled.setOnPreferenceChangeListener(this);

        mDoubleTapToSleepAnywhere = (SwitchPreference) findPreference("double_tap_sleep_anywhere");
        mDoubleTapToSleepAnywhere.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.DOUBLE_TAP_SLEEP_ANYWHERE, 0) == 1);
        mDoubleTapToSleepAnywhere.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		       if (preference.equals(mDoubleTapToSleepEnabled)) {
            boolean enabled = ((Boolean) newValue).booleanValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DOUBLE_TAP_SLEEP_GESTURE, enabled ? 1 : 0);
            return true;
        }
        if (preference.equals(mDoubleTapToSleepAnywhere)) {
            boolean enabled = ((Boolean) newValue).booleanValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DOUBLE_TAP_SLEEP_ANYWHERE, enabled ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }
}
