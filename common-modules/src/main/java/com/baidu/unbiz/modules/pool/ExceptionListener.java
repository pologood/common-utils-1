package com.baidu.unbiz.modules.pool;

import java.util.List;

/**
 * 异常监听器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:07:35
 */
public interface ExceptionListener<T> {

    /**
     * 异常处理
     * 
     * @param records 记录列表
     */
    void onException(List<T> records);

}
