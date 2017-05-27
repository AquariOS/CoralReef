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
import android.provider.Settings;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.util.aquarios.AwesomeAnimationHelper;

public class ListviewAnimation extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "ListviewAnimation";
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";

	private int[] mAnimations;
	private String[] mAnimationsStrings;
	private String[] mAnimationsNum;
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    protected Context mContext;

    protected ContentResolver mContentRes;
          
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.listview_animation_tab);
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

          mListViewAnimation = (ListPreference) findPreference(KEY_LISTVIEW_ANIMATION);
          int listviewanimation = Settings.System.getInt(getContentResolver(),
                  Settings.System.LISTVIEW_ANIMATION, 0);
          mListViewAnimation.setValue(String.valueOf(listviewanimation));
          mListViewAnimation.setSummary(mListViewAnimation.getEntry());
          mListViewAnimation.setOnPreferenceChangeListener(this);
    
          mListViewInterpolator = (ListPreference) findPreference(KEY_LISTVIEW_INTERPOLATOR);
          int listviewinterpolator = Settings.System.getInt(getContentResolver(),
                  Settings.System.LISTVIEW_INTERPOLATOR, 0);
          mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
          mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
          mListViewInterpolator.setOnPreferenceChangeListener(this);
          mListViewInterpolator.setEnabled(listviewanimation > 0);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		  boolean result = false;
		if (preference == mListViewAnimation) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION, value);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            mListViewInterpolator.setEnabled(value > 0);
            return true;
         } else if (preference == mListViewInterpolator) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR, value);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
            return true;
		 }
        return false;
         }
    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.AQUARIOS;
    }
}
