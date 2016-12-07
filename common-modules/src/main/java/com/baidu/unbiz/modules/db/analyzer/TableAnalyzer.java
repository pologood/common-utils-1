/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db.analyzer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.StringPool;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.db.Column;
import com.baidu.unbiz.modules.db.DBConstant;
import com.baidu.unbiz.modules.db.Table;

/**
 * 解析一个<code>Table</code>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-9 上午1:35:53
 */
public class TableAnalyzer extends LoggerSupport implements Analyzer<List<Table>>, DBConstant, StringPool.Symbol {

    /**
     * 维护所有表名与表对象的字典
     */
    private Map<String, Table> tableMap = CollectionUtil.createHashMap();

    @Override
    public List<Table> analyse(Connection connection) throws AnalyzerException {
        logger.info("begin to table analyse.");
        List<Table> tableList = CollectionUtil.createArrayList();

        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            // 获得所有表
            ResultSet rs = dbMeta.getTables(null, null, PERCENT, TABLE_TYPE);
            while (rs.next()) {
                // 表名
                String tableName = rs.getString(TABLE_NAME);
                logger.debug("analyse table=[{}]", tableName);

                Table table = createTable(dbMeta, tableName);
                tableList.add(table);
            }

        } catch (SQLException e) {
            logger.error("analyse table error", e);
            throw new AnalyzerException(e);
        }

        return tableList;
    }

    /**
     * 获取表<code>Table</code>
     * 
     * @param tableName 表名称
     * @return 表<code>Table</code>
     */
    public Table getTable(String tableName) {
        return tableMap.get(tableName);

    }

    /**
     * 创建表对象<code>Table</code>
     * 
     * @param dbMeta 数据库元信息 @see DatabaseMetaData
     * @param tableName 指定表名
     * @return 表对象 @see Table
     * @throws SQLException
     */
    private Table createTable(DatabaseMetaData dbMeta, String tableName) throws SQLException {
        Table table = new Table();
        table.setName(tableName);
        // 取得表中所有列
        ResultSet columnResultSet = dbMeta.getColumns(null, "%", tableName, "%");

        List<Column> columns = CollectionUtil.createArrayList();
        while (columnResultSet.next()) {
            Column column = createColumn(columnResultSet);
            columns.add(column);
        }

        table.setColumns(columns.toArray(new Column[0]));
        tableMap.put(table.getName(), table);

        table.setPrimaries(getTablePrimaries(dbMeta, tableName));
        return table;
    }

    /**
     * 创建列对象<code>Column</code>
     * 
     * @param columnResultSet 列结果集 @see ResultSet
     * @return 列对象 @see Column
     * @throws SQLException
     */
    private Column createColumn(ResultSet columnResultSet) throws SQLException {
        Column column = new Column();
        // 列名
        column.setName(columnResultSet.getString(COLUMN_NAME));
        // 列类型
        column.setType(columnResultSet.getString(COLUMN_TYPE));
        // 列长度
        column.setSize(columnResultSet.getInt(COLUMN_SIZE));
        // int digits = colRet.getInt("DECIMAL_DIGITS");
        // int nullable = colRet.getInt("NULLABLE");
        return column;
    }

    /**
     * 获取所有主键
     * 
     * @param dbMeta 数据库元信息 @see DatabaseMetaData
     * @param tableName 指定表名
     * @return 所有主键 @see Column
     * @throws SQLException
     */
    private Column[] getTablePrimaries(DatabaseMetaData dbMeta, String tableName) throws SQLException {
        ResultSet pkResultSet = dbMeta.getPrimaryKeys(null, null, tableName);
        List<Column> columns = CollectionUtil.createArrayList();
        Table table = tableMap.get(tableName);

        while (pkResultSet.next()) {
            String name = pkResultSet.getString(COLUMN_NAME);
            Column column = table.getColumn(name);
            columns.add(column);
        }
        // // FIXME
        // if (CollectionUtil.isEmpty(columns)) {
        // return new Column[] { table.getColumns()[0] };
        // }

        return columns.toArray(new Column[0]);
    }
}
