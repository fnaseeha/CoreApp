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

public class MerchantSync {
    private MerchantSyncEvents delegate;

    public MerchantSync(MerchantSyncEvents delegate) {
        this.delegate = delegate;
    }

    public void setMerchants(String userId, final String merchantId, String merchantName, String address, String telephone, String city, String bankAccId, String bank, String nic,String bankAccName) {

        ServiceGenerator.CreateService(SyncDataServices.AddMerchant.class, Constants.BaseUrlTOCoreApp)
                .AddMerchantList(userId, merchantId, merchantName, address, telephone, city, bankAccId, bank, nic,bankAccName)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getData() != null) {

                                        delegate.getMerchantSyncSuccess(response.body().getStatus(), merchantId);


                                    } else {
                                        delegate.getMerchantSynceFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getMerchantSynceFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getMerchantSynceFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getMerchantSynceFaile(message);
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
                        delegate.getMerchantSynceFaile("Retrofit error");
                    }
                });

    }


    public interface MerchantSyncEvents {
        void getMerchantSyncSuccess(String update, String merchantId);

        void getMerchantSynceFaile(String message);
    }
}
