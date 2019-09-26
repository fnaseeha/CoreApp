package com.example.user.lankabellapps.services.backgroundservices;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class GetHandInUnit {

    private   GetHandEvents delegate;

    public  GetHandInUnit(GetHandEvents delegate){
        this.delegate = delegate;
    }

    public void GetUnits(String user){
        try{
            ServiceGenerator.CreateServiceWithTimeout(SyncDataServices.GetInHandUnitList.class, Constants.BaseUrlTOCoreApp)
                    .HandUnit(user)
                    .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                        @Override
                        public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {


                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    delegate.GetHandSuccess(response.body());

                                } else {
                                    delegate.GetHandError("");
                                }
                            } else {
                                try {
                                    String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                    delegate.GetHandError(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                     delegate.GetHandError(e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {

                            t.printStackTrace();
                            delegate.GetHandError(call.toString());
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public interface GetHandEvents{
        void GetHandSuccess(GetCommonResponseModel update);
        void GetHandError(String message);
    }
}


