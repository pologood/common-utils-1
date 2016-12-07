/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-4-3 下午2:24:00
 */
public interface ObjectFilter<T> {

    boolean accept(T object);

}
