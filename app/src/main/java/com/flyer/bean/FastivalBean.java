package com.flyer.bean;

import java.util.Date;

/**
 * Created by liangchuanfei on 15/11/8.
 */
public class FastivalBean {
    private int id;
    private String name;

    public FastivalBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
