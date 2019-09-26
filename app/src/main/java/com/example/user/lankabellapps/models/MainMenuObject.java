package com.example.user.lankabellapps.models;

/**
 * Created by user on 2017-03-27.
 */

public class MainMenuObject {

    public String itemKey;
    public String itemText;
    public int icon;
    public String iconUrl;
    public String packagename;
    public int updateAvailable;

    public MainMenuObject(String name, String id, int img, String packagename, int x, String iconUrl){
        this.itemKey = id;
        this.itemText = name;
        this.icon = img;
        this.packagename = packagename;
        this.updateAvailable = x;
        this.iconUrl = iconUrl;
    }

}
