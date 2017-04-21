/*
 * Copyright (C) 2015-2016 The Dirty Unicorns Project
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

package com.aquarios.settings.fragments;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.ContentResolver;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.Utils;

 import com.aquarios.settings.preference.CustomSeekBarPreference;

public class QuickSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PREF_COLUMNS = "qs_layout_columns";
    private static final String PREF_LOCK_QS_DISABLED = "lockscreen_qs_disabled";

    private CustomSeekBarPreference mQsColumns;
    private SwitchPreference mLockQsDisabled;

    private static final int MY_USER_ID = UserHandle.myUserId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.quicksettings);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();
        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());

        int defaultValue;

        mQsColumns = (CustomSeekBarPreference) findPreference(PREF_COLUMNS);
        int columnsQs = Settings.System.getInt(resolver,
                Settings.System.QS_LAYOUT_COLUMNS, 3);
        mQsColumns.setValue(columnsQs / 1);
        mQsColumns.setOnPreferenceChangeListener(this);

        mLockQsDisabled = (SwitchPreference) findPreference(PREF_LOCK_QS_DISABLED);
        if (lockPatternUtils.isSecure(MY_USER_ID)) {
            mLockQsDisabled.setChecked((Settings.Secure.getInt(resolver,
                Settings.Secure.LOCK_QS_DISABLED, 0) == 1));
            mLockQsDisabled.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mLockQsDisabled);
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.CUSTOM_SQUASH;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {

        int intValue;
        int index;

        if (preference == mQsColumns) {
        int qsColumns = (Integer) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_LAYOUT_COLUMNS, qsColumns * 1);
            return true;
        } else if  (preference == mLockQsDisabled) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.LOCK_QS_DISABLED, checked ? 1:0);
            return true;
        }
        return false;
    }
}
