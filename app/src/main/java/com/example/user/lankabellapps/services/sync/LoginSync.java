package com.example.user.lankabellapps.services.sync;


import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;
import com.google.gson.Gson;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 2016-10-28.
 */

/**
 * Loginc Web service sync using this classs.
 */
public class LoginSync {
    private LoginSyncEvetns delegate;


    public LoginSync(LoginSyncEvetns delegate) {
        this.delegate = delegate;
    }

    public void login(String user, String pw, final String simNo) {

        ServiceGenerator.CreateService(MasterDataServices.LoginService.class, Constants.BaseUrlTOCoreApp)
                .checkLogin(user, pw, simNo)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {


                        System.out.println("* url " + response.raw().request().url());

                            if (response.isSuccessful()) {

                                if (response.body() != null) {
                                    delegate.onLoginSuccess(response.body());

                                } else {
                                    delegate.onLoginFailed(response.body().getData(), response.body().getId());
                                }
                            } else {

                                try {
                                    String message = response.errorBody().string();

//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                    delegate.onLoginFailed(message, response.body().getId());
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
                        if(t.getMessage() == null){
                            delegate.onLoginFailed(t.getMessage(), "");
                        }else{
                            if(  t.getMessage().contains("IllegalStateException")){
                                delegate.onLoginFailed("Please check your internet connection ", "");
                            }else{

                                delegate.onLoginFailed(t.getMessage(), "");
                            }
                        }

                    }
                });

    }


    public interface LoginSyncEvetns {
        void onLoginSuccess(GetCommonResponseModel update);
        void onLoginFailed(String message, String id);
    }
}
