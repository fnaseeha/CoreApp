package com.example.user.lankabellapps.popups;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.lankabellapps.BuildConfig;
import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.adapters.DownloadingAdapter;
import com.example.user.lankabellapps.adapters.SingleListAdapter;
import com.example.user.lankabellapps.helper.CommonHelperClass;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.helper.FilterSingleItem;
import com.example.user.lankabellapps.helper.StringEmptyCheck;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.SingleItemModel;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Thejan on 6/28/2016.
 */
public class SingleItemListPopup implements DownloadingAdapter.SingleItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private LayoutInflater mLayoutInflater;
    private View mDialogView;
    private Dialog mListDialog;
    private Context mContext;
    private RecyclerView mRvSingleList;
    private EditText mSearchView;
    private TextView mHeaderTitle;
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private RelativeLayout mRelSearchContainer;
    private DownloadingAdapter mAdapter;
    private SingleItemPopupItemClickListener mListener;
    private List<DownloadItemModel> mSingleList = new ArrayList<>();
    private String mBtnType;
    private ImageButton btnOk;
    private int checkStatus = 0;
    private String callActivity;
    DownloadTask downloadTask;

    File sdcard;
    //DownloadTask downloadTask;

    private String currentAppName = "", currentPackageName = "", currentUrl = "";
    DownloadManager manager;
    long downloadId;
    DownloadManager.Request request;

    List<AvailableApps> availableAllApps = new ArrayList<>(), availableNotInstalledAppsList = new ArrayList<>(), availableInstalled = new ArrayList<>();

    ProgressDialog mProgressDialog, progressDialog;

    public SingleItemListPopup(Context context, SingleItemPopupItemClickListener listener, LayoutInflater inflater, String callActivity) {
        mLayoutInflater = inflater;
        mContext = context;
        mListener = listener;
        this.callActivity = callActivity;
        configView();
    }

    public void makeDialog(String title, boolean hasSearch, List<DownloadItemModel> list, String btnType) {
        mHeaderTitle.setText(title);
        mBtnType = btnType;
//        if (false) {
//            mRelSearchContainer.setVisibility(View.VISIBLE);
//        }

//        if(mBtnType.equals("account")){
//            btnOk.setVisibility(View.VISIBLE);
//        }
        checkAppsAvailability();

        for (AvailableApps a : availableNotInstalledAppsList) {

            mSingleList.add(new DownloadItemModel(a.getAppName(), a.getAppId(), CommonHelperClass.getApplicationIcon(mContext, a.getIconName()), a.getPackagename(), a.getUrl()));

        }

        setToAdapter(mSingleList);
    }

    private void configView() {
        mListDialog = new Dialog(mContext, R.style.CustomDialog);
        mDialogView = mLayoutInflater.inflate(R.layout.dialog_list, null);
        ImageButton btnClose = (ImageButton) mDialogView.findViewById(R.id.img_btn_close);
        btnOk = (ImageButton) mDialogView.findViewById(R.id.img_btn_ok);
        mRvSingleList = (RecyclerView) mDialogView.findViewById(R.id.rv_single_list);
        mSearchView = (EditText) mDialogView.findViewById(R.id.et_searchView);
        mHeaderTitle = (TextView) mDialogView.findViewById(R.id.tv_dialog_header_title);
        mProgressBar = (ProgressBar) mDialogView.findViewById(R.id.progressBar);
        mTvMessage = (TextView) mDialogView.findViewById(R.id.tv_message);
        mRelSearchContainer = (RelativeLayout) mDialogView.findViewById(R.id.rel_lay_search_container);

        mSearchView.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);

        mSearchView.addTextChangedListener(mTextWatcher);
        mRvSingleList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvSingleList.setHasFixedSize(true);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDialog.dismiss();
            }
        });


//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("Downloading");
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setCancelable(false);

        AvailableApps availableApps = new AvailableApps();
        availableAllApps = availableApps.getAllApps();


// instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(mContext);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mProgressDialog.setCancelable(true);

