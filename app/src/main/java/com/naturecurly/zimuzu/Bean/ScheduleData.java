package com.naturecurly.zimuzu.Bean;

import java.util.Map;

/**
 * Created by leveyleonhardt on 12/13/16.
 */

public class ScheduleData {
    private Map localData;
    private EpisodesResponse remoteData;

    public ScheduleData(Map localData, EpisodesResponse remoteData) {
        this.localData = localData;
        this.remoteData = remoteData;
    }

    public Map getLocalData() {
        return localData;
    }

    public void setLocalData(Map localData) {
        this.localData = localData;
    }

    public EpisodesResponse getRemoteData() {
        return remoteData;
    }

    public void setRemoteData(EpisodesResponse remoteData) {
        this.remoteData = remoteData;
    }
}
