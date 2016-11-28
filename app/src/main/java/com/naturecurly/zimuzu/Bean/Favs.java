package com.naturecurly.zimuzu.Bean;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class Favs {
    private String id;
    private String cnname;
    private String enname;
    private String poster;

    public Favs(String id, String cnname, String enname, String poster) {
        this.id = id;
        this.cnname = cnname;
        this.enname = enname;
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
