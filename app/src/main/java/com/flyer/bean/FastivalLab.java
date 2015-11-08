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
    }

    public List<FastivalBean> getFastivals() {
        //返回一个副本，防止源数据被修改
        return new ArrayList<>(list);
    }

    public FastivalBean getFastivalById(int id){
        for (FastivalBean fastivalBean : list){
            if (fastivalBean.getId() == id){
                return fastivalBean;
            }
        }
        return  null;
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