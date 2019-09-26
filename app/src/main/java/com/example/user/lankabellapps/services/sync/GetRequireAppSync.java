package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

public class GetRequireAppSync {

    private GetRequireEvents context;

    public GetRequireAppSync(GetRequireEvents getRequireEvents){
        context = getRequireEvents;
    }

    public void getRequireApp(){
        ServiceGenerator.CreateServiceWithTimeout(SyncDataServices.GetRequiredVersionList.class,
                Constants.BaseUrlTOCoreApp)
                .getRequiredList() .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {

            @Override
            public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                System.out.println("* url "+response.raw().request().url());

                if(response.isSuccessful()){
                    if(response.body() != null){
                        context.getRequireAppSuccess(response.body());
                    }else{
                        context.getRequireAppError("");
                    }
                }else{
                    try{
                        String mes = response.errorBody().string();
                        context.getRequireAppError(mes);
                    }catch (Exception e){
                        e.printStackTrace();
                        context.getRequireAppError(e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                t.printStackTrace();
                context.getRequireAppError(t.getMessage());
            }
        });
    }
    public interface GetRequireEvents{
        void getRequireAppSuccess(GetCommonResponseModel response);
        void getRequireAppError(String message);

    }
}
