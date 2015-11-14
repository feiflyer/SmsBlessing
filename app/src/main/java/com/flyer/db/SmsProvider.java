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
 *
 * 使用ContentProvider共享数据的好处是统一了数据访问方式。
 *
 *
 */
public class SmsProvider extends ContentProvider {
    /**
     * 主机名（或Authority）：用于唯一标识这个ContentProvider，外部调用者可以根据这个标识来找到它。
     *
     */
    private static final String AUTHORITY = "com.flyer.sms.provider";
    /**
     *
     *对外公布查询所需的uri
     *
     * Uri代表了要操作的数据，Uri主要包含了两部分信息：1.需要操作的ContentProvider ，
     * 2.对ContentProvider中的什么数据进行操作，一个Uri由以下几部分组成：

      1.scheme：ContentProvider（内容提供者）的scheme已经由Android所规定为：content://。
      2.主机名（或Authority）：用于唯一标识这个ContentProvider，外部调用者可以根据这个标识来找到它。
      3.路径（path）：可以用来表示我们要操作的数据，路径的构建应根据业务而定，如下：
      •         要操作contact表中id为10的记录，可以构建这样的路径:/contact/10
      •         要操作contact表中id为10的记录的name字段， contact/10/name
      •         要操作contact表中的所有记录，可以构建这样的路径:/contact
     */
    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/sms");

    /**
     UriMatcher：用于匹配Uri，它的用法如下：
     1.首先把你需要匹配Uri路径全部给注册上，如下：
     //常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码(-1)。
     UriMatcher  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
     //如果match()方法匹配content://com.changcheng.sqlite.provider.contactprovider/contact路径，返回匹配码为1
     uriMatcher.addURI(“com.changcheng.sqlite.provider.contactprovider”, “contact”, 1);//添加需要匹配uri，如果匹配就会返回匹配码
     //如果match()方法匹配   content://com.changcheng.sqlite.provider.contactprovider/contact/230路径，返回匹配码为2
     uriMatcher.addURI(“com.changcheng.sqlite.provider.contactprovider”, “contact/#”, 2);//#号为通配符
     */
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

                /**
                 * ContentUris：用于获取Uri路径后面的ID部分，它有两个比较实用的方法：
                 •         withAppendedId(uri, id)用于为路径加上ID部分
                 •         parseId(uri)方法用于从路径中获取ID部分
                 */
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
