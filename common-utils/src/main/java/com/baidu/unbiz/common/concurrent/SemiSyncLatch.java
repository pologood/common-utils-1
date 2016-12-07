/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.concurrent;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.baidu.unbiz.common.ConcurrentUtil;
import com.baidu.unbiz.common.able.Stopable;
import com.baidu.unbiz.common.lang.DefaultCaller;
import com.baidu.unbiz.common.lang.FutureResponse;
import com.baidu.unbiz.common.lang.SimpleThreadFactory;

/**
 * 半同步的门闩
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-9-15 下午11:59:49
 */
public class SemiSyncLatch implements Stopable {

    private ExecutorService executor;

    public SemiSyncLatch() {
        executor = Executors.newCachedThreadPool(new SimpleThreadFactory("SemiSyncLatch"));
    }

    public SemiSyncLatch(ExecutorService executor) {
        this.executor = executor;
    }

    public <T> T executeAndWait(Collection<Callable<T>> callables) throws InterruptedException, ExecutionException {
        final FutureResponse<T> futureResponse = new FutureResponse<T>();
        for (final Callable<T> callable : callables) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    futureResponse.set(new DefaultCaller<T>(callable).call());
                }
            });
        }

        return futureResponse.get();
    }

    public <T> T executeAndWait(final Callable<T> callable, int workers) throws InterruptedException,
            ExecutionException {
        if (workers <= 0) {
            throw new IllegalArgumentException("workers must > 0");
        }

        final FutureResponse<T> futureResponse = new FutureResponse<T>();
        for (int i = 0; i < workers; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    futureResponse.set(new DefaultCaller<T>(callable).call());
                }
            });
        }

        return futureResponse.get();
    }

    public <T> T executeAndWait(Collection<Callable<T>> callables, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        final FutureResponse<T> futureResponse = new FutureResponse<T>();
        for (final Callable<T> callable : callables) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    futureResponse.set(new DefaultCaller<T>(callable).call());
                }
            });
        }

        return futureResponse.get();
    }

    public <T> T executeAndWait(final Callable<T> callable, int workers, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (workers <= 0) {
            throw new IllegalArgumentException("workers must > 0");
        }

        final FutureResponse<T> futureResponse = new FutureResponse<T>();
        for (int i = 0; i < workers; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    futureResponse.set(new DefaultCaller<T>(callable).call());
                }
            });
        }

        return futureResponse.get();
    }

    @Override
    public void stop() {
        ConcurrentUtil.shutdownAndAwaitTermination(executor);
    }
}
