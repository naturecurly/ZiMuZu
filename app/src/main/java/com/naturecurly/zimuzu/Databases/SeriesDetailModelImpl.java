package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.DetailResponse;
import com.naturecurly.zimuzu.Listeners.OnSeriesDetailLoadListener;
import com.naturecurly.zimuzu.NetworkServices.SeriesDetailService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 12/14/16.
 */

public class SeriesDetailModelImpl implements SeriesDetailModel {
    @Override
    public void accessDataFromInternet(Context context, String id, final OnSeriesDetailLoadListener onSeriesDetailLoadListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        SeriesDetailService seriesDetailService = retrofit.create(SeriesDetailService.class);
        Observable<DetailResponse> getDetail = seriesDetailService.getDetail(AccessUtils.generateAccessKey(context), id);

        getDetail.retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DetailResponse>() {
                    @Override
                    public void call(DetailResponse detailResponse) {
                        onSeriesDetailLoadListener.success(detailResponse.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onSeriesDetailLoadListener.fail();
                    }
                });

    }
}
