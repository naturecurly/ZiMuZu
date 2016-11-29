package com.naturecurly.zimuzu.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class StatusBarUtils {
    public static void transparentStatusBar(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = ((AppCompatActivity) context).getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            ((AppCompatActivity) context).getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
