package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class UpdateResponse {
    private String status;
    private String info;
    private List<Update> data;

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

    public List<Update> getData() {
        return data;
    }

    public void setData(List<Update> data) {
        this.data = data;
    }
}
