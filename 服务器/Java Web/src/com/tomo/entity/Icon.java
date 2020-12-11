package com.tomo.entity;

public class Icon {
    private int iconid;
    private int userid;
    private String path;

    public void setIconid(int iconid) {
        this.iconid = iconid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIconid() {
        return iconid;
    }

    public int getUserid() {
        return userid;
    }

    public String getPath() {
        return path;
    }
}
