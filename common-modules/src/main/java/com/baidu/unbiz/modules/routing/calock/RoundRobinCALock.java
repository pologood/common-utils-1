package com.baidu.unbiz.modules.routing.calock;

import java.util.concurrent.locks.Lock;

import com.baidu.unbiz.common.concurrent.CompositeFastPathLock;
import com.baidu.unbiz.modules.routing.SchedulingStrategy;
import com.baidu.unbiz.modules.routing.SchedulingSupport;

/**
 * 使用Composite Abortable Lock的轮询策略，提升并发性能
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:47:45
 */
public class RoundRobinCALock extends SchedulingSupport {

    /**
     * 当前索引号
     */
    private int index;

    /**
     * Composite Abortable Lock
     */
    private final Lock lock = new CompositeFastPathLock();

    @Override
    public void setTotal(int total) {
        this.total = total;
        index = 0;
    }

    @Override
    public int next() {
        lock.lock();
        try {
            int next = index++;
            // 溢出处理
            if (next < 0) {
                next = 0;
                index = next;
            }
            return next % total;
        } finally {
            lock.unlock();
        }

    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.COMPOSITE_ABORTABLE_RR;
    }

}
