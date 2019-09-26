package com.example.user.lankabellapps.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2016-10-17.
 */
public class TimeFormatter {

    /**
     * This methoad use to change the string addedTime to millisecond addedTime.
     *
     * @param timeX   millisecond addedTime to return
     * @param formatX String addedTime
     * @return
     */
    public static String convertStringTimetoMilliseconds(long timeX, String formatX) {

        //long timeInMillis = 0;

        SimpleDateFormat formatter = new SimpleDateFormat(formatX);

        // Create a calendar object that will convert the date and addedTime value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(new Date().getAddedTime());
        calendar.setTimeInMillis(timeX);

        return formatter.format(calendar.getTime());
        //paymentUpdate.setPaymentDate(formatter.format(calendar.getAddedTime()));

        //return timeInMillis;
    }

    /**
     * This method uses to change the addedTime format ot another format
     *
     * @param currentFormmat
     * @param newFormat
     * @param timeX
     * @return
     */

    public static String changeTimeFormat(String currentFormmat, String newFormat, String timeX) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(currentFormmat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(timeX);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    /**
     *
     * @param timeString
     * @param newFormat
     * @return
     */

    public static long changeStringTimeToLong(String timeString , String newFormat){

        Date lastdate = new Date();
        try {
            lastdate = new SimpleDateFormat(newFormat).parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lastdate.getTime();
    }




}
