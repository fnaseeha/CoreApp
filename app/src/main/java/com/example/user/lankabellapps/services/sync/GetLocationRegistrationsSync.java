package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.FeatchingDataUpdatesService;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 1/2/2017.
 */
public class GetLocationRegistrationsSync {

    private GetLocationRegistrationFeatchEvents delegate;


    public GetLocationRegistrationsSync(GetLocationRegistrationFeatchEvents delegate) {
        this.delegate = delegate;
    }

    public void getLocationRegistration(String user, String pw) {


        ServiceGenerator.CreateService(FeatchingDataUpdatesService.LocationRegFeatchService.class, Constants.BaseUrlTOCoreApp)
                .FeatchLocationsRegister(user, pw)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getData() != null) {

//                                        JSONArray obj = new JSONArray(response.body().getData());
//                                        City city = new City();
//                                        city.clearTable();
//                                        saveDataToTheDatabase(obj);

                                        delegate.getLocationRegistrationFeatchSuccess(response.body().getData());
                                    } else {
                                        delegate.getLocationRegistrationFeatchFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getLocationRegistrationFeatchFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getLocationRegistrationFeatchFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getLocationRegistrationFeatchFaile(message);
                                //response.raw().body().close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                            }
                        }
                    }

                    private void saveDataToTheDatabase(JSONArray data) {

//                        JSONArray names = data.names();
//                        JSONArray values = data.toJSONArray(names);



//                        for(int i = 0; i<data.length(); i++){
//
//                            try {
//                                JSONObject row = data.getJSONObject(i);
//
//                                City city = new City();
//
//                                city.setProvinceId(row.getString("ProvinceCode"));
//                                city.setDistrictId(row.getString("DiscrictCode"));
//                                city.setCityName(row.getString("Description"));
//
//                                city.save();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }

                        try {
                            // province.set

                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        System.out.println(t);
                        delegate.getLocationRegistrationFeatchFaile("Retrofit error");
                    }
                });

    }


    public interface GetLocationRegistrationFeatchEvents {
        void getLocationRegistrationFeatchSuccess(String update);

        void getLocationRegistrationFeatchFaile(String message);


    }
}



