package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Thejan on 7/4/2017.
 */

public class DbAdapter {
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DbAdapter(Context c)
    {
        context = c;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper( Context context)
        {
            super(context, "LankaBellAppsDB.db", null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                System.out.println("----------------------===============================");
                //db.execSQL();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
      /* UPDATE CODE HERE */
        }
    }

    public DbAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public SQLiteDatabase getDatabase(){
        return db;
    }



    public void close()
    {
        DBHelper.close();
    }

    public void dosomething()
    {
        // code here
        System.out.println("DB version " + db.getVersion());
    }

    public String getStringEntry(String column, String table, String match, String matchCol) {
        String text = null;
        Cursor c = db.rawQuery("SELECT " + column + " FROM " + table
                + " WHERE " + matchCol + "=" + "'" + match + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                text = c.getString(c.getColumnIndex(column));
            }
        }
        return text;
    }

    // Other database abstraction layer methods down here...
}