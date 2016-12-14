package com.naturecurly.zimuzu.NetworkServices;

import com.naturecurly.zimuzu.Bean.EpisodesResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public interface ScheduleService {
    @GET("resource/season_episode")
    Observable<EpisodesResponse> getEpisodes(@QueryMap Map<String, String> accessInfo, @Query("id") String id);
}
