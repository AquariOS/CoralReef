/*
 * Copyright (C) 2019 AquariOS
 * Copyright (C) 2018 The Dirty Unicorns Project
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

package com.aquarios.coralreef;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.aquarios.coralreef.navigation.BottomNavigationViewCustom;
import com.aquarios.coralreef.tabs.ButtonsTab;
import com.aquarios.coralreef.tabs.LockScreenTab;
import com.aquarios.coralreef.tabs.StatusBarTab;
import com.aquarios.coralreef.tabs.NotificationsTab;
import com.aquarios.coralreef.tabs.SystemMiscTab;

public class CoralReef extends SettingsPreferenceFragment {

    public CoralReef() {
    }

    MenuItem menuitem;

    PagerAdapter mPagerAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.coralreef, container, false);

        final BottomNavigationViewCustom navigation = view.findViewById(R.id.navigation);

        final ViewPager viewPager = view.findViewById(R.id.viewpager);

        navigation.setBackground(new ColorDrawable(getResources().getColor(R.color.BottomBarBackgroundColor)));

        mPagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationViewCustom.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buttons_tab:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.lockscreen_tab:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.status_bar_tab:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.notifications_tab:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.system_misc_tab:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(menuitem != null) {
                    menuitem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                menuitem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new ButtonsTab();
            frags[1] = new LockScreenTab();
            frags[2] = new StatusBarTab();
            frags[3] = new NotificationsTab();
            frags[4] = new SystemMiscTab();
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
                getString(R.string.bottom_nav_buttons_title),
                getString(R.string.bottom_nav_lockscreen_title),
                getString(R.string.bottom_nav_status_bar_title),
                getString(R.string.bottom_nav_notifications_title),
                getString(R.string.bottom_nav_system_misc_title)};

        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }
}
