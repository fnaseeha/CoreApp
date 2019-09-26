package com.example.user.lankabellapps.services.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Thejan on 2016-11-30.
 */
public class GetCommonResponseModel {

    @SerializedName("Data")
    @Expose
    private String data;

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("ServerTime")
    @Expose
    private String ServerTime;

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
