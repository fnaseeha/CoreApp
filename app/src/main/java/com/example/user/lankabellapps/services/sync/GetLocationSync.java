package com.example.user.lankabellapps.services.sync;

import android.content.Context;

import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.ResponseModal;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;

import retrofit2.Call;
import retrofit2.Response;

public class GetLocationSync {

    private GetLocationEvents context;


    public GetLocationSync(GetLocationEvents context1) {
        this.context = context1;
    }
    public void getLocationsSync(String v, String data, String mcc, String mnc, int lac, int cellid){

        ServiceGenerator.CreateServiceWithTimeout(SyncDataServices.GetLoca.class,"api.mylnikov.org")
                .GetLocaResponse(v,data,mcc,mnc,lac,cellid)
                .enqueue(new retrofit2.Callback<ResponseModal>(){

                    @Override
                    public void onResponse(Call<ResponseModal> call, Response<ResponseModal> response) {
                        System.out.println("* url "+response.raw().request().url());
                        try {
                          if(response.body()!= null) {
                              if (response.body().getData() != null) {
                                  context.getLocationsSuccess(response.body());
                              } else {
                                  context.getLocationsError("");
                              }
                           }else{
                              context.getLocationsError(response.errorBody().toString());
                          }
                            //response.raw().body().close();
                        }catch (Exception e){
                            context.getLocationsError(response.errorBody().toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModal> call, Throwable t) {
                        context.getLocationsError(t.getMessage());
                        t.printStackTrace();
                    }
                });

    }

    public interface GetLocationEvents{
        void getLocationsSuccess(ResponseModal response);
        void getLocationsError(String message);

    }
}
