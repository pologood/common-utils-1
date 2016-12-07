package com.baidu.unbiz.modules.pool;

import java.util.List;

/**
 * 批处理执行器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:10:05
 */
public interface IBatchExecutor<T> {

    /**
     * 执行操作
     * 
     * @param records 记录列表
     */
    void execute(List<T> records);

}
