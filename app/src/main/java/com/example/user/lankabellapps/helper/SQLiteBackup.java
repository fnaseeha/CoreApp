package com.example.user.lankabellapps.helper;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteBackup {

    Context context;
    String packageName="com.example.user.lankabellapps";

    public SQLiteBackup(Context context){
        this.context = context;

    }

    public void exportDB() throws  Exception{
        File folder = new File(Environment.getExternalStorageDirectory() + "/DbLankaBell");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packageName
                        + "//databases//" + "LankaBellAppsDB.db";
                String backupDBPath = "//DbLankaBell//Core"+dateFormat.format(date).toString()+".db"; // From SD directory.
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "Backup Successful!", Toast.LENGTH_SHORT).show();

            }
        }else {
            // Do something else on failure
            Toast.makeText(context, "Backup Failed!", Toast.LENGTH_SHORT).show();
        }

    }
}
