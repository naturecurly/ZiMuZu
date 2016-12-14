package com.naturecurly.zimuzu.Views;

import com.naturecurly.zimuzu.Bean.Series;

/**
 * Created by leveyleonhardt on 12/14/16.
 */

public interface SeriesDetailView {
    void updateView(Series series);

    void failToUpdate();
}
