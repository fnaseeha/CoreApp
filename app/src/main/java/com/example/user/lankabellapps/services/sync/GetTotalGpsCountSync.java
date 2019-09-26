package com.example.user.lankabellapps.services.sync;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import retrofit2.Call;
import retrofit2.Response;

public class GetTotalGpsCountSync {

    private GetTotalGps delegate;

    public GetTotalGpsCountSync(GetTotalGps delegate) {
        this.delegate = delegate;
    }

    public void getTotalGps(String user){

        ServiceGenerator.CreateService(MasterDataServices.GetTotalGpsCount.class, Constants.BaseUrlTOCoreApp)
                .getTotalGpsCount(user)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>(){

                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        System.out.println("* url "+response.raw().request().url());
                        if(response.body() != null){
                            try{
                                if(response.body().getData() != null){
                                    delegate.getTotalGpsSuccess(response.body());
                                }else{
                                    delegate.getTotalGpsFailed("");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                delegate.getTotalGpsFailed(e.getMessage());
                            }
                        }else{
                            delegate.getTotalGpsFailed("");
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        delegate.getTotalGpsFailed(t.getMessage());
                    }
                } );
    }

    public interface GetTotalGps{
        void getTotalGpsSuccess(GetCommonResponseModel update);
        void getTotalGpsFailed(String message);

    }
}
