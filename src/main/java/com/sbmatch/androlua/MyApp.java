package com.sbmatch.androlua;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.interfaces.OnBugReportListener;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.color.DynamicColors;
import com.sbmatch.helper.utils.MagicHelper;
import java.io.File;
import android.content.Intent;
import java.io.StringWriter;
import java.io.PrintWriter;

public class MyApp extends BaseApp<MyApp> {
    @Override
    public void init() {
        DynamicColors.applyToActivitiesIfAvailable(this);
        MagicHelper.init();

        setOnCrashListener(new OnBugReportListener() {
            @Override
            public boolean onCrash(Exception e, final File crashLogFile) {

                Intent intent = new Intent(me, com.sbmatch.androlua.DebugCrashActivity.class);
                intent.putExtra("crash_log_path", crashLogFile.getAbsolutePath());
                intent.putExtra("error_stacktrace", getStackTraceString(e));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return false;
            }
        });
    }

    private String getStackTraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
