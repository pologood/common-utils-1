/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db.analyzer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.db.DBConstant;
import com.baidu.unbiz.modules.db.Database;
import com.baidu.unbiz.modules.db.Table;

/**
 * 解析一个<code>Database</code>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-6 下午1:44:21
 */
public class DatabaseAnalyzer extends LoggerSupport implements Analyzer<Database>, DBConstant {

    @Override
    public Database analyse(Connection connection) throws AnalyzerException {
        logger.info("begin to database analyse.");
        Database database = new Database();

        try {
            setMeta(database, connection);

            database.setTables(getTables(connection));
        } catch (SQLException e) {
            logger.error("analyse database error", e);
            throw new AnalyzerException(e);
        }

        return database;
    }

    /**
     * 设置数据库元信息
     * 
     * @param database 数据库对象 @see Database
     * @param connection 标准JDBC连接 @see Connection
     * @throws SQLException
     */
    private void setMeta(Database database, Connection connection) throws SQLException {
        DatabaseMetaData dbMeta = connection.getMetaData();
        // 数据库产品名
        database.setProductName(dbMeta.getDatabaseProductName());
        // JDBC地址
        database.setUrl(dbMeta.getURL());
        // 数据库产品版本
        database.setVersion(dbMeta.getDatabaseProductVersion());
        // 数据库名
        database.setName(connection.getCatalog());
    }

    /**
     * 获得<code>connection</code>下所有表
     * 
     * @param connection 标准JDBC连接 @see Connection
     * @return 所有表
     * @throws AnalyzerException
     */
    private List<Table> getTables(Connection connection) throws AnalyzerException {
        Analyzer<List<Table>> tableAnalyzer = new TableAnalyzer();

        return tableAnalyzer.analyse(connection);
    }

}
