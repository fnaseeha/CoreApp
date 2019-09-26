package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;
import com.google.gson.Gson;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 2016-12-20.
 */
public class AttendenctSync {

    private GetAttendenceEvents delegate;

    public AttendenctSync(GetAttendenceEvents delegate) {
        this.delegate = delegate;
    }

    public void setAttendence(String user, String pw, final String date, final String type, String simNo,
                              String teamCode, String meterValue, String bikeCode, String category) {

        ServiceGenerator.CreateService(MasterDataServices.Attendance.class, Constants.BaseUrlTOCoreApp)
                .attendence(user, pw , date , type, simNo, teamCode, meterValue, bikeCode, category)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {

                        System.out.println("* url "+response.raw().request().url());

                    ///   ResponseBody responseBody = response.body();

                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getStatus() != null) {
                                        System.out.println("* response body "+response.body().getData());
                                       // System.out.println(new Gson().toJson(response.body()));
                                       // Constants.ATTENDANCE_STATUS_OUT="OK";
//                                        JSONArray obj = new JSONArray(response.body().getData());
//                                        City city = new City();
//                                        city.clearTable();
//                                        saveDataToTheDatabase(obj);

                                        //if(response.body().getStatus()){
                                            delegate.getAttendenceSyncFail("", type);

                                            if(type.equalsIgnoreCase("IN")){
                                                Constants.ATTENDANCE_STATUS_IN="OK";
                                            }
                                            if(type.equalsIgnoreCase("OUT")){
                                                Constants.ATTENDANCE_STATUS_OUT="OK";
                                            }
                                            updateSyncId(date,type,"1");
                                            delegate.getAttendenceSyncSuccess(response.body().getData(), type);


                                    } else {

                                        updateSyncId(date,type,"0");
                                        setContantsError(type);
                                        delegate.getAttendenceSyncFail("", type);
                                    }

                                } catch (Throwable t) {
                                    updateSyncId(date,type,"0");
                                    setContantsError(type);
                                    delegate.getAttendenceSyncFail("", type);
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                setContantsError(type);
                                updateSyncId(date,type,"0");
                                delegate.getAttendenceSyncFail("", type);
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                updateSyncId(date,type,"0");
                                setContantsError(type);
                                delegate.getAttendenceSyncFail(message, type);
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
                        delegate.getAttendenceSyncFail("Retrofit error", type);
                    }
                });

    }

    private void setContantsError(String type){
        if(type.equalsIgnoreCase("IN")){
            Constants.ATTENDANCE_STATUS_IN="NO";
        }
        if(type.equalsIgnoreCase("OUT")){
            Constants.ATTENDANCE_STATUS_OUT="NO";
        }
    }
    private void updateSyncId(String date, String type,String id) {

        if(type.equalsIgnoreCase("IN")){
            Attendence attendence1 = new Attendence();
            System.out.println("* getAttendenceDate in "+attendence1.getAttendenceDate());
            attendence1.updateSyncIn(date,id);
        }

        if(type.equalsIgnoreCase("OUT")){
            Attendence attendence1 = new Attendence();
            System.out.println("* getAttendenceDate out "+attendence1.getAttendenceDate());
            attendence1.updateSyncOut(date,id);
        }
    }


    public interface GetAttendenceEvents {
        void getAttendenceSyncSuccess(String update, String type);
        void getAttendenceSyncFail(String message, String type);
    }
}
