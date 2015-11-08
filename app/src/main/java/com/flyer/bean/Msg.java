package com.flyer.bean;

/**
 * Created by liangchuanfei on 15/11/8.
 */
public class Msg {
    private int id;
    private int fastivalId;
    private String content;

    public int getId() {
        return id;
    }

    public int getFastivalId() {
        return fastivalId;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFastivalId(int fastivalId) {
        this.fastivalId = fastivalId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Msg(int id, int fastivalId, String content) {
        this.id = id;
        this.fastivalId = fastivalId;
        this.content = content;
    }
}
