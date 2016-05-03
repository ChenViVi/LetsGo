package com.example.vivi.letsgo.model;

/**
 * Created by vivi on 2016/4/3 0003.
 */
public class GroupMessageDiscuss {
    private String id;
    private String name;
    private String content;
    private String headimg;
    private String opttime;
    private String userid;
    private String touser;
    private String tousermc;

    public String getContent() {
        return content;
    }

    public String getHeadimg() {
        return headimg;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOpttime() {
        return opttime;
    }

    public String getTouser() {
        return touser;
    }

    public String getTousermc() {
        return tousermc;
    }

    public String getUserid() {
        return userid;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
