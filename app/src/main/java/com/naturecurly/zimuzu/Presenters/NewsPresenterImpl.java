package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Databases.NewsModel;
import com.naturecurly.zimuzu.Databases.NewsModelImpl;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Views.NewsView;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public class NewsPresenterImpl implements NewsPresenter, OnLoadUpdateFinishedListener {

    private NewsView newsView;
    private NewsModel newsModel;

    public NewsPresenterImpl(NewsView newsView) {
        this.newsView = newsView;
        this.newsModel = new NewsModelImpl();
    }

    @Override
    public void getNews(Context context, int page) {
        newsModel.accessNewsFromInternet(context, page, this);
    }

    @Override
    public void success(List dataSet) {
        newsView.updateRecyclerView(dataSet);
    }

    @Override
    public void success(NewsDetail newsDetail) {

    }

    @Override
    public void fail() {
        newsView.failGetData();
    }

    @Override
    public void unchanged() {

    }


}
