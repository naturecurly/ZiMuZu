package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import java.util.Map;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public interface SchedulePresenter {
    void getSchedules(Context context, String id);

    void writeData(String res, Map map);

//    void readData(String res);
}
