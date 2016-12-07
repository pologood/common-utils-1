package com.baidu.unbiz.modules.routing;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:47:45
 */
public class RoundRobin extends SchedulingSupport {

    /**
     * 当前索引号，采用Atomic保证同步
     */
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public void setTotal(int total) {
        this.total = total;
        this.index.set(0);
    }

    @Override
    public int next() {
        int next = index.getAndIncrement();
        // 溢出处理
        if (next < 0) {
            next = 0;
            index.set(next);
        }
        return next % total;
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.ROUND_ROBIN;
    }

}