// execute this when the downloader must be fired
        downloadTask = new DownloadTask(mContext);


        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(checkStatus == 0){
//
////                    if(callActivity.equals(Constants.CUSTOMER_SEARCH_ACTIVITY)) {
////                       // ((CustomerSearchActivity) mContext).showColorSnackBar("You must check at least one account", "Error");
////                    }
//                }else {
//                    mListDialog.dismiss();
//
//                    mListener.onClickOk(mSingleList);
//                }
//
//            }
//        });

        // downloadTask = new DownloadTask(mContext);
        ////// downloadTask.execute("the url to the file you want to download");

//        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                // downloadTask.cancel(true);
//            }
//        });

//        if(mSingleList.isEmpty()){
//            mTvMessage.setVisibility(View.VISIBLE);
//            mTvMessage.setTextColor(Color.RED);
//            mTvMessage.setText("No Apps Available");
//        }else{
//            mTvMessage.setVisibility(View.GONE);
//        }

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 80;
        float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 90;


        final float x, y;
        DisplayPixelCalculator converter = new DisplayPixelCalculator();
        x = converter.dipToPixels(mContext, dpWidth);
        y = converter.dipToPixels(mContext, dpHeight);

        mListDialog.setContentView(mDialogView);
        mListDialog.show();
        mListDialog.setCanceledOnTouchOutside(false);
        mListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mListDialog.getWindow().setLayout((int) x, (int) y);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterItem(s.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void filterItem(String search) {
        boolean isEmpty = new StringEmptyCheck().isNotNullNotEmptyNotWhiteSpaceOnly(search);
        mTvMessage.setVisibility(View.GONE);
        mTvMessage.setText("");
        if (!isEmpty) {
            if (mSingleList != null) {
                setToAdapter(mSingleList);
            }
        } else {
            if (mSingleList != null) {
                List<DownloadItemModel> SingleList = new FilterSingleItem().filterSRDownload(mSingleList, search);
                if (SingleList.size() > 0) {
                    setToAdapter(SingleList);
                } else {
                    mAdapter.clearDataSet();
                    mTvMessage.setText("No Result for \"" + search + "\"");
                    mTvMessage.setVisibility(View.VISIBLE);
                }
            } else {
                mTvMessage.setText("Poor Internet Connection.");
                mTvMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setToAdapter(List<DownloadItemModel> list) {

        List<DownloadItemModel> currentList = new ArrayList<>();
        if (list.size() == 0) {
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText("No Apps Available");
        }
        for (DownloadItemModel singleItemModel : list) {
            if (!singleItemModel.itemKey.equals("1") || !singleItemModel.itemKey.equals("2") || !singleItemModel.itemKey.equals("3")) {
                currentList.add(singleItemModel);
            }
        }

        if (mAdapter != null) {
            mAdapter.clearDataSet();
        }
        mAdapter = new DownloadingAdapter(mContext, currentList, this);
        mRvSingleList.setAdapter(mAdapter);
        mSearchView.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickSingleItem(DownloadItemModel selectedItem, int position) {
//        downloadTask.execute(selectedItem.url);
        currentPackageName = selectedItem.packagename;
        currentAppName = selectedItem.itemText;
        currentUrl = selectedItem.url;

//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            public void onReceive(Context ctxt, Intent intent) {
////                    Intent install = new Intent(Intent.ACTION_VIEW);
////                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    install.setDataAndType(uri,
////                            manager.getMimeTypeForDownloadedFile(downloadId));
////                    mContext.startActivity(install);
////
////                    mContext.unregisterReceiver(this);
//                // mContext.finish();
//                System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzz");
//                //progressDialog.dismiss();
//
//                installApk();
//            }
//        };
//
//        mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mProgressDialog.setMessage("Downloading " + currentAppName);
        try {
            downloadTask.execute("http://" + currentUrl.trim());
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(mContext, "Check the internet connection...", Toast.LENGTH_LONG).show();
        }


//        Intent i;
//        PackageManager manager = mContext.getPackageManager();
//        try {
//            i = manager.getLaunchIntentForPackage("com.example.user.lankabellapps");
//            if (i == null)
//                throw new PackageManager.NameNotFoundException();
//            i.addCategory(Intent.CATEGORY_LAUNCHER);
//            mContext.startActivity(i);
//        } catch (PackageManager.NameNotFoundException e) {
//            InstallAPK downloadAndInstall = new InstallAPK();
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setMessage("Downloading...");
//            downloadAndInstall.setContext(mContext, mProgressDialog);
//            downloadAndInstall.execute(selectedItem.url);
//        }
        try {
//            InstallAPK downloadAndInstall = new InstallAPK();
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setMessage("Downloading " + currentAppName);
//            downloadAndInstall.setContext(mContext, mProgressDialog);
//            downloadAndInstall.execute(selectedItem.url);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public interface SingleItemPopupItemClickListener {
        void onClickSingleItem(SingleItemModel selectedItem, String btnType, int position);

        void onClickOk(List<SingleItemModel> list);
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                String fileName = currentAppName + ".apk";
                destination += fileName;
                File file = new File(destination);
                if (file.exists()) {
//                //file.delete() - test this, I think sometimes it doesnt work
                    file.delete();
                }

                final Uri uri = Uri.parse("file://" + destination);


                output = new FileOutputStream(destination);


                //            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//            String fileName = currentAppName + ".apk";
//            destination += fileName;
//            final Uri uri = Uri.parse("file://" + destination);
//
//            //Delete update file if exists
//            File file = new File(destination);
//            if (file.exists())
//                //file.delete() - test this, I think sometimes it doesnt work
//                file.delete();
//
//            //get url of app on server
//            //String url = Main.this.getString(R.string.update_app_url);
//
//
//            request = new DownloadManager.Request(Uri.parse("http://" + currentUrl.trim()));
//            request.setDescription("Downloading " + currentAppName);
//            request.setTitle(currentAppName);
//
//            request.setDestinationUri(uri);
//
//
//            if (isStoragePermissionGranted()) {
//                manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
//                downloadId = manager.enqueue(request);
//                downloadingComplete();
//            } else {
//                Log.v("Popup Installing", "Permission is revoked");
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Check the internet connection... ", Toast.LENGTH_LONG).show();
            else
                installApk();
            //Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }


//    public class InstallAPK extends AsyncTask<String, Void, Void> {
//
//
//        int status = 0;
//
//        private Context context;
//
//        public void setContext(Context context, ProgressDialog progress) {
//            this.context = context;
//            progressDialog = progress;
//        }
//
//        public void onPreExecute() {
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(String... arg0) {
//
//            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//            String fileName = currentAppName + ".apk";
//            destination += fileName;
//            final Uri uri = Uri.parse("file://" + destination);
//
//            //Delete update file if exists
//            File file = new File(destination);
//            if (file.exists())
//                //file.delete() - test this, I think sometimes it doesnt work
//                file.delete();
//
//            //get url of app on server
//            //String url = Main.this.getString(R.string.update_app_url);
//
//
//            request = new DownloadManager.Request(Uri.parse("http://" + currentUrl.trim()));
//            request.setDescription("Downloading " + currentAppName);
//            request.setTitle(currentAppName);
//
//            request.setDestinationUri(uri);
//
//
//            if (isStoragePermissionGranted()) {
//                manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
//                downloadId = manager.enqueue(request);
//                downloadingComplete();
//            } else {
//                Log.v("Popup Installing", "Permission is revoked");
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//
//
//            return null;
//        }
//
//
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//            progressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//
//        public void onPostExecute(Void unused) {
//
//
////            Intent intent = new Intent(Intent.ACTION_VIEW);
////            intent.setDataAndType(Uri.fromFile(new File(sdcard, "Android/data/com.mycompany.android.games/" + currentAppName + "/" + currentAppName + "1" +".apk")), "application/vnd.android.package-archive");
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
////            context.startActivity(intent);
//
//
//            if (status == 1)
//                Toast.makeText(context, "App Not Available", Toast.LENGTH_LONG).show();
//        }
//    }

    private void downloadingComplete() {
        boolean downloading = true;

        while (downloading) {

            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(downloadId); //filter by id which you have receieved when reqesting download from download manager
            Cursor cursor = manager.query(q);
            cursor.moveToFirst();
            int bytes_downloaded = cursor.getInt(cursor
                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                System.out.println("Download Complete");
                downloading = false;
            }

            final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);


            //progressDialog.setProgress((int) dl_progress);
            //onProgressUpdate(String.valueOf(dl_progress));


            // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
            cursor.close();
        }


    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Popup Installing", "Permission is granted");
                return true;
            } else {


                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Popup Installing", "Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.v("Permission Request", "Permission: " + permissions[0] + "was " + grantResults[0]);
//            //resume tasks needing this permission
//            manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
//            downloadId = manager.enqueue(request);
//            downloadingComplete();
//        }

        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    installApk();
                }else{
                    mListDialog.dismiss();
                }
                break;

            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void installApk() {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/")+currentAppName+".apk"));
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        mContext.startActivity(intent);

//        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
//                .setDataAndType(Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),
//                        "application/vnd.android.package-archive");
//        mContext.startActivity(promptInstall);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)); // a directory
//        intent.setDataAndType(uri, "*/*");
//        mContext.startActivity(Intent.createChooser(intent, "Open folder"));

        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                    + Environment.DIRECTORY_DOWNLOADS+"/");
//            intent.setDataAndType(uri,"file");
//            mContext.startActivity(Intent.createChooser(intent, "Open folder"));


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+currentAppName+".apk"),
//                        "application/vnd.android.package-archive");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
//        mContext.startActivity(intent);

        // mContext.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

//        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +  "/Local/Internal storage/Download");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setDataAndType(selectedUri , "text/csv");
//
//        if(intent.resolveActivityInfo(mContext.getPackageManager(),0)!=null){
//            mContext.startActivity(Intent.createChooser(intent, "Open folder"));
//        }else{
//            Toast.makeText(mContext, "Go to the Downloads Folder to Install the app", Toast.LENGTH_LONG).show();
//        }




        File toInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + currentAppName + ".apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+ ".fileprovider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(intent);
            mListDialog.dismiss();
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            mListDialog.dismiss();
        }

//            if(isStoragePermissionGranted()){
//                Notification.Builder notification = new Notification.Builder(mContext);
//
//                Intent install_intent = new Intent(Intent.ACTION_VIEW);
//                install_intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                install_intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + currentAppName + ".apk")), "application/vnd.android.package-archive");
//
//                mContext.startActivity(install_intent);
//
//                mListDialog.dismiss();
//            }else{
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            }









//        PendingIntent pending = PendingIntent.getActivity(mContext,0, install_intent, 0);
//
//        NotificationManager nm = (NotificationManager) mContext
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notification.setContentIntent(pending)
//                .setSmallIcon(R.drawable.collector_app)
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setContentTitle(currentAppName)
//                .setContentText(currentAppName);
//        Notification n = notification.build();
//
//        nm.notify(1, n);

        //((MainActivity) mContext).showNotification("",0,"");


    }


//    public static void InstallAPKs(String filename){
//        File file = new File(filename);
//        if(file.exists()){
//            try {
//                String command;
//                command = "adb install -r " + filename;
//                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
//                proc.waitFor();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public class DownloadTask extends AsyncTask<String, Integer, String> {
//
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//
//        public DownloadTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(String... sUrl) {
//            InputStream input = null;
//            OutputStream output = null;
//            HttpURLConnection connection = null;
//            try {
//                URL url = new URL(sUrl[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // expect HTTP 200 OK, so we don't mistakenly save error report
//                // instead of the file
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return "Server returned HTTP " + connection.getResponseCode()
//                            + " " + connection.getResponseMessage();
//                }
//
//                // this will be useful to display download percentage
//                // might be -1: server did not report the length
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                input = connection.getInputStream();
//                output = new FileOutputStream("/"+ currentAppName +".extension");
//
//                byte data[] = new byte[4096];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    // allow canceling with back button
//                    if (isCancelled()) {
//                        input.close();
//                        return null;
//                    }
//                    total += count;
//                    // publishing the progress....
//                    if (fileLength > 0) // only if total length is known
//                        publishProgress((int) (total * 100 / fileLength));
//                    output.write(data, 0, count);
//                }
//            } catch (Exception e) {
//                return e.toString();
//            } finally {
//                try {
//                    if (output != null)
//                        output.close();
//                    if (input != null)
//                        input.close();
//                } catch (IOException ignored) {
//                }
//
//                if (connection != null)
//                    connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // take CPU lock to prevent CPU from going off if the user
//            // presses the power button during download
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    getClass().getName());
//            mWakeLock.acquire();
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            mWakeLock.release();
//            mProgressDialog.dismiss();
//            if (result != null)
//                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + currentAppName)), currentPackageName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//
//
//            checkAppsAvailability();
//
//            for(AvailableApps a : availableNotInstalledAppsList){
//                mSingleList.add(new DownloadItemModel(a.getAppName(),a.getAppId(),CommonHelperClass.getApplicationIcon(mContext,a.getIconName()),a.getPackagename(),a.getUrl()));
//            }
//
//            setToAdapter(mSingleList);
//        }
//    }

    private void checkUpdateAvailability(){

    }


    private void checkAppsAvailability() {
        availableNotInstalledAppsList.clear();
        for (AvailableApps a : availableAllApps) {

            String tempPackagename = a.getPackagename();
            //
            if (!CommonHelperClass.appInstalledOrNot(mContext, tempPackagename)) {
                if (!a.getAppId().matches("1|2|3")) {
                    System.out.println(a.getAppId());
                    availableNotInstalledAppsList.add(a);
                }
            } else {
                availableInstalled.add(a);
            }

        }

    }


}
