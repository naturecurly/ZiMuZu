package com.naturecurly.zimuzu.Listeners;

import com.naturecurly.zimuzu.Bean.NewsDetail;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/4/16.
 */

public interface OnLoadUpdateFinishedListener {
    void success(List dataSet);

    void success(NewsDetail newsDetail);

    void fail();

    void unchanged();
}
