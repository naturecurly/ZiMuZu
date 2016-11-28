package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.NewsDetailResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public interface NewsDetailService {
    @GET("article/getinfo")
    Call<NewsDetailResponse> getInfo(@QueryMap Map<String, String> accessInfo, @Query("id") String id);
}
