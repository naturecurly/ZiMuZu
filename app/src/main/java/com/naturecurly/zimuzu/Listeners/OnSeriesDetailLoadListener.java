package com.naturecurly.zimuzu.Listeners;

import com.naturecurly.zimuzu.Bean.Series;

/**
 * Created by leveyleonhardt on 12/14/16.
 */

public interface OnSeriesDetailLoadListener {
    void success(Series series);

    void fail();
}
