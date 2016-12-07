/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.lang;

import java.io.Serializable;

import com.baidu.unbiz.common.able.Identifiable;

/**
 * 一个消息体
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-6 下午6:05:48
 */
public class Message<T> extends DefaultProperty implements Identifiable<String>, Serializable {

    private static final long serialVersionUID = -7115139569553613570L;

    private static final UUID UID = new UUID();

    /**
     * 唯一标识
     */
    protected String uid;

    /**
     * 消息体内容
     */
    protected T body;

    public Message() {
        uid = UID.nextID();
    }

    public Message(T body) {
        uid = UID.nextID();
        this.body = body;
    }

    @Override
    public String getIdentification() {
        return uid;
    }

    @Override
    public void setIdentification(String uid) {
        this.uid = uid;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
