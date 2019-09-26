package com.example.user.lankabellapps.helper;

import android.os.AsyncTask;

import com.example.user.lankabellapps.models.TrackingData;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.sync.TrackingSync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 2017-04-17.
 */

public class ManualSync implements TrackingSync.TrackingEvents {

    int trackingCount;
    UserProfile userProfile;
    List<UserProfile> userProfileList;


    public void commonData() {
        userProfile = new UserProfile();
        userProfileList = new ArrayList<>();
        userProfileList = userProfile.getAllUsers();


    }

    public void syncAll() {
        commonData();
        syncTracking();
    }


    public void syncTracking() {
        new SyncAttendecne().execute("");
    }

    @Override
    public void onTrackingSuccess(GetCommonResponseModel update, String addedTime) {
        System.out.println("**** Tracking success ...........");
        if (update.getData() != null) {
            if (!update.getData().equals(Constants.LOGIN_SUCCESSFUL)) {

                TrackingData trackingData = new TrackingData();
                trackingData.updateIsSynced(addedTime, "true");

                //trackingCountArray.add("1");


            } else {
                // trackingCountArray.add("0");

            }
        } else {
            // trackingCountArray.add("0");
        }
        // checkTrackingSyncStatus();
    }

    @Override
    public void onTrackingFailed(String message) {
        // trackingCountArray.add("0");
        // checkTrackingSyncStatus();
    }


    private class SyncAttendecne extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            TrackingData trackingData = new TrackingData();
            List<TrackingData> trackingDataArrayList = new ArrayList<>();

            trackingDataArrayList = trackingData.getUpdatesByisSynced();

            trackingCount = trackingDataArrayList.size();
            System.out.println("**** trackingCount "+trackingCount);

            if (trackingCount > 0) {

                int time = 50;
                if (trackingCount < 300 && trackingCount > 100) {
                    time = 80;
                } else if (trackingCount < 1000 && trackingCount > 301) {
                    time = 200;
                } else if (trackingCount > 1000) {
                    time = 300;
                }

                for (TrackingData t : trackingDataArrayList) {
//                PaymentScheduleSync paymentScheduleSync = new PaymentScheduleSync(this);
//                // paymentScheduleSync.PaymentSchedule(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), p.getPaymentId(), "M", p.companyCode, p.getAccountID(), Constants.PAYMENT_UPDATE_STATUS_OUTSTANDING_PENDING, userProfileList.get(0).getCollectorCode(), String.valueOf(collectorProfileList.get(0).getEpfNo()), p.getPayment());
//                paymentScheduleSync.PaymentSchedule(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), p.getPaymentId(), "M", p.companyCode, p.getAccountID(), Constants.PAYMENT_UPDATE_STATUS_OUTSTANDING_PENDING, userProfileList.get(0).getCollectorCode(), String.valueOf(collectorProfileList.get(0).getEpfNo()), p.getPayment());

                    TrackingSync trackingSync = new TrackingSync(ManualSync.this);
                    trackingSync.Trackings(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(),
                            t.getAddedTime(), String.valueOf(t.getLongi()), String.valueOf(t.getLati()), t.getAccuracy(), t.getSpeed(),
                            t.getUseTime());

                    //sendToFirebase(t,0.0f);
                    //new SendToFirebaseData().sendToFirebase(t , 0.0f);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("* tracking error");
                    }

                }
            } else {
                // ++currentSyncNumber;
                //syncListArray.add("1");
                //checkAllSyncStatus();
                // delegate.onManualSyncSuccess("S");
            }



            return "Download failed";
        }

    }


}
