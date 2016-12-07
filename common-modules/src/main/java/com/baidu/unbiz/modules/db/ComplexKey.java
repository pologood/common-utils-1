/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import java.util.List;

import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * 复合主键
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-4-14 下午7:36:40
 */
public class ComplexKey<T extends Comparable<? super T>> extends StringableSupport 
        implements Comparable<ComplexKey<T>> {

    /**
     * 主键列表
     */
    private List<T> keys;

    @Override
    public int compareTo(ComplexKey<T> o) {
        for (int i = 0, size = keys.size(); i < size; i++) {
            T thisKey = keys.get(i);
            T otherKey = o.keys.get(i);
            int result = thisKey.compareTo(otherKey);
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }

    public List<T> getKeys() {
        return keys;
    }

    public void setKeys(List<T> keys) {
        this.keys = keys;
    }

}
