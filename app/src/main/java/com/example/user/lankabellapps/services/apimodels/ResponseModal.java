package com.example.user.lankabellapps.services.apimodels;

import com.example.user.lankabellapps.models.datas;
import com.google.gson.annotations.SerializedName;

public class ResponseModal {

    @SerializedName("result")
    public String result;

    @SerializedName("data")
    public datas data;

    public ResponseModal(String result, datas data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public datas getData() {
        return data;
    }

    public void setData(datas data) {
        this.data = data;
    }
}
