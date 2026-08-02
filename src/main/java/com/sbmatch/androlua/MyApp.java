package com.sbmatch.androlua;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.interfaces.OnBugReportListener;

import com.google.android.material.color.DynamicColors;
import com.sbmatch.helper.utils.MagicHelper;
import java.io.File;
import android.content.Intent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import xcrash.XCrash;
import xcrash.ICrashCallback;
import com.tencent.mmkv.MMKV;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.FileInputStream;


public class MyApp extends BaseApp<MyApp> {
    @Override
    public void init() {
        DynamicColors.applyToActivitiesIfAvailable(this);
        MagicHelper.init();

        MMKV.initialize(this);
        MMKV xCrashkv = MMKV.mmkvWithID("xcrash");

        ICrashCallback callback = new ICrashCallback() {
            @Override
            public void onCrash(String logPath, String emergency) {
                xCrashkv.encode("logPath", logPath);
                xCrashkv.encode("emergency", emergency);
            }
        };

        String nativeCrashPath = xCrashkv.decodeString("logPath", null);
        if (nativeCrashPath != null) {
            openCrashActivity("nativeCrashLog", nativeCrashPath, xCrashkv.decodeString("emergency", null), "上次崩溃日志");
        }

        XCrash.init(this, new XCrash.InitParameters()
                .setNativeLogCountMax(1)
                .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
                .setNativeDumpAllThreadsCountMax(10)
                .setNativeCallback(callback)
//          .setAnrCheckProcessState(false)
                // .setAnrRethrow(true)
                // .setAnrLogCountMax(10)
                // .setAnrCallback(callback)
                // .setAnrFastCallback(anrFastCallback)
                .setPlaceholderSizeKb(512)
                .setLogDir(getExternalFilesDir("xcrash").toString())
                // .setLogFileMaintainDelayMs(1000))
        );

        setOnCrashListener(new OnBugReportListener() {
            @Override
            public boolean onCrash(Exception e, final File crashLogFile) {
                // TODO: 请在这里处理异常信息
                // return true时，会在执行完上述代码后关闭 App，但 return false，则会拦截此错误，App 不会闪退，继续运行
                xCrashkv.encode("javaCrashLog", android.util.Log.getStackTraceString(e));
                openCrashActivity("javaCrashLog", null, null, null);
                return false;
            }
        });
    }

    void openCrashActivity(String logKey, String logPath, String emergency, String title) {
        Intent intent = new Intent(me, com.sbmatch.androlua.DebugCrashActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("logKey", logKey);
        intent.putExtra("logPath", logPath);
        intent.putExtra("emergency", emergency);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    String readFileString(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
                FileChannel channel = fis.getChannel()) {
            long size = channel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) size);
            channel.read(buffer);
            buffer.flip();
            return StandardCharsets.UTF_8.decode(buffer).toString();
        }
    }
}
