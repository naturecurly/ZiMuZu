package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class SearchResponse {
    private String status;
    private String info;

    public SearchResultSet getData() {
        return data;
    }

    public void setData(SearchResultSet data) {
        this.data = data;
    }

    private SearchResultSet data;

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
}
