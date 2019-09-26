package com.example.user.lankabellapps.models;

public class datas {
    double lat;
    double range;
    double lon;
    double time;

    public datas(double lat, double range, double lon, double time) {
        this.lat = lat;
        this.range = range;
        this.lon = lon;
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
