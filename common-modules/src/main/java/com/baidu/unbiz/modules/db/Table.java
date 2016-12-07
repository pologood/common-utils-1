/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import java.util.Map;

import com.baidu.unbiz.biz.entity.Entity;
import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.able.Nameable;

/**
 * 数据库中的表
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-8 下午11:02:44
 */
public class Table extends Entity<Table> implements Comparable<Table>, Nameable {

    private static final long serialVersionUID = -8017564415435983663L;

    /**
     * 表名
     */
    private String name;

    /**
     * 表中列名
     */
    private Column[] columns;

    /**
     * 列名与列对象<code>Column</code>的映射表
     */
    private Map<String, Column> columnMap = CollectionUtil.createHashMap();

    /**
     * 主键的列名
     */
    private Column[] primaries;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        if (ArrayUtil.isEmpty(columns)) {
            return;
        }

        this.columns = columns;
        for (Column column : columns) {
            columnMap.put(column.getName(), column);
        }
    }

    public Column[] getPrimaries() {
        return primaries;
    }

    // FIXME 联合主键顺序应该有其他信息元信息可获取，先这样
    /**
     * 设置主键信息
     * 
     * @param primaries 主键 数组
     */
    public void setPrimaries(Column[] primaries) {
        this.primaries = new Column[primaries.length];
        for (int i = primaries.length - 1, j = 0; i >= 0; i--, j++) {
            this.primaries[j] = primaries[i];
        }

    }

    public Column getColumn(String name) {
        return columnMap.get(name);
    }

    @Override
    protected boolean isEquals(Table obj) {
        return this.name.equals(obj.name);
    }

    @Override
    protected Object hashKey() {
        return name;
    }

    /**
     * 是否为复合主键
     * 
     * @return 如果是则返回<code>true</code>
     */
    public boolean isComplexPrimary() {
        return primaries != null && primaries.length > 1;
    }

    /**
     * 是否存在主键
     * 
     * @return 如果是则返回<code>true</code>
     */
    public boolean hasPrimary() {
        return ArrayUtil.isNotEmpty(primaries);
    }

    @Override
    public int compareTo(Table o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
