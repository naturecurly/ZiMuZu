package com.naturecurly.zimuzu.Databases;

import android.content.Context;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.NewsResponse;
import com.naturecurly.zimuzu.Fragments.HomeFragment;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.NetworkServices.NewsService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public class NewsModelImpl implements NewsModel {
    @Override
    public void accessNewsFromInternet(Context context, int page, final OnLoadUpdateFinishedListener onLoadUpdateFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        NewsService newsService = retrofit.create(NewsService.class);
        Observable<NewsResponse> fetchNews = newsService.fetchNews(AccessUtils.generateAccessKey(context), "15", page + "");
        fetchNews.retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsResponse>() {
                    @Override
                    public void call(NewsResponse newsResponse) {
                        onLoadUpdateFinishedListener.success(newsResponse.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onLoadUpdateFinishedListener.fail();
                    }
                });
//                .subscribe(new Subscriber<NewsResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//
//                    @Override
//                    public void onNext(NewsResponse newsResponse) {
//                        dataSet.addAll(newsResponse.getData());
//                        if (page != 1) {
//                            recyclerView.getAdapter().notifyDataSetChanged();
//                        } else {
//                            recyclerView.setAdapter(new HomeFragment.NewsAdapter(dataSet));
//                        }
//                        if (swipeRefreshLayout != null) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                });
    }
}
