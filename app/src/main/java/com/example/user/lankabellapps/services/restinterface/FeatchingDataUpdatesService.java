package com.example.user.lankabellapps.services.restinterface;

import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Thejan on 5/20/2017.
 */

public class FeatchingDataUpdatesService {

    public interface LocationRegFeatchService {
        @GET("/Core.svc/LocationRegFetch")
        Call<GetCommonResponseModel> FeatchLocationsRegister(@Query("companyId") String userName,
                                                             @Query("pword") String pw);
    }
}
