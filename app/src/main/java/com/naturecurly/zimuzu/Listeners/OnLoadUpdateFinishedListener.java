package com.naturecurly.zimuzu.Listeners;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/4/16.
 */

public interface OnLoadUpdateFinishedListener {
    void success(List dataSet);

    void fail();

    void unchanged();
}
