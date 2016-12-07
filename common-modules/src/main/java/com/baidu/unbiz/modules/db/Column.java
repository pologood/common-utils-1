/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import com.baidu.unbiz.biz.entity.Entity;

/**
 * 数据库表中的列
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-3 下午5:10:07
 */
public class Column extends Entity<Column> {

    /**
     * 
     */
    private static final long serialVersionUID = 433115844846807053L;

    /**
     * 列名
     */
    private String name;

    /**
     * 列类型
     */
    private String type;

    /**
     * 列最大长度
     */
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    protected boolean isEquals(Column obj) {
        return this.name.equals(obj.name);
    }

    @Override
    protected Object hashKey() {
        return name;
    }

}
