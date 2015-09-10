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

import com.android.internal.widget.LockPatternUtils;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.aquarios.AquaUtils;
import com.android.internal.logging.nano.MetricsProto;
import com.aquarios.support.preferences.CustomSeekBarPreference;

import java.util.ArrayList;
import java.util.List;

public class PowerMenu extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String TORCH_POWER_BUTTON_GESTURE = "torch_power_button_gesture";
    private static final String KEY_LOCKDOWN_IN_POWER_MENU = "lockdown_in_power_menu";
    private static final String PREF_ON_THE_GO_ALPHA = "on_the_go_alpha";
    private static final int MY_USER_ID = UserHandle.myUserId();

    private SwitchPreference mPowerMenuLockDown;
    private ListPreference mTorchPowerButton;
    private CustomSeekBarPreference mOnTheGoAlphaPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.powermenu);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());

        mPowerMenuLockDown = (SwitchPreference) findPreference(KEY_LOCKDOWN_IN_POWER_MENU);
        if (lockPatternUtils.isSecure(MY_USER_ID)) {
            mPowerMenuLockDown.setChecked((Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.LOCKDOWN_IN_POWER_MENU, 0) == 1));
            mPowerMenuLockDown.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(mPowerMenuLockDown);
        }

            mOnTheGoAlphaPref = (CustomSeekBarPreference) findPreference(PREF_ON_THE_GO_ALPHA);
            float otgAlpha = Settings.System.getFloat(getContentResolver(),
                Settings.System.ON_THE_GO_ALPHA, 0.5f);
            final int alpha = ((int) (otgAlpha * 100));
            mOnTheGoAlphaPref.setValue(alpha);
            mOnTheGoAlphaPref.setOnPreferenceChangeListener(this);

        if (!AquaUtils.deviceHasFlashlight(getContext())) {
            Preference toRemove = prefScreen.findPreference(TORCH_POWER_BUTTON_GESTURE);
            if (toRemove != null) {
                prefScreen.removePreference(toRemove);
            }
        } else {
            mTorchPowerButton = (ListPreference) findPreference(TORCH_POWER_BUTTON_GESTURE);
            int mTorchPowerButtonValue = Settings.Secure.getInt(resolver,
                    Settings.Secure.TORCH_POWER_BUTTON_GESTURE, 0);
            mTorchPowerButton.setValue(Integer.toString(mTorchPowerButtonValue));
            mTorchPowerButton.setSummary(mTorchPowerButton.getEntry());
            mTorchPowerButton.setOnPreferenceChangeListener(this);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

         if (preference == mTorchPowerButton) {
            int mTorchPowerButtonValue = Integer.valueOf((String) newValue);
            int index = mTorchPowerButton.findIndexOfValue((String) newValue);
            mTorchPowerButton.setSummary(
                    mTorchPowerButton.getEntries()[index]);
            Settings.Secure.putInt(resolver, Settings.Secure.TORCH_POWER_BUTTON_GESTURE,
                    mTorchPowerButtonValue);
            if (mTorchPowerButtonValue == 1) {
                //if doubletap for torch is enabled, switch off double tap for camera
                Settings.Secure.putInt(resolver, Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED,
                        1);
            }
            return true;
        }
        if (preference == mPowerMenuLockDown) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.LOCKDOWN_IN_POWER_MENU, value ? 1 : 0);
            return true;
            }
             if (preference == mOnTheGoAlphaPref) {
            float val = (Integer) newValue;
            Settings.System.putFloat(getActivity().getContentResolver(),
                    Settings.System.ON_THE_GO_ALPHA, val / 100);
            return true;
        }
        return false;
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
                    sir.xmlResId = R.xml.powermenu;
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
