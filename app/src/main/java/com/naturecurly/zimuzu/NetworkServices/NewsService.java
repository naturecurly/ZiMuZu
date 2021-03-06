package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.NewsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public interface NewsService {
    @GET("article/fetchlist")
    Observable<NewsResponse> fetchNews(@QueryMap Map<String, String> accessInfo, @Query("limit") String limit, @Query("page") String page);
}
