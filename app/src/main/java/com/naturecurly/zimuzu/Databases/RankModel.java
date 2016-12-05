package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public interface RankModel {
    void accessRankFromInternet(Context context, OnLoadUpdateFinishedListener onLoadUpdateFinishedListener);
}
