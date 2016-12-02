package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public class TopResponse {
    private String status;
    private String info;
    private List<Series> data;

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

    public List<Series> getData() {
        return data;
    }

    public void setData(List<Series> data) {
        this.data = data;
    }
}
