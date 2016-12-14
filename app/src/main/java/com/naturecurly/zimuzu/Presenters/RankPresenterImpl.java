package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Databases.RankModel;
import com.naturecurly.zimuzu.Databases.RankModelImpl;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Views.RankView;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public class RankPresenterImpl implements RankPresenter, OnLoadUpdateFinishedListener {
    private RankModel rankModel;
    private RankView rankView;


    public RankPresenterImpl(RankView rankView) {
        this.rankModel = new RankModelImpl();
        this.rankView = rankView;
    }

    @Override
    public void getRank(Context context) {
        rankModel.accessRankFromInternet(context, this);
    }

    @Override
    public void success(List dataSet) {
        rankView.updateRecyclerView(dataSet);
    }

    @Override
    public void success(NewsDetail newsDetail) {

    }

    @Override
    public void fail() {
        rankView.failGetData();
    }

    @Override
    public void unchanged() {

    }
}
