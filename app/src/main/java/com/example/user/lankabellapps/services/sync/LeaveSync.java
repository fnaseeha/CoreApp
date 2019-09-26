package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Response;

public class LeaveSync {

    private GetLeaveEvents delegate;

    public LeaveSync(GetLeaveEvents delegate) {
        this.delegate = delegate;
    }


    public void setLeave(String user, String pw, String date,String Remark, String Leavetype,String Daytpe,String LeaveMarkDate) {

        ServiceGenerator.CreateService(MasterDataServices.Leave.class, Constants.BaseUrlTOCoreApp)
                .leave(user,pw,date,Remark,Leavetype,Daytpe,LeaveMarkDate)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getStatus() != null) {

//                                        JSONArray obj = new JSONArray(response.body().getData());
//                                        City city = new City();
//                                        city.clearTable();
//                                        saveDataToTheDatabase(obj);

                                        delegate.getLeaveSuccess(response.body().getData(),response.body().getStatus());
//                                AttendenceActivity atatvt = new AttendenceActivity();
//                               atatvt.getLeaveSucces(response.body().getData());



                                    } else {
                                        delegate.getLeaveFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getLeaveFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getLeaveFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getLeaveFaile(message);
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
                        delegate.getLeaveFaile("Retrofit error");
                    }
                });
    }

    public interface GetLeaveEvents {
        void getLeaveSuccess(String update,String status);
        void getLeaveFaile(String message);
    }




}