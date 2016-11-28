package com.naturecurly.zimuzu.Bean;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class NewsDetailResponse {
    private String status;
    private String info;
    private NewsDetail data;

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

    public NewsDetail getData() {
        return data;
    }

    public void setData(NewsDetail data) {
        this.data = data;
    }
}
