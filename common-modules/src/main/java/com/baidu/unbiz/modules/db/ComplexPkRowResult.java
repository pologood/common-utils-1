/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import java.util.List;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.CastUtil;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.collection.ListMap;
import com.baidu.unbiz.common.lang.StringSerializable;

/**
 * 复合主键的行返回结果
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-9 上午1:11:18
 * @param <K>
 */
public class ComplexPkRowResult<K extends Comparable<? super K>> extends StringSerializable implements
        RowMap<ComplexKey<K>> {

    private static final long serialVersionUID = 1681857162421465686L;

    /**
     * 行结果中列名与列值的映射表
     */
    private ListMap<String, Object> map;

    /**
     * 主键名数组
     */
    private String[] idKeys;

    public ComplexPkRowResult(String[] idColumns, ListMap<String, Object> map) {
        this.idKeys = idColumns;
        this.map = map;
    }

    @Override
    public ComplexKey<K> getId() {
        if (ArrayUtil.isEmpty(idKeys)) {
            return null;
        }

        ComplexKey<K> complexKey = new ComplexKey<K>();
        int length = idKeys.length;
        List<Object> keys = CollectionUtil.createArrayList(length);
        for (int i = 0; i < length; i++) {
            keys.add(map.get(idKeys[i]));

        }

        List<K> cast = CastUtil.cast(keys);
        complexKey.setKeys(cast);
        return complexKey;
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
