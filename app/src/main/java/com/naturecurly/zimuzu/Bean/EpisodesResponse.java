package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/12/16.
 */

public class EpisodesResponse {
    private String status;
    private String info;
    private List<Episode> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Episode> getData() {
        return data;
    }

    public void setData(List<Episode> data) {
        this.data = data;
    }
}
