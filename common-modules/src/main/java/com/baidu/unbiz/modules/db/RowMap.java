/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import com.baidu.unbiz.common.able.Keyable;
import com.baidu.unbiz.common.collection.ListMap;

/**
 * 行映射
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-4-16 上午12:06:15
 */
public interface RowMap<K> extends Keyable<K> {

    /**
     * 获取行数据
     * 
     * @return 行数据
     */
    ListMap<String, Object> getMap();

    /**
     * 获取行中列数据
     * 
     * @param column 列名称
     * @return 列数据
     */
    Object get(String column);

}
