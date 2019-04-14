/*
 * Copyright (C) 2019 AquariOS
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
 *
 * Sets a sane color filter on a white drawable when system accent
 * is white
 */

package com.aquarios.coralreef.helpers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.settings.R;

public class WhiteAccentHelperImageView extends ImageView {

    public WhiteAccentHelperImageView(final Context context) {
        this(context, null);
    }

    public WhiteAccentHelperImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteAccentHelperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WhiteAccentHelperImageView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        if (isUsingWhiteAccent()) {
            setColorFilter(
                    context.getResources().getColor(R.color.top_banner_icon_color_white_accent),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }

    private static boolean isUsingWhiteAccent() {
        IOverlayManager om = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo("com.accents.white",
                    ActivityManager.getCurrentUser());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }
}
