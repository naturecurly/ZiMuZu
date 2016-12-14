package com.naturecurly.zimuzu.Listeners;

import com.naturecurly.zimuzu.Views.ScheduleItem;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public interface OnSwitchWatchedListener {
    void watched(ScheduleItem.Cell cell);

    void unWatched(ScheduleItem.Cell cell);
}
