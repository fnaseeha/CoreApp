package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.AddMerchantsActivity;
import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.activities.LocationShowActivity;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.activities.ViewCustomerActivity;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;

/**
 * Created by Thejan on 3/28/2017.
 */
public class UpdateAppsPopup {

    Context context;
    ColoredSnackbar coloredSnackbar;
    private Dialog dialog;
    private ButtonClicksUpdatePopups deleget;

    public UpdateAppsPopup(Context context, ButtonClicksUpdatePopups deleget) {
        this.context = context;
        this.deleget = deleget;
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
                message.setText("Update is Available. \n Do you want to Update?");

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleget.onButtonClcik(1);

                        dialog.dismiss();

                    }
                });


                break;


        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleget.onButtonClcik(0);
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

    public interface ButtonClicksUpdatePopups {
        void onButtonClcik(int x);

    }

}

