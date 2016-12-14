package com.naturecurly.zimuzu.Databases;

import android.content.Context;

import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Bean.NewsDetailResponse;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.NetworkServices.NewsDetailService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public class NewsContentModelImpl implements NewsContentModel {
    @Override
    public void accessNewsContentFromInternet(Context context, String id, final OnLoadUpdateFinishedListener onLoadUpdateFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        NewsDetailService newsDetailService = retrofit.create(NewsDetailService.class);

        Observable<NewsDetailResponse> getNews = newsDetailService.getInfo(AccessUtils.generateAccessKey(context), id);
        getNews.retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetailResponse>() {
                    @Override
                    public void call(NewsDetailResponse newsDetailResponse) {
                        onLoadUpdateFinishedListener.success(newsDetailResponse.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onLoadUpdateFinishedListener.fail();
                    }
                });

//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response.isSuccessful()) {
//                    NewsDetailResponse newsDetailResponse = (NewsDetailResponse) response.body();
//                    NewsDetail newsDetail = newsDetailResponse.getData();
//                    fillContent(newsDetail.getTitle(), newsDetail.getContent(), newsDetail.getPoster());
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
    }
}
