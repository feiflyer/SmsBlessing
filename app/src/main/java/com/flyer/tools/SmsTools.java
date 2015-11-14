package com.flyer.tools;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by liangchuanfei on 15/11/14.
 */
public class SmsTools {

    /**
     * 分割并发送短信
     *
     * @param toNum               单个联系人
     * @param content
     * @param sendPendingTnent
     * @param resultPendingIntent
     * @return 返回分割后的短信条数
     */
    public int sendMsg(String toNum, String content, PendingIntent sendPendingTnent, PendingIntent resultPendingIntent) {

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(content);
        for (String sendContent : contents) {
            smsManager.sendTextMessage(toNum, null, sendContent, sendPendingTnent, resultPendingIntent);
        }
        return contents.size();
    }


    public int sendMsg(Set<String> toNums, String content, PendingIntent sendPendingTnent, PendingIntent resultPendingIntent) {

        int result = 0;
        for (String num : toNums) {
            int count = sendMsg(num, content, sendPendingTnent, resultPendingIntent);
            result += count;
        }
        return result;
    }
}
