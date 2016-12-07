/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

import com.baidu.unbiz.common.CollectionUtil;

/**
 * 参照<code>ConcurrentMap</code>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-12-2 下午8:13:22
 */
public class ConcurrentSet<E> extends AbstractSet<E> implements Serializable {

    private static final long serialVersionUID = 7403583237380863822L;

    private final ConcurrentMap<E, Boolean> map;

    public ConcurrentSet() {
        map = CollectionUtil.createConcurrentMap();
    }

    public ConcurrentSet(ConcurrentMap<E, Boolean> map) {
        this.map = map;
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E o) {
        return map.putIfAbsent(o, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public void clear() {
        map.clear();
    }

}
