/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

import java.util.concurrent.Callable;

/**
 * 无抛出异常的“Callable”
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-11 下午3:29:31
 */
public interface Caller<V> extends Callable<V> {

    /**
     * Computes a result
     * 
     * @return computed result
     */
    @Override
    V call();

}
