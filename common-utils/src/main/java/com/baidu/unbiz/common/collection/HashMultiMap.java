/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.baidu.unbiz.common.CollectionUtil;

/**
 * HashMultiMap
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016-1-19 下午8:14:29
 * @param <V>
 */
public class HashMultiMap<K, V> extends HashMap<K, Collection<V>> implements MultiMap<K, V> {

    private static final long serialVersionUID = -7903494719704524128L;

    private final Map<K, Set<V>> container = CollectionUtil.createHashMap();

    @Override
    public Collection<V> get(Object key) {
        Collection<V> value = container.get(key);
        if (value == null) {
            value = CollectionUtil.createArrayList();
        }

        return value;
    }

    @Override
    public boolean putItem(K key, V value) {
        return get(key).add(value);
    }

    @Override
    public boolean containsEntry(K key, V value) {
        Collection<V> collection = container.get(key);
        return collection != null && collection.contains(value);
    }

}
