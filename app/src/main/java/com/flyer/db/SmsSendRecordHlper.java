package com.flyer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flyer.ibs.Constant;

/**
 * Created by liangchuanfei on 15/11/14.
 * 单例模式
 */
public class SmsSendRecordHlper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sms_db_name";
    private static final int DB_VERSION = 2;

    private static SmsSendRecordHlper INSTANCE;

    private SmsSendRecordHlper(Context context) {
        //不管传入的Context是什么类型，都转为全局Context，避免内存泄露
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    public static SmsSendRecordHlper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (SmsSendRecordHlper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SmsSendRecordHlper(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //注意类型前面的空格，一定不能漏掉，否则当前列可能会创建失败
        String sql = "create table " + Constant.TABLE_NAME + " (" +
                " _id integer primary key autoincrement ," +
                Constant.COLUMN_DATE + " integer ," +
                Constant.COLUMN_FASTIVAL + " text ," +
                Constant.COLUMN_MSG + " text ," +
                Constant.COLUMN_NAME + " text ," +
                Constant.COLUMN_NUM + " text " +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
