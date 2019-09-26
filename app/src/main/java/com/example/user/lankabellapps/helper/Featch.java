package com.example.user.lankabellapps.helper;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.sync.GetAppDetailsToUserSync;
import com.example.user.lankabellapps.services.sync.LoginSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 4/18/2017.
 */
public class Featch implements GetAppDetailsToUserSync.GetAppilcationEvents {

    int trackingCount;
    UserProfile userProfile;
    List<UserProfile> userProfileList;


    public void commonData() {
        userProfile = new UserProfile();
        userProfileList = new ArrayList<>();
        userProfileList = userProfile.getAllUsers();

    }

    private featchDone delegate;


    public Featch(featchDone delegate) {
        this.delegate = delegate;
    }


    public void featchAppDetails() {

        commonData();

        GetAppDetailsToUserSync getAppDetailsToUserSync = new GetAppDetailsToUserSync(this);
        getAppDetailsToUserSync.getApplications(userProfileList.get(0).getUserName()); //should turn to the epf from user details
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onSuccess(GetCommonResponseModel update) {

        if(update != null){
            if (!update.getData().equals(Constants.LOGIN_SUCCESSFUL)) {
                try {
                    JSONArray obj = new JSONArray(update.getData());

                    new AvailableApps().clearTable();

                    for (int i = 0; i < obj.length(); i++) {

                        try {
                            JSONObject row = obj.getJSONObject(i);

                            AvailableApps availableApps = new AvailableApps();

                            availableApps.setDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(),"dd/MM/yyyy"));
                            availableApps.setVersion(row.getString("Version"));
                            availableApps.setUrl(row.getString("Download_url"));
                            availableApps.setPackagename(row.getString("Pkg_name"));

                            if(row.getString("App_id").equals("5")) {
                                availableApps.setIconName("collector_app");
                            }else if(row.getString("App_id").equals("4")){
                                availableApps.setIconName("tsr");
                            }else if(row.getString("App_id").equals("6")){
                                availableApps.setIconName("sales_icon");
                            }else{
                                availableApps.setIconName("common_icon");
                            }
                            availableApps.setAppId(row.getString("App_id"));
                            availableApps.setAppName(row.getString("App_name"));
                            availableApps.setSysSync(row.getString("Sys_sync"));
                            //availableApps.setIconUrl("https://lh5.ggpht.com/ly6G6lUNHjWKvLgeHO0-ilg7zkEXc-hCWP0Q94gdyCeejWNoDMw6h5buM0pFO0mSDKQ=w300");
                            availableApps.setIconUrl(row.getString("Icon_url"));
                            availableApps.setUpdateAvailable(0);

                            availableApps.save();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    delegate.onSuccess();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }




    }


    @Override
    public void onFailed(String message) {
      delegate.onFailed();
    }


    public interface featchDone {
        void onSuccess();
        void onFailed();

    }
}
