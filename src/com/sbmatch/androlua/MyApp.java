package com.sbmatch.androlua;

import android.app.Application;
import android.content.Context;
import com.google.android.material.color.DynamicColors;
import com.sbmatch.helper.utils.MagicHelper;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MagicHelper.init();
    }
}