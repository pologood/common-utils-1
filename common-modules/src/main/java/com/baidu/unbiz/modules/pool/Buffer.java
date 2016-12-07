/**
 * 
 */
package com.baidu.unbiz.modules.pool;

import java.util.concurrent.TimeUnit;

import com.baidu.unbiz.common.able.Service;

/**
 * 缓冲池，用于批处理
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:06:27
 */
public interface Buffer<T> extends Service {

    /**
     * 名称
     * 
     * @return 名称
     */
    String getName();

    /**
     * 添加记录
     * 
     * @param record 记录
     * @return 是否成功
     */
    boolean add(T record);

    /**
     * 记录池中添加记录
     * 
     * @param record 记录
     * @param timeout 超时
     * @param unit 超时单位
     * @return 是否成功
     */
    boolean add(T record, long timeout, TimeUnit unit);

}
