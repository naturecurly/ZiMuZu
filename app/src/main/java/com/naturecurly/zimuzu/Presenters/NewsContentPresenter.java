package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public interface NewsContentPresenter {
    void getNewsContent(Context context,String id);

    void shareNews();
}
