package com.naturecurly.zimuzu.Views;

import com.naturecurly.zimuzu.Bean.ScheduleData;

import java.util.List;
import java.util.Map;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public interface ScheduleView {
    void updateRecyclerView(ScheduleData scheduleData);

    void failGetData();

    void fillWatchedData(Map map);
}
