package com.naturecurly.zimuzu.Presenters;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Databases.UpdateModel;
import com.naturecurly.zimuzu.Databases.UpdateModelImpl;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Views.UpdateView;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/4/16.
 */

public class UpdatePresenterImpl implements UpdatePresenter, OnLoadUpdateFinishedListener {
    private UpdateModel mUpdateModel;
    private UpdateView mUpdateView;

    public UpdatePresenterImpl(UpdateView updateView) {
        this.mUpdateView = updateView;
        this.mUpdateModel = new UpdateModelImpl();
    }

    @Override
    public void readData() {
        mUpdateModel.loadEntries(this);
    }

    @Override
    public void updateDate(Context context) {
        mUpdateModel.getUpdate(context, this);
    }

    @Override
    public void success(List dataSet) {
        mUpdateView.updateRecyclerView(dataSet);
    }

    @Override
    public void fail() {
        mUpdateView.failGetData();
    }

    @Override
    public void unchanged() {
        mUpdateView.unchaged();
    }
}
