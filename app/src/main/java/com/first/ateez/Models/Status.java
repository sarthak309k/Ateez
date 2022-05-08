package com.first.ateez.Models;

public class Status {
    private String imgurl;
    private long timestapm;

    public Status() {
    }

    public Status(String imgurl, long timestapm) {
        this.imgurl = imgurl;
        this.timestapm = timestapm;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public long getTimestapm() {
        return timestapm;
    }

    public void setTimestapm(long timestapm) {
        this.timestapm = timestapm;
    }
}
