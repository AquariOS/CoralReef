/*
 * Copyright (C) 2017 The Dirty Unicorns Project
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

package com.aquarios.coralreef.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.SearchIndexableResource;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settings.applications.LayoutPreference;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.utils.ActionHandler;
import com.android.internal.utils.ActionConstants;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settingslib.graph.BatteryMeterDrawableBase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StatusBarTab extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String BATTERY_CATEGORY = "battery_options_category";
    private static final String CLOCK_OPTIONS_CATEGORY = "clock_options_category";
    private static final String TRAFFIC_CATEGORY = "traffic_category";
    private static final String QUICK_SETTINGS_CATEGORY = "quick_settings_category";
    private static final String STATUS_BAR_ITEMS_CATEGORY = "status_bar_items_category";
    private static final String CORALREEF_CONFIGS_PREFIX = "coralreef_config_";

    private static final int MENU_RESET = Menu.FIRST;
    private static final int MENU_SAVE = Menu.FIRST + 1;
    private static final int MENU_RESTORE = Menu.FIRST + 2;

    private static final int DIALOG_RESET_CONFIRM = 1;
    private static final int DIALOG_RESTORE_PROFILE = 2;
    private static final int DIALOG_SAVE_PROFILE = 3;
    private static final String CONFIG_STORAGE = Environment.getExternalStorageDirectory()
            + File.separator
            + "coralreef_configs";

    private LayoutPreference mBattery;
    private LayoutPreference mClockOptions;
    private LayoutPreference mTraffic;
    private LayoutPreference mQuickSettings;
    private LayoutPreference mStatusBarItems;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.status_bar_tab);

        mBattery = (LayoutPreference) findPreference(BATTERY_CATEGORY);
        mBattery.setTitle(R.string.battery_options_title);

        mClockOptions = (LayoutPreference) findPreference(CLOCK_OPTIONS_CATEGORY);
        mClockOptions.setTitle(R.string.clock_options_title);

        mTraffic = (LayoutPreference) findPreference(TRAFFIC_CATEGORY);
        mTraffic.setTitle(R.string.traffic_title);

        mQuickSettings = (LayoutPreference) findPreference(QUICK_SETTINGS_CATEGORY);
        mQuickSettings.setTitle(R.string.quicksettings_title);

        mStatusBarItems = (LayoutPreference) findPreference(STATUS_BAR_ITEMS_CATEGORY);
        mStatusBarItems.setTitle(R.string.status_bar_items_title);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.aqua_reset_config_title)
                .setIcon(R.drawable.ic_menu_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_SAVE, 0, R.string.aqua_backup_current_config_title)
                .setIcon(R.drawable.ic_save_profile)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_RESTORE, 0, R.string.aqua_restore_config_title)
                .setIcon(R.drawable.ic_restore_profile)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                showDialog(DIALOG_RESET_CONFIRM);
                return true;
            case MENU_SAVE:
                showDialog(DIALOG_SAVE_PROFILE);
                return true;
            case MENU_RESTORE:
                showDialog(DIALOG_RESTORE_PROFILE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {
        switch (dialogId) {
            case DIALOG_RESET_CONFIRM: {
                Dialog dialog;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.aqua_factory_reset_title);
                alertDialog.setMessage(R.string.aqua_factory_reset_confirm);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetSettings(mContext);
                    }
                });
                alertDialog.setNegativeButton(R.string.write_settings_off, null);
                dialog = alertDialog.create();
                return dialog;
            }
            case DIALOG_RESTORE_PROFILE: {
                Dialog dialog;
                final ConfigAdapter configAdapter = new ConfigAdapter(getActivity(),
                        getConfigFiles(CONFIG_STORAGE));
                AlertDialog.Builder configDialog = new AlertDialog.Builder(getActivity());
                configDialog.setTitle(R.string.aqua_config_dialog_title);
                configDialog.setNegativeButton(getString(android.R.string.cancel), null);
                configDialog.setAdapter(configAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String resultMsg;
                        try {
                            File configFile = (File) configAdapter.getItem(item);
                            String config = getConfigFromStorage(configFile);
                            restoreConfig(getActivity(), config);
                            resultMsg = getString(R.string.aqua_config_restore_success_toast);
                        } catch (Exception e) {
                            resultMsg = getString(R.string.aqua_config_restore_error_toast);
                        }
                        Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog = configDialog.create();
                return dialog;
            }
            case DIALOG_SAVE_PROFILE: {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText input = new EditText(getActivity());
                builder.setTitle(getString(R.string.aqua_config_name_edit_dialog_title));
                builder.setMessage(R.string.aqua_config_name_edit_dialog_message);
                builder.setView(input);
                builder.setNegativeButton(getString(android.R.string.cancel), null);
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String inputText = input.getText().toString();
                                if (TextUtils.isEmpty(inputText)) {
                                    inputText = String.valueOf(android.text.format.DateFormat
                                            .format(
                                                    "yyyy-MM-dd_hh:mm:ss", new java.util.Date()));
                                }
                                String resultMsg;
                                try {
                                    String currentConfig = getCurrentConfig(getActivity());
                                    backupCoralReefConfig(currentConfig, inputText);
                                    resultMsg = getString(R.string.aqua_config_backup_success_toast);
                                } catch (Exception e) {
                                    resultMsg = getString(R.string.aqua_config_backup_error_toast);
                                }
                                Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                return dialog;
            }
        }
        return super.onCreateDialog(dialogId);
    }

    @Override
    public int getDialogMetricsCategory(int dialogId) {
        switch (dialogId) {
            case DIALOG_RESET_CONFIRM:
            case DIALOG_RESTORE_PROFILE:
            case DIALOG_SAVE_PROFILE:
                return MetricsProto.MetricsEvent.AQUA;
            default:
                return 0;
        }
    }

    public void resetSettings(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();

        Settings.System.putIntForUser(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 1, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 4, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_LOCATION, 0, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_COLOR, 0xFFFFFFFF, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_CHARGING_COLOR, 0xFFFFFFFF, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_BATTERY_LOW_COLOR_WARNING, 0xFFFFFFFF, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_LOW_COLOR, 0xFFFFFFFF, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_HIGH_COLOR, 0xFFFFFFFF, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_USE_GRADIENT_COLOR, 0, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_ANIMATE, 0, UserHandle.USER_CURRENT);
        Settings.System.putIntForUser(resolver,
                Settings.System.BATTERY_BAR_THICKNESS, 2, UserHandle.USER_CURRENT);
        StatusBarTab.resetSettings(mContext);
    }

    static class ConfigAdapter extends ArrayAdapter<File> {
        private final ArrayList<File> mConfigFiles;
        private final Context mContext;

        public ConfigAdapter(Context context, ArrayList<File> files) {
            super(context, android.R.layout.select_dialog_item, files);
            mContext = context;
            mConfigFiles = files;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemRow = convertView;
            File f = mConfigFiles.get(position);
            itemRow = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(android.R.layout.select_dialog_item, null);
            String name = f.getName();
            if (name.startsWith(CORALREEF_CONFIGS_PREFIX)) {
                name = f.getName().substring(CORALREEF_CONFIGS_PREFIX.length(), f.getName().length());
            }
            ((TextView) itemRow.findViewById(android.R.id.text1)).setText(name);

            return itemRow;
        }
    }

    private static class StartsWithFilter implements FileFilter {
        private String[] mStartsWith;

        public StartsWithFilter(String[] startsWith) {
            mStartsWith = startsWith;
        }

        @Override
        public boolean accept(File file) {
            for (String extension : mStartsWith) {
                if (file.getName().toLowerCase().startsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }

    static String getCurrentConfig(Context ctx) {
        String config = Settings.Secure.getStringForUser(
                ctx.getContentResolver(), ActionConstants.getDefaults(ActionConstants.SMARTBAR)
                        .getUri(),
                UserHandle.USER_CURRENT);
        if (TextUtils.isEmpty(config)) {
            config = ActionConstants.getDefaults(ActionConstants.SMARTBAR).getDefaultConfig();
        }
        return config;
    }

    static void restoreConfig(Context context, String config) {
        Settings.Secure.putStringForUser(context.getContentResolver(),
                ActionConstants.getDefaults(ActionConstants.SMARTBAR)
                        .getUri(), config,
                UserHandle.USER_CURRENT);
        Intent intent = new Intent("intent_navbar_edit_reset_layout");
        ActionHandler.dispatchNavigationEditorResult(intent);
    }

    static void backupCoralReefConfig(String config, String suffix) {
        File dir = new File(CONFIG_STORAGE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File configFile = new File(dir, CORALREEF_CONFIGS_PREFIX + suffix);
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(configFile);
            stream.write(config.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getConfigFromStorage(File file) {
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String contents = new String(bytes);
        return contents;
    }

    public static ArrayList<File> getConfigFiles(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ArrayList<File> list = new ArrayList<File>();
        for (File tmp : dir.listFiles(new StartsWithFilter(new String[] {
                CORALREEF_CONFIGS_PREFIX
        }))) {
            list.add(tmp);
        }
        return list;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.AQUA;
    }
}
