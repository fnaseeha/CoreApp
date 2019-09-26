package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thejan on 2017-01-06.
 */
public class CheckMyAttendneceSync {


    private GetAttendenceEvents delegate;
    String ServerTime;

    public CheckMyAttendneceSync(GetAttendenceEvents delegate) {
        this.delegate = delegate;
    }

    public void GetAttendenceData(String user, String pw) {


        ServiceGenerator.CreateService(MasterDataServices.CheckMyAttendenc.class, Constants.BaseUrlTOCoreApp)
                .getAttendence(user, pw)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                System.out.println(response.code());

                                try {

                                    if (response.body().getData() != null) {
                                        ServerTime= response.body().getServerTime();//server time
                                       // delegate.getAttendenceSyncSuccess(response.body().getData());
                                        saveDataToTheDatabase(response.body().getData());

                                    } else {
                                        delegate.getAttendenceFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getAttendenceFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getAttendenceFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getAttendenceFaile(message);
                          //response.raw().body().close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                // delegate.onUpdateFailer(SmartConstants.TRY_AGAIN_EXCEPTION);
                            }
                        }
                    }

                    private void saveDataToTheDatabase(String data) {
                        try {
                            JSONArray attendenceJsonarray = new JSONArray(data);

                            if (attendenceJsonarray.length() > 0) {


                                if (attendenceJsonarray.length() > 1) {
                                    JSONObject row1 = attendenceJsonarray.getJSONObject(0);
                                    JSONObject row2 = attendenceJsonarray.getJSONObject(1);

                                    Attendence attendence = new Attendence();
                                    attendence.setAttendenceIn(TimeFormatter.changeTimeFormat("yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", row1.getString("Attendance")));
                                    attendence.setAttendenceOut(TimeFormatter.changeTimeFormat("yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", row2.getString("Attendance")));
                                    attendence.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                                    attendence.setSyncTimeIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"));
                                    attendence.setSyncTimeOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"));
                                    attendence.save();

                                } else {
                                    JSONObject row1 = attendenceJsonarray.getJSONObject(0);

                                    Attendence attendence = new Attendence();
                                    attendence.setAttendenceIn(TimeFormatter.changeTimeFormat("yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", row1.getString("Attendance")));
                                    attendence.setAttendenceOut("");
                                    attendence.setSyncTimeOut("");
                                    attendence.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                                    attendence.setSyncTimeIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"));
                                    attendence.save();
                                }

                            }

                            delegate.getAttendenceSuccess("Success",ServerTime);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        System.out.println(t);
                        delegate.getAttendenceFaile("Retrofit error");
                    }
                });

    }


    public interface GetAttendenceEvents {
        void getAttendenceSuccess(String update,String Servertime);

        void getAttendenceFaile(String message);

    }
}

