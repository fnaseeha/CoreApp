package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by user on 2017-07-10.
 */

public class GetMerchantsSync {
    private GetMerchantsList delegate;


    public GetMerchantsSync(GetMerchantsList delegate) {
        this.delegate = delegate;
    }

    public void getCustomerList(String user, String pw) {


        ServiceGenerator.CreateService(MasterDataServices.GetMerchants.class, Constants.BaseUrlTOCoreApp)
                .getMerchants(user, pw)
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

                                        delegate.getMerchantsListSuccess(response.body());
                                        //saveDataToTheDatabase();
                                    } else {
                                        delegate.getMerchantsListFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getMerchantsListFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getMerchantsListFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getMerchantsListFaile(message);
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
                        delegate.getMerchantsListFaile("Retrofit error");
                    }
                });

    }


    public interface GetMerchantsList {
        void getMerchantsListSuccess(GetCommonResponseModel update);

        void getMerchantsListFaile(String message);


    }
}


