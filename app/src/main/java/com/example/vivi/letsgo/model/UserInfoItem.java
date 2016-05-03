package com.example.vivi.letsgo.model;

/**
 * Created by vivi on 2016/4/12 0012.
 */
public class UserInfoItem {
    String name;
    String content;

    public UserInfoItem(String name,String content){
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
