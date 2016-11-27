package com.naturecurly.zimuzu.Bean;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class News {
    private String id;
    private String title;
    private String type;
    private String poster;
    private String dateline;
    private String resourceid;
    private String poster_a;

    public String getPoster_a() {
        return poster_a;
    }

    public void setPoster_a(String poster_a) {
        this.poster_a = poster_a;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
}
