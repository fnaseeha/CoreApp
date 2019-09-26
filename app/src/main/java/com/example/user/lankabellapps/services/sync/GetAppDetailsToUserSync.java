package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by user on 2017-04-18.
 */

public class GetAppDetailsToUserSync {

    private GetAppilcationEvents delegate;


    public GetAppDetailsToUserSync(GetAppilcationEvents delegate) {
        this.delegate = delegate;
    }

    public void getApplications(String epf) {

        ServiceGenerator.CreateService(MasterDataServices.GetApplications.class,
                Constants.BaseUrlTOCoreApp)
                .getApps(epf)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        System.out.println("* url "+response.raw().request().url());
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                delegate.onSuccess(response.body());

                            } else {
                                delegate.onFailed("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.onFailed(message);
                                //response.raw().body().close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        delegate.onFailed("Retrofit error");
                    }
                });

    }


    public interface GetAppilcationEvents {
        void onSuccess(GetCommonResponseModel update);

        void onFailed(String message);
    }

}
