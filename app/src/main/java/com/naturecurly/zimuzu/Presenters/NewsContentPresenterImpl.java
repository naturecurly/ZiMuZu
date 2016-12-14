package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Databases.NewsContentModel;
import com.naturecurly.zimuzu.Databases.NewsContentModelImpl;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Views.NewsContentView;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public class NewsContentPresenterImpl implements NewsContentPresenter, OnLoadUpdateFinishedListener {

    private NewsContentModel newsContentModel;
    private NewsContentView newsContentView;

    public NewsContentPresenterImpl(NewsContentView newsContentView) {
        this.newsContentView = newsContentView;
        this.newsContentModel = new NewsContentModelImpl();
    }

    @Override
    public void getNewsContent(Context context, String id) {
        newsContentModel.accessNewsContentFromInternet(context, id, this);
    }

    @Override
    public void shareNews() {

    }

    @Override
    public void success(List dataSet) {

    }

    @Override
    public void success(NewsDetail newsDetail) {
        newsContentView.fillContent(newsDetail.getTitle(), newsDetail.getContent(), newsDetail.getPoster());
    }

    @Override
    public void fail() {
        newsContentView.failGetData();
    }

    @Override
    public void unchanged() {

    }
}
