package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.activities.LoginActivity;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateVersionSync {

    private UpdateVersionEvents delegate;

    public UpdateVersionSync(UpdateVersionEvents delegate) {
        this.delegate = delegate;
    }



    public void updateVersion(String epfNo,String appId,String appVersion){
        ServiceGenerator.CreateService(SyncDataServices.UpdateVersion.class, Constants.BaseUrlTOCoreApp)
                .updateVersion(epfNo,appId,appVersion)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {

                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        System.out.println("* url "+response.raw().request().url());

                        if(response.isSuccessful()){
                            if(response.body()!= null){
                                delegate.updateVersionSuccess(response.body());
                            }else{
                                delegate.updateVersionFailed("");
                            }
                        }else{
                            try {
                                String message = response.errorBody().toString();
                                delegate.updateVersionFailed(message);
                                //response.raw().body().close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        delegate.updateVersionFailed(t.getMessage());
                    }
                });

    }

    public interface UpdateVersionEvents{
        void updateVersionSuccess(GetCommonResponseModel update);
        void updateVersionFailed(String update);
    }
}
