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
 import android.os.SystemProperties;
 import android.support.v7.preference.ListPreference;
 import android.support.v14.preference.SwitchPreference;
 import android.support.v7.preference.Preference;
 import android.support.v7.preference.Preference.OnPreferenceChangeListener;
 import android.support.v7.preference.PreferenceScreen;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Toast;
 import android.provider.Settings;
 
 
 import com.android.settings.R;
 import com.android.settings.SettingsPreferenceFragment;
 import com.android.settings.Utils;
 
 import com.android.internal.util.aquarios.AwesomeAnimationHelper;
 
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashSet;
 import java.util.List;
 
 import com.android.internal.logging.MetricsProto.MetricsEvent;

public class PowerMenuAnimation extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "PowerMenuAnimation";
    
    private static final String POWER_MENU_ANIMATIONS = "power_menu_animations";
    
    private ListPreference mPowerMenuAnimations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.power_menu_animation_tab);
 		
        mPowerMenuAnimations = (ListPreference) findPreference(POWER_MENU_ANIMATIONS);
        mPowerMenuAnimations.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.POWER_MENU_ANIMATIONS, 0)));
        mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
        mPowerMenuAnimations.setOnPreferenceChangeListener(this);

    }

     @Override
     public boolean onPreferenceChange(Preference preference, Object newValue) {
 		  boolean result = false;
		if (preference == mPowerMenuAnimations) {
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_MENU_ANIMATIONS,
                    Integer.valueOf((String) newValue));
            mPowerMenuAnimations.setValue(String.valueOf(newValue));
            mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
            return true;
		 }  
          return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.AQUARIOS;
    }
}
