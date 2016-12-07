/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import com.baidu.unbiz.common.able.CloneableObject;
import com.baidu.unbiz.common.lang.StringSerializable;

/**
 * 表中每行记录的元信息
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-13 上午12:31:40
 */
public class RowMeta extends StringSerializable implements CloneableObject<RowMeta> {

    private static final long serialVersionUID = -3676563571196478730L;

    /**
     * 表名
     */
    private String tableName;

    private String sql;

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * JDBC连接地址
     */
    private String url;

    /**
     * 分片号 FIXME 先放这
     */
    private int partionNum = -1;

    public RowMeta(String dbName, String url) {
        this.dbName = dbName;
        this.url = url;
    }

    public RowMeta(String tableName, String dbName, String url) {
        this.tableName = tableName;
        this.dbName = dbName;
        this.url = url;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUrl() {
        return url;
    }

    public int getPartionNum() {
        return partionNum;
    }

    public void setPartionNum(int partionNum) {
        this.partionNum = partionNum;
    }

    @Override
    public RowMeta clone() {
        RowMeta cloned = new RowMeta(tableName, dbName, url);

        return cloned;
    }

}
