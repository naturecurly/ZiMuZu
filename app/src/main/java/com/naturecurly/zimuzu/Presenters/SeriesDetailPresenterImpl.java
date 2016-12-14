package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Databases.SeriesDetailModel;
import com.naturecurly.zimuzu.Databases.SeriesDetailModelImpl;
import com.naturecurly.zimuzu.Listeners.OnSeriesDetailLoadListener;
import com.naturecurly.zimuzu.Views.SeriesDetailView;

/**
 * Created by leveyleonhardt on 12/14/16.
 */

public class SeriesDetailPresenterImpl implements SeriesDetailPresenter, OnSeriesDetailLoadListener {


    private SeriesDetailModel seriesDetailModel;
    private SeriesDetailView seriesDetailView;

    public SeriesDetailPresenterImpl(SeriesDetailView seriesDetailView) {
        this.seriesDetailView = seriesDetailView;
        this.seriesDetailModel = new SeriesDetailModelImpl();
    }

    @Override
    public void getSeriesData(Context context, String id) {
        seriesDetailModel.accessDataFromInternet(context, id, this);
    }

    @Override
    public void success(Series series) {
        seriesDetailView.updateView(series);
    }

    @Override
    public void fail() {
        seriesDetailView.failToUpdate();
    }
}
