package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Bean.TopResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public interface RankService {
    @GET("resource/top")
    Observable<TopResponse> getTop(@QueryMap Map<String, String> accessInfo, @Query("limit") String limit);
}
