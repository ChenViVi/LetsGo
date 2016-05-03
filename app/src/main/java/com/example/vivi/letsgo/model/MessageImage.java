package com.example.vivi.letsgo.model;

/**
 * Created by vivi on 2016/4/25.
 */
public class MessageImage {
    private String name;
    private String data;

    public MessageImage(String name,String data){
        this.name = name;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
