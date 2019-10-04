package com.example.user.lankabellapps.helper;

/**
 * Created by thejanthrimanna on 02/06/16.
 */


import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

    /**
     * Created by Thejan on 5/2/2016.
     */
    public class DateTimeSelectView implements View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

        private Context mContext;
        private FragmentManager mFragmentManager;
        private boolean mIsEnableTime = false;
        private String mDate;
        private Button mBtnSelectDate;
        private String mTime;
        private TextView mTvDateTitle;
        private boolean isFillData;
        private String mFieldId;
        private int mPosition;
        private DateTimeSelectListener mDateTimeSelectListener;
        private int callFrom;

        private Date SelecetedStartDate = new Date() , SelectedEndDate = new Date();

        private TimePickerDialog dlg;

        private String AfterFreeTimeChange;
        private String Mdate, Mmonth, Myear, Mhours, Mminuts, MmonthName;



        public DateTimeSelectView(Context context, FragmentManager fragmentManager, DateTimeSelectListener selectListener) {
            mContext = context;
            mFragmentManager = fragmentManager;
            mDateTimeSelectListener = selectListener;
        }

        private String getDateTimeFormat(String dateTime) {
            String dateString = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            try {
                Date dt = new Date();
                dt = dateFormat.parse(dateTime);
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                dateString = newFormat.format(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return dateString;

        }

        public static String getCurrentTimeUsingCalendar() {
            Calendar cal = Calendar.getInstance();
            Date date=cal.getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedDate=dateFormat.format(date);
            return formattedDate;
        }

        public static int getTodayDate(){
            Calendar cal = Calendar.getInstance();
            Date date=cal.getTime();
            SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate=dateOnly.format(date);
            String all_date[] = formattedDate.split("/");
            return Integer.parseInt(all_date[0]);
        }

        public static long getCurrentTimeMilliSecond() {
            Calendar cal = Calendar.getInstance();
            return cal.getTimeInMillis();
        }

        public static long ConvertToMilliseconds(String date)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            try
            {
                Date mDate = sdf.parse(date);
                long timeInMilliseconds = mDate.getTime();
                return timeInMilliseconds;
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return 0;
        }
        public static String getCurrentTimeOnly(){
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");

           return dateOnly.format(cal.getTime());
        }

        public String getFieldId() {
            return mFieldId;
        }

        public void setFieldId(String fieldId) {
            mFieldId = fieldId;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public boolean isFillData() {
            return isFillData;
        }

        public void setIsFillData(boolean isFillData) {
            this.isFillData = isFillData;
        }

        @Override
        public void onClick(View v) {

            Calendar now = Calendar.getInstance();

            com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show(mFragmentManager, "DatePickerDialog");

        }

        public void ShowDialof(View v,int CallFrom, String AfterFreeTime){


            this.callFrom = CallFrom;
            this.AfterFreeTimeChange = AfterFreeTime;

            switch (CallFrom){

                case 1:
                    Calendar now1 = Calendar.getInstance();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            this,
                            now1.get(Calendar.YEAR),
                            now1.get(Calendar.MONTH),
                            now1.get(Calendar.DAY_OF_MONTH)
                    );

                    if(!AfterFreeTime.equals("")){
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        try {
                            cal.setTime(sdf.parse(AfterFreeTime));
                            cal.add(Calendar.DATE,2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        datePickerDialog.setMinDate(cal);
                    }

                    datePickerDialog.show(mFragmentManager, "DatePickerDialog");


                    break;

                case 3:

                    Calendar now2 = Calendar.getInstance();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog1 = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            this,
                            now2.get(Calendar.YEAR),
                            now2.get(Calendar.MONTH),
                            now2.get(Calendar.DAY_OF_MONTH)
                    );

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    try {
                        cal.setTime(sdf.parse(AfterFreeTime));
                        cal.add(Calendar.DATE,2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    datePickerDialog1.setMinDate(cal);
                    datePickerDialog1.show(mFragmentManager, "DatePickerDialog");


                    break;

                case 2:

                    Calendar now = Calendar.getInstance();
////                    dlg = TimePickerDialog.newInstance(this,
////                            now.get(Calendar.HOUR_OF_DAY),
////                            now.get(Calendar.MINUTE),
////                            true);
//
                    com.wdullaer.materialdatetimepicker.time.TimePickerDialog dlg = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                            this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false);

                    dlg.setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
                    dlg.setTimeInterval(1,30);


                    dlg.show(mFragmentManager, "Timepickerdialog");
                    break;
            }


//            if(AfterFreeTimeChange.trim().equals("") || callFrom ==2) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                        this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//
//                System.out.println(callFrom);
//
//                if(callFrom ==2){
//                    try {
//                        System.out.println(AfterFreeTime);
//
//                        Date mindate = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(AfterFreeTimeChange);
//                        Calendar cal =  Calendar.getInstance();
//                        cal.setAddedTime(mindate);
//                        datePickerDialog.setMinDate(cal);
//
//                        SelecetedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(AfterFreeTimeChange);
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                 datePickerDialog.show(mFragmentManager, "DatePickerDialog");
//            }else{
//                Date dt = new Date();
//
//                //SimpleDateFormat sdf = new;
//
//                //String AfterTimeDate = sdf.format(AfterFreeTimeChange);
//
//                try {
//                    Date newdate = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(AfterFreeTimeChange);
//                    Calendar cal =  Calendar.getInstance();
//                    cal.setAddedTime(newdate);
//
//                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
//                    datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                            this,
//                            cal.get(Calendar.YEAR),
//                            cal.get(Calendar.MONTH),
//                            cal.get(Calendar.DAY_OF_MONTH)
//
//
//                    );
//
////                    if(CallFrom ==2){
////                       // datePickerDialog.setMinDate(cal);
////                    }
//                    datePickerDialog.show(mFragmentManager, "DatePickerDialog");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
        }


        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            mDate = "" + new DecimalFormat("00").format(dayOfMonth) + "/" + new DecimalFormat("00").format(monthOfYear) + "/" + year;
            //mBtnSelectDate.setText(mDate);
            mDateTimeSelectListener.onDateTimeSelectTextFocusOut(mPosition, mDate);
            System.out.println("mDate" + mDate);

            int tempmonth = monthOfYear +1; //this temporaly used to send the corect month to the end addedTime selection

            try {
                SelectedEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + tempmonth + "-" + dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(AfterFreeTimeChange.trim().equals("")) {

                Calendar now = Calendar.getInstance();
                dlg = TimePickerDialog.newInstance(this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true);

                dlg.setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            }else {

                Date dt = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");




                    try {
                        Date newdate = new  SimpleDateFormat("dd/MM/yyyy").parse(AfterFreeTimeChange);
                        Calendar cal =  Calendar.getInstance();
                        cal.setTime(newdate);

                        dlg = TimePickerDialog.newInstance(this,
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



            }

                dlg.setCancelable(false);


                Mdate = new DecimalFormat("00").format(dayOfMonth);

                int month = monthOfYear + 1;
                Mmonth = new DecimalFormat("00").format(month);
                Myear = "" + year;

                DateFormatSymbols dfs = new DateFormatSymbols();
                String[] months = dfs.getShortMonths();
                MmonthName = months[monthOfYear];


            if(callFrom == 2) {
                try {

                Date mindate = null;
                mindate = new SimpleDateFormat("yyyy-MM-dd").parse(this.AfterFreeTimeChange);
                    Date newdate = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(AfterFreeTimeChange);
                    System.out.println(mindate + " " + SelectedEndDate);

                    System.out.println(mindate.getTime() + " " + SelectedEndDate.getTime());



                if (SelectedEndDate.getTime() == (mindate.getTime())) {

                    System.out.println("equal");

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(newdate);
                    System.out.println(Calendar.HOUR_OF_DAY + " " + cal.get(Calendar.MINUTE)+5 + " " + cal.get(Calendar.SECOND));
                        dlg.setMinTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)+5, cal.get(Calendar.SECOND));
                        //dlg.setMinTime(12, 00, 00);

                }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
                //dlg.setMinTime(12, 00, 00);
              //  dlg.show(mFragmentManager, "Timepickerdialog");
            backtoActivity();




        }


        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            mTime = " " + hourOfDay + ":" + minute + ":00";
           // mBtnSelectDate.setText(mDate + mTime);



            mDateTimeSelectListener.onDateTimeSelectTextFocusOut(mPosition, mDate + mTime);

            Mhours = new DecimalFormat("00").format(hourOfDay);
            Mminuts = new DecimalFormat("00").format(minute);

            backtoActivity();
        }

        private void backtoActivity() {

            mDateTimeSelectListener.onSet(Mdate,Mmonth,Myear,Mhours,Mminuts, MmonthName ,callFrom);
        }

        public interface DateTimeSelectListener {
            void onSet(String Mdate, String Mmonth, String Myear, String Mhours, String Mminuts, String monthName, int CallFrom);
            void onDateTimeSelectTextFocusOut(int position, String displayText);
        }
    }

