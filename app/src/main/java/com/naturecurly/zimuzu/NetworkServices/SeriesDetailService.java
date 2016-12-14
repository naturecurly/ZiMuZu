package com.naturecurly.zimuzu.NetworkServices;


import com.naturecurly.zimuzu.Bean.DetailResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public interface SeriesDetailService {
    @GET("resource/getinfo")
    Observable<DetailResponse> getDetail(@QueryMap Map<String, String> accessInfo, @Query("id") String id);
}
