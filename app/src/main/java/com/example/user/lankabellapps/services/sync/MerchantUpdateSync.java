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
 * Created by user on 2017-07-11.
 */

public class MerchantUpdateSync {
    private updateMerchantsList delegate;


    public MerchantUpdateSync(updateMerchantsList delegate) {
        this.delegate = delegate;
    }

    public void updateMerchants(String userId, final String merchantId, String merchantName, String address, String telephone, String city, String bankAccId, String bank, String nic,String bankAccName) {


        ServiceGenerator.CreateService(SyncDataServices.UpdateMerchants.class, Constants.BaseUrlTOCoreApp)
                .updateMerchants(userId, merchantId, merchantName, address, telephone, city, bankAccId, bank, nic,bankAccName)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getData() != null) {

//                                        JSONArray obj = new JSONArray(response.body().getData());
//                                        Customers customers = new Customers();
//                                        customers.clearTable();
//                                        saveDataToTheDatabase(obj);

                                        delegate.updateMerchantsListSuccess(response.body().getStatus(), merchantId);
                                        //saveDataToTheDatabase();
                                    } else {
                                        delegate.updateerchantsListFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.updateerchantsListFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.updateerchantsListFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.updateerchantsListFaile(message);
                                //response.raw().body().close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                            }
                        }
                    }

                    private void saveDataToTheDatabase(JSONArray data) {


                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        System.out.println(t);
                        delegate.updateerchantsListFaile("Retrofit error");
                    }
                });

    }


    public interface updateMerchantsList {
        void updateMerchantsListSuccess(String update, String merchantId);

        void updateerchantsListFaile(String message);


    }
}



