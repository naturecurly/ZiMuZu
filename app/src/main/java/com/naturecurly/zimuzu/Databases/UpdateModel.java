package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.UpdateResponse;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;

/**
 * Created by leveyleonhardt on 12/4/16.
 */

public interface UpdateModel {
    void loadEntries(OnLoadUpdateFinishedListener onLoadUpdateFinishedListener);

    void writeEntries(Context context, UpdateResponse updateResponse);

    void getUpdate(Context context, OnLoadUpdateFinishedListener onLoadUpdateFinishedListener);
}
