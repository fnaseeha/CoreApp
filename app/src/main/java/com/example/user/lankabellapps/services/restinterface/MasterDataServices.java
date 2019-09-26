package com.example.user.lankabellapps.services.restinterface;

import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2017-03-31.
 */

public class MasterDataServices {


    public interface LoginService {
        @GET("/Core.svc/InitialLogin")
        Call<GetCommonResponseModel> checkLogin(@Query("companyId") String userName,
                                                @Query("pword") String pw,
                                                @Query("simNumber") String simNumber);
    }

    public interface Attendance {
        @GET("/Core.svc/Attendance")
        Call<GetCommonResponseModel> attendence(@Query("companyId") String userName,
                                                @Query("pword") String pw,
                                                @Query("attendanceDate") String accountCode,
                                                @Query("type") String type,
                                                @Query("simNumber") String simNumber,
                                                @Query("teamCode") String teamCode,
                                                @Query("meterValue") String meterValue,
                                                @Query("bikeCode") String bikeCode,
                                                @Query("category") String category);
    }



    public interface Leave {
        @GET("/Core.svc/TsrLeave")
        Call<GetCommonResponseModel> leave(@Query("companyId") String userName,
                                           @Query("pword") String pw,
                                           @Query("leavedate") String LeaveDate,
                                           @Query("remark") String Remark,
                                           @Query("LeaveType") String Leavetype,
                                           @Query("DayType") String Daytpe,
                                           @Query("applyDate") String LeaveMarkDate
        );
        //@Query("remark") String Remark );

    }


    public interface GetApplications {
        @GET("/Core.svc/UserDetails")
        Call<GetCommonResponseModel> getApps(@Query("epfNo") String epf);
    }


    public interface CheckMyAttendenc {
        @GET("/Core.svc/CheckTodayAttendance")
        Call<GetCommonResponseModel> getAttendence(@Query("companyId") String userName,
                                                   @Query("pword") String pw);
    }


    public interface GetCustomorsService {
        @GET("/Core.svc/ListCustomers")
        Call<GetCommonResponseModel> getCustomers(@Query("companyId") String userName,
                                                  @Query("pword") String pw);
    }


    public interface GetAccountsService {
        @GET("/Core.svc/ListAccounts")
        Call<GetCommonResponseModel> getCustomers(@Query("companyId") String userName,
                                                  @Query("pword") String pw);
    }

    public interface GetCities {
        @GET("/Core.svc/CityListMerchant")
        Call<GetCommonResponseModel> getCitiesMerchants(@Query("userId") String userName);
    }

    public interface TSRAccDetails {
        @GET("/Core.svc/TSRAccDetails")
        Call<GetCommonResponseModel> getTSRD(@Query("companyId") String userName,
                                             @Query("pword") String pw);
    }

    public interface GetMerchants {
        @GET("/Core.svc/FetchMerchantsAssigned")
        Call<GetCommonResponseModel> getMerchants(@Query("userId") String userName,
                                                  @Query("pword") String pw);
    }
    //http://10.12.14.143:4010/Core.svc/FetchGPSCount?epfNo=444
    public interface GetTotalGpsCount {
        @GET("/Core.svc/FetchGPSCount")
        Call<GetCommonResponseModel> getTotalGpsCount(@Query("epfNo") String userName);
    }


}
