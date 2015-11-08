package com.flyer.bean;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangchuanfei on 15/11/8.
 * <p/>
 * 单例，模仿短信数据
 */
public class FastivalLab {

    private static FastivalLab mInstance;
    private List<FastivalBean> list = new ArrayList<>();
    private List<Msg> msgList = new ArrayList<>();
    public static FastivalBean mFastivalBean;

    private FastivalLab() {
        list.add(new FastivalBean(1, "国庆节"));
        list.add(new FastivalBean(2, "中秋节"));
        list.add(new FastivalBean(3, "元旦"));
        list.add(new FastivalBean(4, "春节"));
        list.add(new FastivalBean(5, "端午节"));
        list.add(new FastivalBean(6, "七夕节"));
        list.add(new FastivalBean(7, "圣诞节"));
        list.add(new FastivalBean(8, "儿童节"));

        msgList.add(new Msg(1, 1, "加入我有一百万，" +
                "我将送你999999，我有一百万吗？我没有，所以我智能花一毛钱给你发一条短信，" +
                "祝你节日快乐"));

        msgList.add(new Msg(2, 1, "可记得那年红旗飘扬，" +
                "我们豪情万状，为祖国发下宏愿，为此不惜一切。。。。。"));

        msgList.add(new Msg(3, 1, "不要让问候沉默，" +
                "记得送一份祝福给你的朋友" +
                "祝你节日快乐"));

        msgList.add(new Msg(5, 1, "国庆节你给我送什么礼物，" +
                "其实一个微笑一个祝福就够了。" +
                "祝你节日快乐"));

        msgList.add(new Msg(6, 1, "天蓝蓝，草青青，假日心温馨，" +
                "祝你节日快乐"));

        msgList.add(new Msg(7, 1, "加入我有一百万，" +
                "朋友一生一起走，哪些日子不再有，一辈子，一生情铭记在心，" +
                "祝你节日快乐"));

        msgList.add(new Msg(8, 1, "今天觉得自己异常心动，总想对你说点什么" +
                "祝你节日快乐"));

        msgList.add(new Msg(9, 1, "别问为什么，把祝福传递下去，收获温暖，收获情谊。" +
                "祝你节日快乐"));

    }

    public List<Msg> getMsgByFastivalId(int id) {
        List<Msg> list = new ArrayList<>();
        for (Msg msg : msgList) {
            if (msg.getFastivalId() == id) {
                list.add(msg);
            }
        }
        return list;
    }

    public Msg getMsgById(int id) {
        for (Msg msg : msgList) {
            if (msg.getId() == id) {
                return msg;
            }
        }
        return null;

    }

    public List<FastivalBean> getFastivals() {
        //返回一个副本，防止源数据被修改
        return new ArrayList<>(list);
    }

    public FastivalBean getFastivalById(int id) {
        for (FastivalBean fastivalBean : list) {
            if (fastivalBean.getId() == id) {
                return fastivalBean;
            }
        }
        return null;
    }

    public static FastivalLab getInstance() {
        if (mInstance == null) {
            synchronized (FastivalLab.class) {
                if (mInstance == null) {
                    mInstance = new FastivalLab();
                }

            }
        }
        return mInstance;
    }

}