package com.example.user.lankabellapps.services.sync;


import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 12/31/2016.
 */
public class TrackingSync {

    private TrackingEvents delegate;

    //private String cusId;

    public TrackingSync(TrackingEvents delegate) {
        this.delegate = delegate;
    }

    public void Trackings(String user, String pw , final String addedTime, String longt, String latt, String accuracy, String speed, String useTime ) {

        //this.paymentId = paymentId;
        try {

            ServiceGenerator.CreateServiceWithTimeout(SyncDataServices.TrackingSendService.class, Constants.BaseUrlTOCoreApp)
                    .Tracking(user, pw, addedTime, longt, latt, accuracy, speed, useTime)
                    .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                        @Override
                        public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                            System.out.println("* url "+response.raw().request().url());
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    delegate.onTrackingSuccess(response.body(), addedTime);

                                } else {
                                    delegate.onTrackingFailed("");
                                }
                            } else {
                                try {
                                    String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                    delegate.onTrackingFailed(message);
                                    //response.raw().body().close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                            System.out.println("* tracking error");
                            System.out.println(t);
                            delegate.onTrackingFailed("Retrofit error");
                        }
                    });
        }catch (Exception e){
            System.out.println("* tracking error");
            System.out.println(e);
            delegate.onTrackingFailed("Retrofit error");
        }
    }


    public interface TrackingEvents {
        void onTrackingSuccess(GetCommonResponseModel update, String addedTime);
        void onTrackingFailed(String message);
    }
}

