package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class NewsResponse {
    private String status;
    private String info;
    private List<News> data;

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

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
