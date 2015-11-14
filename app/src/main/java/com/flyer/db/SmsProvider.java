package com.flyer.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.flyer.ibs.Constant;

/**
 * Created by liangchuanfei on 15/11/14.
 * <p/>
 * 利用ContentProvider的方式对外提供数据
 */
public class SmsProvider extends ContentProvider {
    private static final String AUTHORITY = "com.flyer.sms.provider";
    //对外公布查询所需的uri
    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/sms");
    private static UriMatcher mUriMatcher;

    private static final int SMS_ALL = 0;
    private static final int SMS_ONE = 1;

    private SmsSendRecordHlper mSmsSendRecordHlper;
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * 使用静态块初始化一些变量
     */
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "sms", SMS_ALL);
        mUriMatcher.addURI(AUTHORITY, "sms/#", SMS_ONE);
    }


    @Override
    public boolean onCreate() {
        mSmsSendRecordHlper = SmsSendRecordHlper.getINSTANCE(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case SMS_ALL:

                break;
            case SMS_ONE:
                long id = ContentUris.parseId(uri);
                selection = "_id = ?";
                selectionArgs = new String[]{
                        String.valueOf(id)
                };

                break;
            default:
                throw new IllegalArgumentException("Wrong uri" + uri);
        }
        mSQLiteDatabase = mSmsSendRecordHlper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query(Constant.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), URI_SMS_ALL);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        if (match != SMS_ALL) {
            throw new IllegalArgumentException("Wrong uri" + uri);
        }
        mSQLiteDatabase = mSmsSendRecordHlper.getWritableDatabase();
        long rowId = mSQLiteDatabase.insert(Constant.TABLE_NAME, null, values);
        if (rowId > 0) {
            //大于0，说明插入成功
            notifyDataSetChange();
            return ContentUris.withAppendedId(uri, rowId);
        }
        return null;
    }

    /**
     * 通知数据发生变化
     */
    private void notifyDataSetChange() {
        getContext().getContentResolver().notifyChange(URI_SMS_ALL, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
