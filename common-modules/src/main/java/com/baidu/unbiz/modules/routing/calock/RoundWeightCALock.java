package com.baidu.unbiz.modules.routing.calock;

import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.concurrent.CompositeFastPathLock;
import com.baidu.unbiz.modules.routing.SchedulingStrategy;
import com.baidu.unbiz.modules.routing.SchedulingSupport;

/**
 * 根据服务权重数轮询路由，使用Composite Abortable Lock，提升并发性能
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:49:49
 */
public class RoundWeightCALock extends SchedulingSupport {

    /**
     * 当前索引号
     */
    private int index;

    /**
     * 服务索引到权重的映射表
     */
    private Map<Integer, Integer> weightsMap;

    /**
     * Composite Abortable Lock
     */
    private final Lock lock = new CompositeFastPathLock();

    @Override
    public int next() {
        lock.lock();
        try {
            int next = index++;
            // 溢出处理
            if (next < 0) {
                next = 0;
                index = 0;
            }
            return weightsMap.get(next % total);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getTotal() {
        return weightsMap.size();
    }

    /**
     * 通过服务权重数初始化 {@link #total} 和 {@link #weightsMap}
     * 
     * @param weights 权重数组
     */
    public void setWeights(int[] weights) {
        int length = weights.length;

        weightsMap = CollectionUtil.createHashMap(length);
        int index = 0;

        for (int i = 0; i < length; i++) {
            int weight = weights[i];
            total += weight;
            int max = index + weight;
            for (; index < max; index++) {
                weightsMap.put(index, i);
            }
        }
    }

    @Override
    public void setTotal(int total) {
        // ignore
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.COMPOSITE_ABORTABLE_WEIGHT;
    }

}
