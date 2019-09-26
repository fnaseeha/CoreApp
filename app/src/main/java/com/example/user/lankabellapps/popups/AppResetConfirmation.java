package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.user.lankabellapps.activities.AddMerchantsActivity;
import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.activities.LocationShowActivity;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.ViewCustomerActivity;
import com.example.user.lankabellapps.fragments.HomeFragment;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.models.Attendence;


/**
 * Created by thejanthrimanna on 20/05/16.
 */
public class AppResetConfirmation {
    Context context;
    ColoredSnackbar coloredSnackbar;
    private Dialog dialog;

    public AppResetConfirmation(Context context) {
        this.context = context;
    }

    public void ShowConfirmation(final int k, final String fromActivity) {
        dialog = new Dialog(context, R.style.CustomDialog);
        LayoutInflater i = LayoutInflater.from(context);
        View view = i.inflate(R.layout.dialog_confirmation, null);
        //dialog = bDialog;

//        ((ViewGroup)dialog.getWindow().getDecorView())
//                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
//                context,android.R.anim.slide_in_left));

//        ((ViewGroup)dialog.getWindow().getDecorView())
//                .getChildAt(0).setAnimation(AnimationUtils.loadAnimation(
//                context,android.R.style.Dialog_));

        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView message = (TextView) view.findViewById(R.id.tv_message);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);


        switch (k) {

            case 1:
                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to exit?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) context).CallAppReset();

                        dialog.dismiss();

                    }
                });


                break;

            case 2:
                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want mark OUT?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        //new ColoredSnackbar(context).showSnackBar("Proccessing...",new ColoredSnackbar(context).TYPE_UPDATE, 5000);
                        ((AttendenceActivity) context).DialogConfermation(1);

                    }
                });
                break;

            case 3:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want mark IN?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        ((AttendenceActivity) context).DialogConfermation(2);

                    }
                });

                break;


            case 4:
                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to register this Customer");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((ViewCustomerActivity) context).getLocation();

                    }
                });

                break;

            case 5:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to save this Location");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((LocationShowActivity) context).updateLocation(1);

                    }
                });

                break;

            case 6:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to update the Location");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((LocationShowActivity) context).updateLocation(2);

                    }
                });

                break;

            case 7:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to add the merchant?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((AddMerchantsActivity) context).merchantUpdate(1);

                    }
                });

                break;

            case 8:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want to update the merchant?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((AddMerchantsActivity) context).merchantUpdate(2);

                    }
                });

                break;

            case 9:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Update is Available. \n Do you want to Update?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        ((MainActivity) context).doUpdateCore();

                    }
                });

                break;

            case 10:

//                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Failed to submit IN/OUT \n Please check and Refresh!");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                      //  ((AttendenceActivity) context).DialogConfermation(4);

//                        ((HomeFragment) Context).onResume();

                    }
                });

                break;


            case 11:

                btnCancel.setText("No");
                btnOk.setText("Yes");
                message.setText("Do you want Request a Leave?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        ((AttendenceActivity) context).DialogConfermation(3);

                    }
                });

                break;




        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) context).appReset();
                dialog.dismiss();

            }
        });


        try {
            dialog.setContentView(view);
            dialog.show();
            dialog.setCancelable(false);
//            bDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            bDialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 34;
            float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 98;

            final float x, y;
            DisplayPixelCalculator converter = new DisplayPixelCalculator();
            x = converter.dipToPixels(context, dpWidth);
            y = converter.dipToPixels(context, dpHeight);

            dialog.setContentView(view);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout((int) x, (int) y);


        } catch (Exception e) {
            System.out.println(e);
        }



    }

}
