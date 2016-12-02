package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.UpdateResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public interface TodayUpdateService {
    @GET("resource/today")
    Observable<UpdateResponse> getUpdate(@QueryMap Map<String, String> accessInfo);
}
