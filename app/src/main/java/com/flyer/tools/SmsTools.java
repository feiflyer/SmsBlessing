package com.flyer.tools;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import com.flyer.bean.Msg;
import com.flyer.bean.RecordBean;
import com.flyer.db.SmsProvider;
import com.flyer.ibs.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by liangchuanfei on 15/11/14.
 */
public class SmsTools {
    private Context mContext;

    public SmsTools(Context context) {
        mContext = context;
    }

    /**
     * 分割并发送短信
     *
     * @param toNum               单个联系人
     * @param sendPendingTnent
     * @param resultPendingIntent
     * @return 返回分割后的短信条数
     */
    public int sendMsg(String toNum, RecordBean recordBean, PendingIntent sendPendingTnent, PendingIntent resultPendingIntent) {

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(recordBean.getMsgContent());
        for (String sendContent : contents) {
            smsManager.sendTextMessage(toNum, null, sendContent, sendPendingTnent, resultPendingIntent);
        }
        save(recordBean);
        return contents.size();
    }


    public int sendMsg(Set<String> toNums, RecordBean recordBean, PendingIntent sendPendingTnent, PendingIntent resultPendingIntent) {

        int result = 0;
        for (String num : toNums) {
            int count = sendMsg(num, recordBean, sendPendingTnent, resultPendingIntent);
            result += count;
        }
        return result;
    }

    /**
     * 将发送相关记录到数据库
     *
     */
    private void save(RecordBean recordBean) {
        recordBean.setDate(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COLUMN_DATE ,recordBean.getDate().getTime());
        contentValues.put(Constant.COLUMN_FASTIVAL ,recordBean.getFastivalName());
        contentValues.put(Constant.COLUMN_MSG ,recordBean.getMsgContent());
        contentValues.put(Constant.COLUMN_NAME ,recordBean.getReceiverName());
        contentValues.put(Constant.COLUMN_NUM,recordBean.getMsgNum());
        mContext.getContentResolver().insert(SmsProvider.URI_SMS_ALL ,contentValues);
    }
}
