package com.naturecurly.zimuzu.Bean;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class SearchResult {
    private String itemid;
    private String title;
    private String type;
    private String channel;
    private String pubtime;
    private String uptime;

    public String getItemId() {
        return itemid;
    }

    public void setItemId(String itemId) {
        this.itemid = itemId;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
