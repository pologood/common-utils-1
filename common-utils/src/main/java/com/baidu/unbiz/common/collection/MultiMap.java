/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.collection;

import java.util.Collection;
import java.util.Map;

/**
 * MultiMap
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016-1-19 下午8:35:08
 */
public interface MultiMap<K, V> extends Map<K, Collection<V>> {

    boolean putItem(K key, V value);

    boolean containsEntry(K key, V value);

}
