package com.baidu.unbiz.dynamic.proxy.sample;

import java.io.Serializable;

public abstract class AbstractEcho implements Echo, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1183412961644181457L;

    @Override
    public String echoBack(String message) {
        return message;
    }
}
