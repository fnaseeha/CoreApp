package com.example.user.lankabellapps.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ChathuraHettiarachchi on 3/3/16.
 */

/**
 * Created by Thejan on 3/3/16.
 */
public class ColoredSnackbar {

    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff2195f3;
    private static final int orange = 0xffffc107;
    private static final int grey = 0xff676767;

    public static String TYPE_UPDATE = "update";
    public static String TYPE_OK = "ok";
    public static String TYPE_ERROR = "error";
    public static String TYPE_WARING = "warning";

    private Context context;
    Snackbar snackbar;

    public ColoredSnackbar(Context context) {
        this.context = context;
    }

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }

        return snackbar;
    }

    public static Snackbar info(Snackbar snackbar) {
        return colorSnackBar(snackbar, blue);
    }

    public static Snackbar warning(Snackbar snackbar) {
        return colorSnackBar(snackbar, orange);
    }

    public static Snackbar alert(Snackbar snackbar) {
        return colorSnackBar(snackbar, red);
    }

    public static Snackbar confirm(Snackbar snackbar) {
        return colorSnackBar(snackbar, green);
    }

    public static Snackbar processing(Snackbar snackbar) {
        return colorSnackBar(snackbar, grey);
    }

    public void showSnackBar(String message, String snackBarType, int duration){
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setDuration(duration);

        if(snackBarType.equals(TYPE_UPDATE))
            ColoredSnackbar.processing(snackbar).show();
        else if(snackBarType.equals(TYPE_OK))
            ColoredSnackbar.confirm(snackbar).show();
        else if(snackBarType.equals(TYPE_ERROR))
            ColoredSnackbar.alert(snackbar).show();
        else if(snackBarType.equals(TYPE_WARING))
            ColoredSnackbar.warning(snackbar).show();
        else
            ColoredSnackbar.processing(snackbar).show();
    }

    public void dismissSnacBar(){
        if(snackbar != null){
            if(snackbar.isShown()){
                snackbar.dismiss();
            }
        }
    }
}