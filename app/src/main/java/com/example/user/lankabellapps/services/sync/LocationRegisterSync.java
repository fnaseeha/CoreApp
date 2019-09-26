package com.example.user.lankabellapps.services.sync;



import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 12/30/2016.
 */
public class LocationRegisterSync {
    private LocationRegisterEvents delegate;

    //private String cusId;


    public LocationRegisterSync(LocationRegisterEvents delegate) {
        this.delegate = delegate;
    }

    public void LocationRegister(String user, String pw , final String locationId , final String customerId, String collectId, String longi, String lati, String addedBy, String addedDate, String province, String district, String city, final String accountId, String status) {


        //this.paymentId = paymentId;

        ServiceGenerator.CreateService(SyncDataServices.LocationRegisterService.class, Constants.BaseUrlTOCoreApp)
                .LocationRegistration(user, pw, locationId ,customerId,  collectId,  longi,  lati,  addedBy,  addedDate,  province,  district,  city, accountId, status)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                    delegate.onLocationRegisterSuccess(response.body(), customerId, "");


                            } else {
                                delegate.onLocationRegisterFailed("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.onLocationRegisterFailed(message);
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
                        delegate.onLocationRegisterFailed("Retrofit error");
                    }
                });

    }


    public interface LocationRegisterEvents {
        void onLocationRegisterSuccess(GetCommonResponseModel update, String cusId, String accId);
        void onLocationRegisterFailed(String message);
    }
}


