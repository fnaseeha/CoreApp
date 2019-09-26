package com.example.user.lankabellapps.adapters;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by user on 2017-07-05.
 */

public class SampleContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.sales.lankabellapps.user.tsrsalesapp.auth";
    private static final String BASE_PATH = "/data/data/" + "com.example.user.lankabellapps" + "/databases/" + "LankaBellAppsDB.db";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
//        Context sharedContext = null;
//        sharedContext = this.createPackageContext("com.example.user.lankabellapps", Context.CONTEXT_INCLUDE_CODE);
//        if (sharedContext == null) {
//            //return;
//        }

        DbAdapter sharedDBadapter = new DbAdapter(getContext());
        sharedDBadapter.open();
        database = sharedDBadapter.getDatabase();
//        DbAdapter helper = new DbAdapter(getContext());
//        database = helper.getWritableDatabase();
        return true;
    }

    public static final String TABLE_CONTACTS = "AvailableApps";
    public static final String CONTACT_IDD = "appId";
    public static final String CONTACT_NAME = "appName";
    public static final String CONTACT_PHONE = "package";
    public static final String CONTACT_CREATED_ON = "iconName";

    public static final String[] ALL_COLUMNS =
            {CONTACT_IDD,CONTACT_NAME,CONTACT_PHONE,CONTACT_CREATED_ON};


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
//                cursor = database.query("AvailableApps", DBOpenHelper.ALL_COLUMNS,
//                        s, null, null, null, DBOpenHelper.CONTACT_NAME + " ASC");

                cursor = database.query(TABLE_CONTACTS, ALL_COLUMNS,
                        s, null, null, null, CONTACT_NAME + " ASC");


//                cursor = database.rawQuery("SELECT " + "AvailableApps" + " FROM " + table
//                        + " WHERE " + matchCol + "=" + "'" + match + "'", null);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(TABLE_CONTACTS, null, contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                delCount = database.delete(TABLE_CONTACTS, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                updCount = database.update(TABLE_CONTACTS, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
