package com.sbmatch.androlua;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.util.JumpParameter;
import android.content.res.AssetManager;
import android.os.Bundle;
import org.keplerproject.luajava.*;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;
import java.io.*;
import com.sbmatch.androlua.R;

public class EntryActivity extends BaseActivity {

    public EntryActivity() {
        setLayout(R.layout.entry_layout);
        setDarkStatusBarTheme(true);
        setNavigationBarBackgroundColor(Color.TRANSPARENT);
    }

    private androidx.appcompat.widget.Toolbar toolbar;
    private LinearLayout content_layout;
    private LuaState L;

    @Override
    // 此处用于绑定布局、View初始化等操作
    public void initViews() {
        content_layout = (LinearLayout) findViewById(R.id.content_layout);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    // 请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
        try {
            initAndroidEnvWithLua();
            evalLua("require 'init'");
        } catch (LuaException e) {
            throw new RuntimeException("Lua Initialization Failed: " + e.getMessage(), e);
        }
        Intent intent = getIntent();
        if (intent != null) {
            // 从 Intent 里获取要执行的 Lua 脚本路径或代码
            String luaScript = intent.getStringExtra("LUA_SCRIPT");
            String title = intent.getStringExtra("title");
            if (title != null) toolbar.setTitle(title);
            try {
                evalLua(luaScript);
            } catch (LuaException e) {
                throw new RuntimeException("Lua 脚本执行异常: " + e.getMessage(), e);
            }
        }
    }

    @Override
    // 此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    void initAndroidEnvWithLua() throws LuaException {

        L = LuaStateFactory.newLuaState();
        L.openLibs();
        L.pushJavaObject(this);
        L.setGlobal("activity");

        JavaFunction assetLoader = new JavaFunction(L) {
            @Override
            public int execute() throws LuaException {
                String name = L.toString(-1);
                String fileName = name.replace('.', '/');
                AssetManager am = getAssets();
                try {
                    InputStream is = am.open(fileName + ".lua");
                    byte[] bytes = readAll(is);
                    L.LloadBuffer(bytes, name);
                    return 1;
                } catch (Exception e) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(os));
                    L.pushString("Cannot load module " + name + ":\n" + os.toString());
                    return 1;
                }
            }
        };

        L.getGlobal("package"); // package
        L.getField(-1, "searchers"); // package loaders
        int nLoaders = L.objLen(-1); // package loaders

        L.pushJavaFunction(assetLoader); // package loaders loader
        L.rawSetI(-2, nLoaders + 1); // package loaders
        L.pop(1); // package

        L.getField(-1, "path"); // package path
        String customPath = getFilesDir() + "/?.lua";
        L.pushString(";" + customPath); // package path custom
        L.concat(2); // package pathCustom
        L.setField(-2, "path"); // package
        L.pop(1);
    }

    private static byte[] readAll(InputStream input) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    void evalLua(String src) throws LuaException {
        L.setTop(0);
        int ok = L.LloadString(src);
        if (ok == 0) {
            L.getGlobal("debug");
            L.getField(-1, "traceback");
            L.remove(-2);
            L.insert(-2);
            ok = L.pcall(0, 0, -2);
            if (ok != 0) {
                throw new LuaException(L.toString(-1));
            }
        }
    }

}