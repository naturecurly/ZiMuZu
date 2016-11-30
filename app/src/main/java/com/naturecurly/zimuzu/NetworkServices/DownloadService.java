package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.Aria2Response;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public interface DownloadService {
    @GET("/jsonrpc")
    Call<Aria2Response> download(@Query("method") String method, @Query("id") String id, @Query("params") String params);
}
