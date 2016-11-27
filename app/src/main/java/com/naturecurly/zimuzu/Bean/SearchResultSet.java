package com.naturecurly.zimuzu.Bean;

import java.util.List;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class SearchResultSet {
    private String count;
    private List<SearchResult> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<SearchResult> getList() {
        return list;
    }

    public void setList(List<SearchResult> list) {
        this.list = list;
    }
}
