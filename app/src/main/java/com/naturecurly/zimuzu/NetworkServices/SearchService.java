package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.SearchResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public interface SearchService {
    @GET("search")
    Call<SearchResponse> search(@QueryMap Map<String, String> accessInfo, @Query("k") String keyword, @Query("st") String option);
}
