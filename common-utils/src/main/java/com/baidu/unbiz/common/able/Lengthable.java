/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 定义长度接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-11-19 下午4:40:35
 */
public interface Lengthable {

    /**
     * Returns the number of elements . If this list contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     * 
     * @param array 集合可迭代对象
     * @return 长度、数量、大小
     */
    int length(Object array);

}
