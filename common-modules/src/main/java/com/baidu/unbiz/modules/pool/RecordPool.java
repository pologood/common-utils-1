package com.baidu.unbiz.modules.pool;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 默认的记录池
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:10:13
 */
public class RecordPool<T> extends LoggerSupport implements IRecordPool<T> {

    /**
     * 阻塞队列
     */
    private BlockingQueue<T> queue;

    /**
     * 批处理数
     */
    private int batchSize = 500;

    public RecordPool(int poolSize) {
        queue = new LinkedBlockingQueue<T>(poolSize);
    }

    @Override
    public boolean add(T rec) {
        if (rec == null) {
            logger.warn("object is null,ignore.");
            return false;
        }

        boolean ret = queue.offer(rec);
        if (!ret) {
            logger.warn("add the object to the queue failed, the queue may be full.");
        }

        return ret;
    }

    @Override
    public boolean add(T rec, long timeout, TimeUnit unit) throws InterruptedException {
        if (rec == null) {
            logger.warn("object is null,ignore.");
            return false;
        }

        boolean ret = queue.offer(rec, timeout, unit);
        if (!ret) {
            logger.warn("add the object to the queue failed, the queue may be full.");
        }

        return ret;
    }

    @Override
    public List<T> asList() {
        List<T> recordsCopy = CollectionUtil.createArrayList();
        if (queue.size() > 0) {
            int num = queue.size() >= batchSize ? batchSize : queue.size();
            queue.drainTo(recordsCopy, num);
        }

        return recordsCopy;
    }

    @Override
    public List<T> getWholeRecords() {
        List<T> recordsCopy = CollectionUtil.createArrayList();
        int num = queue.size();
        queue.drainTo(recordsCopy, num);

        return recordsCopy;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public int remainCapacity() {
        return this.queue.remainingCapacity();
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    public void setQueue(BlockingQueue<T> queue) {
        this.queue = queue;
    }

}
