package com.naturecurly.zimuzu.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.EpisodesResponse;
import com.naturecurly.zimuzu.Bean.ScheduleData;
import com.naturecurly.zimuzu.Databases.EpisodeDataScheme.EpisodeTable;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.Listeners.OnScheduleLoadListener;
import com.naturecurly.zimuzu.NetworkServices.ScheduleService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public class ScheduleModelImpl implements ScheduleModel {
    @Override
    public void accessScheduleFromInternet(Context context, String id, final OnScheduleLoadListener onScheduleLoadListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);
        Observable<EpisodesResponse> getEpisodes = scheduleService.getEpisodes(AccessUtils.generateAccessKey(context), id).retry(2);
        Observable loadData = Observable.fromCallable(getReadCallable(id));
        Observable zipped = Observable.zip(loadData, getEpisodes, new Func2<Map, EpisodesResponse, ScheduleData>() {

            @Override
            public ScheduleData call(Map map, EpisodesResponse episodesResponse) {
                return new ScheduleData(map, episodesResponse);
            }
        });
        zipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ScheduleData>() {
                    @Override
                    public void call(ScheduleData scheduleData) {
                        Log.i("ScheduleModelImpl", "run");

                        onScheduleLoadListener.success(scheduleData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("scheduleException", throwable.getMessage());
                        onScheduleLoadListener.fail();
                    }
                });


//        getEpisodes.retry(3)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<EpisodesResponse>() {
//                    @Override
//                    public void call(EpisodesResponse episodesResponse) {
//                        onLoadUpdateFinishedListener.success(episodesResponse.getData());
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onLoadUpdateFinishedListener.fail();
//                    }
//                });
    }

    @Override
    public void writeDatabase(String res, Map watchedMap) {
        Observable observable = Observable.fromCallable(getWriteCallable(res, watchedMap));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

//    @Override
//    public void readDatabase(String res, final OnScheduleLoadListener onScheduleLoadListener) {
//        Observable<Map> observable = Observable.fromCallable(getReadCallable(res));
//
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .concatMap(new Func1<Map, Observable<?>>() {
//                    @Override
//                    public Observable<?> call(Map map) {
//                        return null;
//                    }
//                })
//
//    }


    private Callable getReadCallable(final String res) {
        return new Callable<Map>() {
            @Override
            public Map<String, Set<String>> call() throws Exception {
                String watchedString;
                Cursor cursor = DatabaseInstance.database.query(EpisodeTable.NAME, null, EpisodeTable.Cols.RESOURCE + "=?", new String[]{res}, null, null, null);
                int indexWatched = cursor.getColumnIndex(EpisodeTable.Cols.WATCHED);
                if (cursor.getCount() == 1) {
                    cursor.moveToNext();
                    watchedString = cursor.getString(indexWatched);

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
                    Type type = new TypeToken<Map<String, Set<String>>>() {
                    }.getType();
                    Map<String, Set<String>> map = gson.fromJson(watchedString, type);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return map;
                } else {
                    if (cursor != null) {
                        cursor.close();
                    }
                    return new HashMap<String, Set<String>>();
                }


            }
        };
    }

    private Callable getWriteCallable(final String res, final Map watchedMap) {
        return new Callable() {
            @Override
            public Object call() throws Exception {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.enableComplexMapKeySerialization().create();
                Type type = new TypeToken<Map<String, Set<String>>>() {
                }.getType();
                String json = gson.toJson(watchedMap, type);
                ContentValues contentValues = DatabaseUtils.generateScheduleContentValues(res, json);

                if (DatabaseInstance.database.update(EpisodeTable.NAME, contentValues, EpisodeTable.Cols.RESOURCE + "=?", new String[]{res}) == 0) {
                    DatabaseInstance.database.insert(EpisodeTable.NAME, null, contentValues);
                }
                return null;
            }
        };
    }
}
