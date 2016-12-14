package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Listeners.OnSeriesDetailLoadListener;

/**
 * Created by leveyleonhardt on 12/14/16.
 */

public interface SeriesDetailModel {
    void accessDataFromInternet(Context context, String id, OnSeriesDetailLoadListener onSeriesDetailLoadListener);
}
