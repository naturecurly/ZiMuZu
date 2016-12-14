package com.naturecurly.zimuzu.Databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Bean.UpdateResponse;
import com.naturecurly.zimuzu.Listeners.OnLoadUpdateFinishedListener;
import com.naturecurly.zimuzu.NetworkServices.TodayUpdateService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 12/4/16.
 */

public class UpdateModelImpl implements UpdateModel {


    @Override
    public void loadEntries(final OnLoadUpdateFinishedListener onLoadUpdateFinishedListener) {
        Observable<List<Update>> loadObservable = Observable.fromCallable(getReadCallable());
        loadObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Update>>() {
                    @Override
                    public void call(List<Update> updates) {
                        onLoadUpdateFinishedListener.success(updates);
                    }
                });
    }

    @Override
    public void writeEntries(Context context, UpdateResponse response) {
    }


    @Override
    public void getUpdate(final Context context, final OnLoadUpdateFinishedListener onLoadUpdateFinishedListener) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.baseUrl)).build();
        TodayUpdateService todayUpdateService = retrofit.create(TodayUpdateService.class);
        Observable<UpdateResponse> getUpdate = todayUpdateService.getUpdate(AccessUtils.generateAccessKey(context));
        getUpdate.retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Func1<UpdateResponse, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(UpdateResponse updateResponse) {
                        return Observable.fromCallable(getWriteCallable(context, updateResponse));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            loadEntries(onLoadUpdateFinishedListener);
                        } else {
                            onLoadUpdateFinishedListener.unchanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onLoadUpdateFinishedListener.fail();
                    }
                });
    }

    private Callable<List<Update>> getReadCallable() {
        return new Callable<List<Update>>() {
            @Override
            public List<Update> call() throws Exception {
                List<Update> dataSet = new ArrayList<>();
                Cursor cursor = DatabaseInstance.database.query(UpdateDataScheme.UpdateTable.NAME, null, null, null, null, null, "id" + " DESC");
                int idIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.ID);
                int resourceIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.RESOURCE);
                int nameIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.NAME);
                int formatIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.FORMAT);
                int seasonIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.SEASON);
                int episodeIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.EPISODE);
                int sizeIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.SIZE);
                int cnnameIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.CNNAME);
                int channelIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.CHANNEL);
                int linkIndex = cursor.getColumnIndex(UpdateDataScheme.UpdateTable.Cols.WAY);

                while (cursor.moveToNext()) {
                    Update update = new Update(cursor.getString(idIndex), cursor.getString(resourceIndex), cursor.getString(nameIndex), cursor.getString(formatIndex), cursor.getString(seasonIndex), cursor.getString(episodeIndex), cursor.getString(sizeIndex), cursor.getString(cnnameIndex), cursor.getString(channelIndex), cursor.getString(linkIndex));
                    dataSet.add(update);
                }
                if (cursor != null) {
                    cursor.close();
                }
                return dataSet;
            }
        };

    }

    private Callable getWriteCallable(final Context context, final UpdateResponse updateResponse) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean hasUpdate = false;
                SharedPreferences preferences = context.getSharedPreferences("zimuzu", Context.MODE_PRIVATE);
                int flag = preferences.getInt("updateId", 0);
                List<Update> updateList = updateResponse.getData();
                if (updateList.size() != 0) {
                    preferences.edit().putInt("updateId", Integer.parseInt(updateList.get(0).getId())).apply();
                }
                for (Update item : updateList) {
//                    if (Integer.parseInt(item.getId()) > flag) {
                    Cursor cursor = DatabaseInstance.database.query(FavDataScheme.FavTable.NAME, null, FavDataScheme.FavTable.Cols.ID + "= ?", new String[]{item.getResourceid()}, null, null, null);
                    Cursor cursor2 = DatabaseInstance.database.query(UpdateDataScheme.UpdateTable.NAME, null, UpdateDataScheme.UpdateTable.Cols.ID + "= ?", new String[]{item.getId()}, null, null, null);
                    if (cursor.getCount() != 0 && cursor2.getCount() == 0) {
                        if (DatabaseInstance.database.insert(UpdateDataScheme.UpdateTable.NAME, null, DatabaseUtils.generateUpdateContentValues(item)) != -1) {
                            hasUpdate = true;
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (cursor2 != null) {
                        cursor2.close();
                    }
//                    }
                }
                return hasUpdate;
            }
        };
    }
}
