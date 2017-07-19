/*
* Copyright (C) 2015-2017 The OwnROM Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.aquarios.settings.animation.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.provider.Settings;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.util.aquarios.AwesomeAnimationHelper;

public class ToastAnimation extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "ToastAnimation";
	private static final String KEY_TOAST_ANIMATION = "toast_animation";

	private ListPreference mToastAnimation;

	private int[] mAnimations;
	private String[] mAnimationsStrings;
	private String[] mAnimationsNum;
    
    protected Context mContext;

    protected ContentResolver mContentRes;
          
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.toast_animation_tab);
		mContext = getActivity().getApplicationContext();
		mContentRes = getActivity().getContentResolver();

           mAnimations = AwesomeAnimationHelper.getAnimationsList();
           int animqty = mAnimations.length;
           mAnimationsStrings = new String[animqty];
           mAnimationsNum = new String[animqty];
           for (int i = 0; i < animqty; i++) {
               mAnimationsStrings[i] = AwesomeAnimationHelper.getProperName(mContext, mAnimations[i]);
               mAnimationsNum[i] = String.valueOf(mAnimations[i]);
           }

          mToastAnimation = (ListPreference) findPreference(KEY_TOAST_ANIMATION);
          mToastAnimation.setSummary(mToastAnimation.getEntry());
          int CurrentToastAnimation = Settings.System.getInt(getContentResolver(), Settings.System.TOAST_ANIMATION, 1);
          mToastAnimation.setValueIndex(CurrentToastAnimation); //set to index of default value
          mToastAnimation.setSummary(mToastAnimation.getEntries()[CurrentToastAnimation]);
          mToastAnimation.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		  boolean result = false;
		if (preference == mToastAnimation) {
              int index = mToastAnimation.findIndexOfValue((String) newValue);
              Settings.System.putString(getContentResolver(), Settings.System.TOAST_ANIMATION, (String) newValue);
              mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
              Toast.makeText(mContext, "Toast Test", Toast.LENGTH_SHORT).show();
              return true;
         }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.AQUARIOS;
    }
}
