package com.example.user.lankabellapps.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.example.user.lankabellapps.activities.AttendenceActivity;

/**
 * Created by Naseeha on 20/04/2019.
 */

public class Alert {
    Context context;

    public Alert(Context context) {
        this.context = context;
    }

    public void showLocationAlert(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    public void BuildAlertBox(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Version");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
