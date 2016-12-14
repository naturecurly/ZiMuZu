package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Listeners.OnScheduleLoadListener;

import java.util.Map;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public interface ScheduleModel {
    void accessScheduleFromInternet(Context context, String id, OnScheduleLoadListener onScheduleLoadListener);

    void writeDatabase(String res, Map watchedMap);

//    void readDatabase(String res, OnScheduleLoadListener onScheduleLoadListener);
}
