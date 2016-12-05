package com.naturecurly.zimuzu.Databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Bean.TopResponse;
import com.naturecurly.zimuzu.Fragments.RankFragment;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.NetworkServices.RankService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import java.util.List;
import java.util.Map;

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

public class RankModelImpl implements RankModel {
    @Override
    public void accessRankFromInternet(final Context context, final OnLoadUpdateFinishedListener onLoadUpdateFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        RankService rankService = retrofit.create(RankService.class);
        Map<String, String> paramsMap = AccessUtils.generateAccessKey(context);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Observable<TopResponse> rank = rankService.getTop(paramsMap, sharedPreferences.getString(context.getString(R.string.rank_number_key), context.getString(R.string.rank_limit)));
        rank.retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TopResponse>() {
                    @Override
                    public void call(TopResponse response) {
                        onLoadUpdateFinishedListener.success(response.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onLoadUpdateFinishedListener.fail();
                    }
                });
//                .subscribe(new Subscriber<TopResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(context, "Network timeout, please retry.", Toast.LENGTH_SHORT).show();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(TopResponse response) {
//                        List<Series> series = response.getData();
//                        recyclerView.setAdapter(new RankFragment.CardAdapter(series));
//                        if (swipeRefreshLayout != null) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                });
    }
}
