/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.lang;

import java.util.concurrent.Callable;

import com.baidu.unbiz.common.ExceptionUtil;
import com.baidu.unbiz.common.able.Caller;

/**
 * 默认的<code>Caller</code>实现
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-9-16 上午1:07:20
 */
public class DefaultCaller<V> implements Caller<V> {

    private Callable<V> callable;

    public DefaultCaller(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public V call() {
        try {
            return callable.call();
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

}
