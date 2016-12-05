package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public interface NewsModel {
    void accessNewsFromInternet(Context context, int page, OnLoadUpdateFinishedListener onLoadUpdateFinishedListener);
}
