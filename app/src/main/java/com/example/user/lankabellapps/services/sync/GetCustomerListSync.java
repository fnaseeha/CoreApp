package com.example.user.lankabellapps.services.sync;

import com.activeandroid.util.Log;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.services.ServiceGenerator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.restinterface.MasterDataServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thejanthrimanna on 2016-12-06.
 */
public class GetCustomerListSync {
    private GetCustomerListEvents delegate;


    public GetCustomerListSync(GetCustomerListEvents delegate) {
        this.delegate = delegate;
    }

    public void getCustomerList(String user, String pw) {


        ServiceGenerator.CreateService(MasterDataServices.GetCustomorsService.class, Constants.BaseUrlTOCoreApp)
                .getCustomers(user, pw)
                .enqueue(new retrofit2.Callback<GetCommonResponseModel>() {
                    @Override
                    public void onResponse(Call<GetCommonResponseModel> call, Response<GetCommonResponseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //delegate.getCollectroSuccess(response.body());
                                try {

                                    if (response.body().getData() != null) {

//                                        JSONArray obj = new JSONArray(response.body().getData());
//                                        Customers customers = new Customers();
//                                        customers.clearTable();
//                                        saveDataToTheDatabase(obj);

                                        delegate.getCustoimerListSuccess(response.body());
                                        //saveDataToTheDatabase();
                                    } else {
                                        delegate.getCustomerListFaile("");
                                    }

                                } catch (Throwable t) {
                                    delegate.getCustomerListFaile("");
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response.body().getData() + "\"");
                                }
                            } else {
                                delegate.getCustomerListFaile("");
                            }
                        } else {
                            try {
                                String message = response.errorBody().string();
//                                Log.d("SmartFm - ErrorLog", message);
//                                ErrorLog.saveErrorLog("Update Booking Time", message);
                                delegate.getCustomerListFaile(message);
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



                        for(int i = 0; i<data.length(); i++){

                            try {
                                JSONObject row = data.getJSONObject(i);

                                Customers customers = new Customers();

                                customers.setCompanyCode(row.getString("Id_number"));
                                customers.setNamem(row.getString("Profile_name"));
                                customers.setTitle(row.getString("Title"));
                                customers.setAddress_line_1(row.getString("Address_line_1"));
                                customers.setAddress_line_2(row.getString("Address_line_2"));
                                customers.setAddress_line_3(row.getString("Address_line_3"));
                                customers.setCity(row.getString("City"));
                                customers.setPostal_code(row.getString("Postal_code"));
                                //customers.setModified_by(row.getString("Modified_on"));
                                customers.setStatus(row.getString("Profile_status"));
                                customers.setD_first_name(row.getString("D_first_name"));
                                customers.setD_last_name(row.getString("D_last_name"));
                                customers.setDistrict(row.getString("District"));
                                customers.setProvince(row.getString("Province"));
                                customers.setContactNo(row.getString("Contact_phone_no"));
                                customers.setRegister("false");
                                customers.setLocStatus("");
                                customers.setLati(0.0);
                                customers.setLongi(0.0);
//                                Random r = new Random();
//                                int i1 = r.nextInt(20000 - 10000) + 1250;
                                customers.setOs(row.getString("Account_outstanding"));

                                customers.save();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        try {
                            // province.set

                        }catch (Exception e){

                        }
                        // CollectorProfile collectorProfile = new CollectorProfile();
//                        try {
////                            collectorProfile.setEpfNo(data.getInt("EpfNo"));
////                            collectorProfile.setNextMerchantNo(data.getInt("NextMerchantNo"));
////                            collectorProfile.setNextInvoiceNo(data.getInt("NextInvoiceNo"));
////                            collectorProfile.setStockResetDate(data.getString("StockResetDate"));
////                            collectorProfile.setMaxRecords(data.getInt("MaxRecords"));
////                            collectorProfile.setLastCashierSyn(data.getString("LastCashierSyn"));
////                            //collectorProfile.setSystemUrl(data.getString("SystemUrl"));
////                            collectorProfile.setSystemAutoSynch(data.getInt("SystemAutoSynch"));
////                            collectorProfile.setGpsGetting(data.getInt("GpsGetting"));
////                            collectorProfile.setGpsAutoSynch(data.getInt("GpsAutoSynch"));
////                            //collectorProfile.setLastSynchDoneDate(data.getString("LastSynchDoneDate"));
////
////                            collectorProfile.save();
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommonResponseModel> call, Throwable t) {
                        System.out.println(t);
                        delegate.getCustomerListFaile("Retrofit error");
                    }
                });

    }


    public interface GetCustomerListEvents {
        void getCustoimerListSuccess(GetCommonResponseModel update);

        void getCustomerListFaile(String message);

    }
}

