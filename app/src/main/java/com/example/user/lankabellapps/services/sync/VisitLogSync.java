package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by user on 2017-09-27.
 */

public class VisitLogSync {

    private VisitSyncEvents delegate;

    //private String cusId;

    public VisitLogSync(VisitSyncEvents delegate) {
        this.delegate = delegate;
    }

    public void Visits(String user, String pw, String cusName, String contactNo, String remarks, final String addedDate, String lati, String longi,String address,String custId) {

        //this.paymentId = paymentId;
        try {

            ServiceGenerator.CreateServiceWithTimeout(SyncDataServices.UpdateVisitLog.class, Constants.BaseUrlTOCoreApp)
                    .custVisitLogAdd(user, pw, cusName, contactNo, remarks, addedDate, longi, lati,address,custId)
                    .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                        @Override
                        public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    delegate.onVisitSuccess(response.body(), addedDate);

                                } else {
                                    delegate.onVisitFailed("");
                                }
                            } else {
                                try {
                                    String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                    delegate.onVisitFailed(message);
                                    //response.raw().body().close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                            System.out.println(t);
                            delegate.onVisitFailed("Retrofit error");
                        }
                    });
        }catch (Exception e){
            System.out.println(e);
            delegate.onVisitFailed("Retrofit error");
        }
    }


    public interface VisitSyncEvents {
        void onVisitSuccess(GetCommonResponseModel update, String addedDate);
        void onVisitFailed(String message);
    }
}


