package com.sbmatch.androlua;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.util.JumpParameter;

import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import com.tencent.mmkv.MMKV;
import com.sbmatch.androlua.R;


public class DebugCrashActivity extends BaseActivity {

    public DebugCrashActivity() {
        setLayout(R.layout.debug_crash);
        setDarkStatusBarTheme(true);
        setNavigationBarBackgroundColor(Color.TRANSPARENT);
    }

    private TextView logTextView;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    // 此处用于绑定布局、View初始化等操作
    public void initViews() {
        logTextView = (TextView) findViewById(R.id.logText);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    // 请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
        
        Intent intent = getIntent();
        if (intent != null) {
            MMKV xCrashkv = MMKV.mmkvWithID("xcrash");
            
            String title = intent.getStringExtra("title");
            if (title != null) toolbar.setTitle(title);
            String logKey = intent.getStringExtra("logKey");
            if (logKey == null) return;
            String errorStackTrace = xCrashkv.decodeString(logKey);
            logTextView.setText(errorStackTrace);
            xCrashkv.encode(logKey, (String) null);
        }
    }

    @Override
    // 此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}