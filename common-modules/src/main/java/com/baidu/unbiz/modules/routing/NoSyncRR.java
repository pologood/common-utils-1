package com.baidu.unbiz.modules.routing;

/**
 * 不进行同步化的RoundRobin
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:45:43
 */
public class NoSyncRR extends SchedulingSupport {

    /**
     * 当前索引号
     */
    private int index;

    @Override
    public void setTotal(int total) {
        this.total = total;
        index = 0;
    }

    @Override
    public int next() {
        int next = index++;
        // 溢出处理
        if (next < 0) {
            next = 0;
            index = next;
        }
        return next % total;
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.NO_SYNC_RR;
    }

}
