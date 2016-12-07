/**
 * 
 */
package com.baidu.unbiz.common;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.common.able.Closure;

/**
 * 并发相关的一些常用工具类
 * 
 * <p>
 * 这个类中的每个方法都可以“安全”地处理 <code>null</code> ，而不会抛出 <code>NullPointerException</code>。
 * </p>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月15日 下午4:07:30
 */
public abstract class ConcurrentUtil {

    /**
     * 默认超时时间，1分钟
     */
    private static final int DEFAULT_TIMEOUT = 60;

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentUtil.class);

    /**
     * 在1分钟内关闭<code>executorService</code>，如果不能正常结束，将强制关闭。
     * <p>
     * 如果<code>executorService</code>不存在或已关闭，将不做任何处理。
     * 
     * @param executorService ## @see ExecutorService
     */
    public static final void shutdownAndAwaitTermination(ExecutorService executorService) {

        shutdownAndAwaitTermination(executorService, DEFAULT_TIMEOUT, TimeUnit.SECONDS);

    }

    /**
     * 关闭<code>executorService</code>，如果<code>executorService</code> 不存在或已关闭，将不做任何处理。在限定时间内如无法正常结束，将强制关闭。
     * 
     * @param executorService ## @see ExecutorService
     * @param timeout 限定时间
     * @param timeUnit 单位 @see TimeUnit
     */
    public static final void shutdownAndAwaitTermination(ExecutorService executorService, long timeout,
            TimeUnit timeUnit) {
        if (isShutDown(executorService)) {
            return;
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(timeout, timeUnit)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        executorService.shutdownNow();

    }

    /**
     * <code>executorService</code>是否已关闭，如果<code>executorService</code>不存在，也当做“已关闭”对待
     * 
     * @param executorService ## @see ExecutorService
     * @return 如果<code>executorService</code>“已关闭”，则返回<code>true</code>
     */
    public static boolean isShutDown(ExecutorService executorService) {
        return (executorService == null || executorService.isShutdown());
    }

    public static void execute(boolean asyn, Runnable runnable) {
        if (asyn) {
            asynExecute(runnable);
            return;
        }

        if (runnable == null) {
            return;
        }
        runnable.run();
    }

    public static void execute(boolean asyn, final Object object, final String methodName, final Object...inputs) {
        if (asyn) {
            asynExecute(object, methodName, inputs);
            return;
        }

        if (object == null || StringUtil.isBlank(methodName)) {
            return;
        }
        doExecute(object, methodName, inputs);
    }

    public static void execute(boolean asyn, final Closure closure, final Object...inputs) {
        if (asyn) {
            asynExecute(closure, inputs);
        }

        if (closure == null) {
            return;
        }
        closure.execute(inputs);
    }

    public static void asynExecute(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        new Thread(runnable).start();
    }

    public static void asynExecute(final Object object, final String methodName, final Object...inputs) {
        if (object == null || StringUtil.isBlank(methodName)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                doExecute(object, methodName, inputs);
            }

        }).start();
    }

    public static void asynExecute(final Closure closure, final Object...inputs) {
        if (closure == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                closure.execute(inputs);
            }

        }).start();
    }

    private static void doExecute(final Object object, final String methodName, final Object...inputs) {
        Class<?>[] parameterTypes = null;
        if (ArrayUtil.isEmpty(inputs)) {
            parameterTypes = new Class<?>[0];
        } else {
            int size = inputs.length;
            parameterTypes = new Class<?>[size];
            for (int i = 0; i < size; i++) {
                parameterTypes[i] = inputs[i].getClass();
            }
        }

        ReflectionUtil.invokeMethod(object, methodName, parameterTypes, inputs);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("InterruptedException error", e);
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepUninterruptibly(long recoveryInterval, TimeUnit unit) {
        if (unit == null) {
            return;
        }

        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(recoveryInterval);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.sleep() treats negative timeouts just like zero.
                    NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void park(long sleepTime, TimeUnit unit) {
        if (unit == null) {
            return;
        }

        long sleepTimeNanos = TimeUnit.NANOSECONDS.convert(sleepTime, unit);
        LockSupport.parkNanos(sleepTimeNanos);
    }

    public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        if (future == null) {
            return null;
        }

        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ====================== join =============================

    public static void join(Thread thread) {
        if (thread == null) {
            return;
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.error("InterruptedException error", e);
            thread.interrupt();
        }
    }

    public static void join(Thread thread, long millis) {
        if (thread == null) {
            return;
        }

        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            logger.error("InterruptedException error", e);
            thread.interrupt();
        }
    }

    public static void join(Thread thread, long millis, int nanos) {
        if (thread == null) {
            return;
        }

        try {
            thread.join(millis, nanos);
        } catch (InterruptedException e) {
            logger.error("InterruptedException error", e);
            thread.interrupt();
        }
    }

}
