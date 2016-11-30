package com.naturecurly.zimuzu.Bean;

/**
 * Created by leveyleonhardt on 11/30/16.
 */

public class Aria2Response {
    private String id;
    private String jsonrpc;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
