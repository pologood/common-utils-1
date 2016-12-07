/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.pool;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 记录池接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-5 下午8:20:08
 */
public interface IRecordPool<T> {

    /**
     * 记录池中添加记录
     * 
     * @param rec 记录
     * @return 是否添加成功
     */
    boolean add(T rec);

    /**
     * 记录池中添加记录
     * 
     * @param rec 记录
     * @param timeout 超时
     * @param unit 超时单位
     * @return 是否添加成功
     * @throws InterruptedException
     */
    boolean add(T rec, long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 转换成List
     * 
     * @return List
     */
    List<T> asList();

    /**
     * 获取所有记录
     * 
     * @return 所有记录
     */
    List<T> getWholeRecords();

    /**
     * 记录池剩余容量
     * 
     * @return 剩余容量
     */
    int remainCapacity();

    /**
     * 记录池大小
     * 
     * @return 记录池大小
     */
    int size();

    /**
     * 设置批次处理数
     * 
     * @param batchSize 批次处理数
     */
    void setBatchSize(int batchSize);

}
