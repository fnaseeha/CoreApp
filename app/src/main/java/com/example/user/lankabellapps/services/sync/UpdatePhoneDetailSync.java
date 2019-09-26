package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;
import retrofit2.Call;
import retrofit2.Response;

public class UpdatePhoneDetailSync {

    private UpdatePhoneDataEvents delegete;

    public UpdatePhoneDetailSync(UpdatePhoneDataEvents delegete) {
        this.delegete = delegete;
    }

    public void updatePhoneVersion(String epfNo,String phoneModal,String androidVersion){

        ServiceGenerator.CreateService(SyncDataServices.UpdatePhoneDetails.class, Constants.BaseUrlTOCoreApp)
                .UpdatePhoneDetails(epfNo,phoneModal,androidVersion)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>(){

                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if(response.isSuccessful()){
                            if(response.body()!= null){
                                delegete.UpdatePhoneDataSuccess(response.body());
                            }else{
                                delegete.UpdatePhoneDataError("");
                            }
                        }else{
                            try{
                                String message = response.errorBody().toString();
                                delegete.UpdatePhoneDataError(message);
                            }catch (Exception e){
                                e.printStackTrace();
                                delegete.UpdatePhoneDataError(e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        delegete.UpdatePhoneDataError(t.getMessage());
                    }
                });
    }

    public interface UpdatePhoneDataEvents{
        void UpdatePhoneDataSuccess(GetCommonResponseModel update);
        void UpdatePhoneDataError(String message);

    }
}
