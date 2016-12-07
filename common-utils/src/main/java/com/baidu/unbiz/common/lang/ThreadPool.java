/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.lang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.unbiz.common.logger.CachedLogger;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-12 下午10:54:25
 */
public class ThreadPool extends CachedLogger {

    private static ThreadPool instance = new ThreadPool();

    private final ExecutorService pool;

    private ThreadPool() {
        pool = Executors.newCachedThreadPool(new SimpleThreadFactory("ThreadPool"));
    }

    public static ThreadPool getInstance() {
        return instance;
    }

    public void execute(String name, Runnable runnable) {
        logger.debug("Executing thread: " + name);
        try {
            pool.execute(runnable);
        } catch (RuntimeException e) {
            logger.error("Trapped exception on thread: " + name, e);
        }
    }

}
