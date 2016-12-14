package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Bean.ScheduleData;
import com.naturecurly.zimuzu.Databases.ScheduleModel;
import com.naturecurly.zimuzu.Databases.ScheduleModelImpl;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Listeners.OnScheduleLoadListener;
import com.naturecurly.zimuzu.Views.ScheduleView;

import java.util.List;
import java.util.Map;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public class SchedulePresenterImpl implements SchedulePresenter, OnScheduleLoadListener {
    private ScheduleModel scheduleModel;
    private ScheduleView scheduleView;

    public SchedulePresenterImpl(ScheduleView scheduleView) {
        this.scheduleModel = new ScheduleModelImpl();
        this.scheduleView = scheduleView;
    }

    @Override
    public void getSchedules(Context context, String id) {
        scheduleModel.accessScheduleFromInternet(context, id, this);
    }

    @Override
    public void writeData(String res, Map map) {
        scheduleModel.writeDatabase(res, map);
    }

//    @Override
//    public void readData(String res) {
//        scheduleModel.readDatabase(res, this);
//    }


    @Override
    public void success(ScheduleData data) {
        scheduleView.updateRecyclerView(data);
    }

    @Override
    public void fail() {

    }
}
