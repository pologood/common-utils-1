/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 装饰用
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-6-17 下午4:04:20
 */
public interface Decoratable<T> {

    T get();

    void set(T t);

}
