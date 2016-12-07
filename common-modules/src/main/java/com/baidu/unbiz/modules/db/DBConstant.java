/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

/**
 * 数据库元信息常量
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-9 上午1:42:35
 */
public interface DBConstant {

    /**
     * 数据库中表分析类型Key
     */
    String[] TABLE_TYPE = { "TABLE" };

    /**
     * 表名Key
     */
    String TABLE_NAME = "TABLE_NAME";

    /**
     * 列表Key
     */
    String COLUMN_NAME = "COLUMN_NAME";

    /**
     * 列类型Key
     */
    String COLUMN_TYPE = "TYPE_NAME";

    /**
     * 列长度Key
     */
    String COLUMN_SIZE = "COLUMN_SIZE";

    // String COLUMN_DECIMAL_DIGITS = "DECIMAL_DIGITS";
    //
    // String COLUMN_NULLABLE = "NULLABLE";
}
