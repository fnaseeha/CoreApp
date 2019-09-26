package com.example.user.lankabellapps.models;

/**
 * Created by user on 2017-04-05.
 */

public class DownloadItemModel {

    public String itemKey;
    public String itemText;
    public int icon;
    public String packagename;
    public String url;

    public DownloadItemModel(String name, String id, int img, String packagename, String url){
        this.itemKey = id;
        this.itemText = name;
        this.icon = img;
        this.packagename = packagename;
        this.url = url;
    }

}