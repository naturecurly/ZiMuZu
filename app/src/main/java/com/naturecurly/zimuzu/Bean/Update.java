package com.naturecurly.zimuzu.Bean;

import com.google.gson.JsonObject;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class Update {
    private String id;
    private String resourceid;
    private String name;
    private String format;
    private String season;
    private String episode;
    private String size;
    private String cnname;
    private String channel;
    private JsonObject ways;
    private String link;


    public Update(String id, String resourceid, String name, String format, String season, String episode, String size, String cnname, String channel, String link) {
        this.id = id;
        this.resourceid = resourceid;
        this.name = name;
        this.format = format;
        this.season = season;
        this.episode = episode;
        this.size = size;
        this.cnname = cnname;
        this.channel = channel;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public JsonObject getWays() {
        return ways;
    }

    public void setWays(JsonObject ways) {
        this.ways = ways;
    }
}
