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

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.aquarios.coralreef.tabs.ActionsTab;
import com.aquarios.coralreef.tabs.InterfaceTab;
import com.aquarios.coralreef.tabs.StatusBarTab;
import com.aquarios.coralreef.tabs.LockScreenTab;
import com.aquarios.coralreef.tabs.SystemMiscTab;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem;

public class CoralReef extends SettingsPreferenceFragment {

    Context mContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = getActivity();
        Resources res = getResources();
        Window win = getActivity().getWindow();

        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        win.setNavigationBarColor(res.getColor(R.color.coralreef_navbar_color));
        win.setNavigationBarDividerColor(res.getColor(R.color.coralreef_navbar_color));

        view = inflater.inflate(R.layout.coralreef, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.coralreef_title);
        }

        ExpandableBottomBar bottomBar = view.findViewById(R.id.expandable_bottom_bar);

        Fragment tab_actions = new ActionsTab();
        Fragment tab_interface = new InterfaceTab();
        Fragment tab_status_bar = new StatusBarTab();
        Fragment tab_lock_screen = new LockScreenTab();
        Fragment tab_system_misc = new SystemMiscTab();

        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, tab_actions);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        bottomBar.addItems(new ExpandableBottomBarMenuItem.Builder(mContext)
                .addItem(R.id.tab_actions,
                        R.drawable.bottomnav_actions,
                        R.string.bottom_nav_actions_title, getThemeAccentColor(mContext))
                .addItem(R.id.tab_interface,
                        R.drawable.bottomnav_interface,
                        R.string.bottom_nav_interface_title, getThemeAccentColor(mContext))
                .addItem(R.id.tab_status_bar,
                        R.drawable.bottomnav_lock_screen,
                        R.string.bottom_nav_status_bar_title, getThemeAccentColor(mContext))
                .addItem(R.id.tab_lock_screen,
                        R.drawable.bottomnav_status_bar,
                        R.string.bottom_nav_lock_screen_title, getThemeAccentColor(mContext))
                .addItem(R.id.tab_system_misc,
                        R.drawable.bottomnav_system_misc,
                        R.string.bottom_nav_system_misc_title, getThemeAccentColor(mContext))
                .build()
        );

        bottomBar.setOnItemSelectedListener((view, menuItem) -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.tab_actions:
                    launchFragment(tab_actions);
                    break;
                case R.id.tab_interface:
                    launchFragment(tab_interface);
                    break;
                case R.id.tab_status_bar:
                    launchFragment(tab_status_bar);
                    break;
                case R.id.tab_lock_screen:
                    launchFragment(tab_lock_screen);
                    break;
                case R.id.tab_system_misc:
                    launchFragment(tab_system_misc);
                    break;
            }
            return null;
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    private void launchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }

    // Clears the ActionTab options menu
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    // Should be able to add desired options here if wanted
    }

    @Override
    public void onResume() {
        super.onResume();

        view = getView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK) {
                getActivity().finish();
                return true;
            }
            return false;
        });
    }

    public static int getThemeAccentColor (final Context context) {
        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (android.R.attr.colorAccent, value, true);
        return value.data;
    }
}
