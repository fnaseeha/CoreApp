package com.example.user.lankabellapps.services.restinterface;

import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.apimodels.ResponseModal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Thejan on 2017-04-17.
 */

public class SyncDataServices {


    public interface TrackingSendService {
        @GET("/Core.svc/Tracking")
        Call<GetCommonResponseModel> Tracking(@Query("companyId") String userName,
                                              @Query("pword") String pw,
                                              @Query("addedtime") String addedTime,
                                              @Query("longt") String longt,
                                              @Query("latt") String latt,
                                              @Query("accuracy") String accuracy,
                                              @Query("speed") String speed,
                                              @Query("usetime") String usetime);
    }
    public interface GetInHandUnitList {
        @GET("/Core.svc/GetInHandUnitList")
        Call<GetCommonResponseModel> HandUnit(@Query("epfNo") String userName);

    }



    public interface LocationRegisterService {
        @GET("/Core.svc/LocationRegistration")
        Call<GetCommonResponseModel> LocationRegistration(@Query("companyId") String userName,
                                                          @Query("pword") String pw,
                                                          @Query("LocationId") String LocationId,
                                                          @Query("CustomerId") String CustomerId,
                                                          @Query("CollectorId") String CollectorId,
                                                          @Query("Longtitude") String Longtitude,
                                                          @Query("Latitude") String Latitude,
                                                          @Query("AddedBy") String AddedBy,
                                                          @Query("AddedDate") String AddedDate,
                                                          @Query("Province") String Province,
                                                          @Query("District") String District,
                                                          @Query("City") String City,
                                                          @Query("AccountId") String AccountId,
                                                          @Query("Status") String Status);
    }


    public interface AddMerchant {
        @GET("/Core.svc/AddMerchant")
        Call<GetCommonResponseModel> AddMerchantList(@Query("userId") String userId,
                                                     @Query("merchantId") String merchantId,
                                                     @Query("merchantName") String merchantName,
                                                     @Query("address") String address,
                                                     @Query("telephone") String telephone,
                                                     @Query("city") String city,
                                                     @Query("bankAccId") String bankAccId,
                                                     @Query("bank") String bank,
                                                     @Query("nic") String nic,
                                                     @Query("bankAccName") String bankAccName);
    }

    public interface UpdateMerchants {
        @GET("/Core.svc/EditMerchant")
        Call<GetCommonResponseModel> updateMerchants(@Query("userId") String userId,
                                                     @Query("merchantId") String merchantId,
                                                     @Query("merchantName") String merchantName,
                                                     @Query("address") String address,
                                                     @Query("telephone") String telephone,
                                                     @Query("city") String city,
                                                     @Query("bankAccId") String bankAccId,
                                                     @Query("bank") String bank,
                                                     @Query("nic") String nic,
                                                     @Query("bankAccName") String bankAccName);
    }

    public interface UpdateVisitLog {
        @GET("/Core.svc/CustVisitLogAdd")
        Call<GetCommonResponseModel> custVisitLogAdd(@Query("userId") String userId,
                                                     @Query("customerId") String customerId,
                                                     @Query("custName") String custName,
                                                     @Query("contactNo") String contactNo,
                                                     @Query("remarks") String remarks,
                                                     @Query("addedDate") String addedDate,
                                                     @Query("Longtitude") String longi,
                                                     @Query("Latitude") String lati,
                                                     @Query("address")String address,
                                                     @Query("custId")String custId);
    }
    public interface UpdateVersion {
        @GET("/Core.svc/UpdateAppVersion")
        Call<GetCommonResponseModel> updateVersion(@Query("epfNo") String userId,
                                                     @Query("appId") String appId,
                                                     @Query("appVersion") String appVersion);
    }
    //https://api.mylnikov.org/geolocation/cell?v=1.1&data=open&mcc=268&mnc=06&lac=8280&cellid=5616

    public interface GetLoca{
        @GET("/geolocation/cell")
        Call<ResponseModal>GetLocaResponse(@Query("v") String v ,
                                           @Query("data") String data,
                                           @Query("mcc") String mcc,
                                           @Query("mnc") String mnc,
                                           @Query("lac") int lac,
                                           @Query("cellid") int cellid);
    }
    public interface GetRequiredVersionList{
        @GET("/Core.svc/GetRequiredAppList")
        Call<GetCommonResponseModel>getRequiredList();
    }

    public interface UpdatePhoneDetails{
        @GET("/Core.svc/UpdatePhoneDetails")
        Call<GetCommonResponseModel>UpdatePhoneDetails(@Query("epfNo") String userId ,
                                                    @Query("deviceName") String deviceName,
                                                    @Query("androidVersion") String androidVersion);
    }


}
