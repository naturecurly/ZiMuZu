package com.naturecurly.zimuzu.Listeners;

import com.naturecurly.zimuzu.Bean.ScheduleData;

import java.util.List;
import java.util.Map;

/**
 * Created by leveyleonhardt on 12/13/16.
 */

public interface OnScheduleLoadListener {
    void success(ScheduleData data);

    void fail();
}
