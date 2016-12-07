/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import com.baidu.unbiz.common.collection.ListMap;
import com.baidu.unbiz.common.lang.StringSerializable;

/**
 * 代表数据库中表每行返回结果，适用单一主键
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-9 上午1:11:18
 * @param <K>
 */
public class RowResult<K> extends StringSerializable implements RowMap<K> {

    private static final long serialVersionUID = 8176909114581691253L;

    /**
     * 主键列名
     */
    private String idColumn;

    /**
     * 行结果中列名与值的映射表，<code>ListMap</code>维护内部顺序
     */
    private ListMap<String, Object> map;

    public RowResult(String idColumn, ListMap<String, Object> map) {
        this.idColumn = idColumn;
        this.map = map;
    }

    @Override
    public K getId() {
        @SuppressWarnings("unchecked")
        K id = (K) map.get(idColumn);

        return id;
    }

    @Override
    public ListMap<String, Object> getMap() {
        return map;
    }

    @Override
    public Object get(String column) {
        return map.get(column);
    }

}
