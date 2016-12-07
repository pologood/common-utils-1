package com.baidu.unbiz.modules.db.analyzer;

import java.sql.Connection;

/**
 * DB解析器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-9 上午1:35:30
 */
public interface Analyzer<T> {

    /**
     * 解析操作
     * 
     * @param connection 标准JDBC连接
     * @return 解析结果
     * @throws AnalyzerException
     */
    public T analyse(Connection connection) throws AnalyzerException;

}
