package com.flyer.bean;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liangchuanfei on 15/11/14.
 *
 */
public class RecordBean {
    private int id;
    private String msgContent;
    private String msgNum;
    private String receiverName;
    private String fastivalName;
    private String dateString;
    private Date mDate;
    private DateFormat mDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDateString() {
        dateString = mDateFormat.format(mDate);
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getFastivalName() {
        return fastivalName;
    }

    public void setFastivalName(String fastivalName) {
        this.fastivalName = fastivalName;
    }
}
