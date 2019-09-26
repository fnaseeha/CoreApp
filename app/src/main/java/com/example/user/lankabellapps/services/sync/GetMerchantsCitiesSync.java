package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by user on 2017-07-06.
 */

public class GetMerchantsCitiesSync {
    private GetCitiesEvents delegate;

    public GetMerchantsCitiesSync(GetCitiesEvents delegate) {
        this.delegate = delegate;
    }

    public void setAttendence(String userId) {

        ServiceGenerator.CreateService(MasterDataServices.GetCities.class, Constants.BaseUrlTOCoreApp)
                .getCitiesMerchants(userId)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getData() != null) {

                                        delegate.getCitiesSuccess(response.body().getData());


                                    } else {
                                        delegate.getCitiesFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getCitiesFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getCitiesFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getCitiesFaile(message);
                                //response.raw().body().close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                            }
                        }
                    }

                    private void saveDataToTheDatabase(JSONArray data) {


                        try {
                            // province.set

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        System.out.println(t);
                        delegate.getCitiesFaile("Retrofit error");
                    }
                });

    }


    public interface GetCitiesEvents {
        void getCitiesSuccess(String update);

        void getCitiesFaile(String message);
    }
}

