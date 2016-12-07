package com.baidu.unbiz.modules.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.able.Closure;
import com.baidu.unbiz.common.lang.Functor;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 默认的事件总线
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-21 下午5:30:10
 */
public class DefaultEventBus extends LoggerSupport implements EventBus {

    private ExecutorService mainExecutor;

    /**
     * 是否异步
     */
    private boolean asyn = false;

    /** key is event, value is <code>Closure</code> */
    private Map<String, ClosureSet> closureSets = CollectionUtil.createHashMap();

    /**
     * Set for Closure
     * 
     * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
     * @version create on 2015-7-5 上午6:18:20
     */
    private static class ClosureSet {
        /**
         * Closure列表 @see Closure
         */
        private List<Closure> closures = CollectionUtil.createArrayList();

        /**
         * 添加Closure @see Closure
         * 
         * @param closure 闭包
         */
        public void add(Closure closure) {
            closures.add(closure);
        }

        /**
         * 清除
         */
        public void clear() {
            closures.clear();
        }

        /**
         * 执行Closure @see Closure
         * 
         * @param args 携带参数
         */
        public void execute(Object...args) {
            for (Closure closure : closures) {
                closure.execute(args);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Closure closure : closures) {
                builder.append(closure.toString());
                builder.append(";");
            }

            return builder.toString();
        }
    }

    public DefaultEventBus() {
        this(Executors.newFixedThreadPool(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "ebus main threads");
            }
        }));
    }

    public DefaultEventBus(ExecutorService mainExecutor) {
        this.mainExecutor = mainExecutor;
    }

    @Override
    public void registerObserver(String event, Object target, String methodName, Class<?>[] parameterTypes) {
        registerObserver(event, new Functor(target, methodName, parameterTypes));
    }

    @Override
    public void registerObserver(String event, Object target, String methodName) {
        registerObserver(event, new Functor(target, methodName));
    }

    @Override
    public void registerObserver(final String event, final Closure closure) {
        getOrCreateClosureSet(event).add(closure);
    }

    @Override
    public void fireEvent(final String event, final Object...args) {
        // go
        if (!asyn) {
            doFireEvent(event, args);
            return;
        }

        this.mainExecutor.submit(new Runnable() {
            public void run() {
                doFireEvent(event, args);
            }
        });
    }

    /**
     * 获取等待事件处理数
     * 
     * @return 等待事件处理数
     */
    public int getPendingTaskCount() {
        if (this.mainExecutor instanceof ThreadPoolExecutor) {
            BlockingQueue<Runnable> queue = ((ThreadPoolExecutor) mainExecutor).getQueue();
            return queue.size();
        }

        throw new RuntimeException("Internal Error : mainExecutor is !NOT! ThreadPoolExecutor class");
    }

    public boolean isAsyn() {
        return asyn;
    }

    public void setAsyn(boolean asyn) {
        this.asyn = asyn;
    }

    /**
     * 触发事件
     * 
     * @param event 事件
     * @param args 事件携带参数
     */
    private void doFireEvent(final String event, final Object...args) {
        ClosureSet set = this.getClosure(event);
        if (null != set) {
            set.execute(args);
            set.clear();
        } else {
            logger.warn("event [{}] not found any matched closure!", event);
        }
    }

    /**
     * 获取或创建<code>Closure</code>集
     * 
     * @param event 事件类型
     * @return <code>Closure</code>集 @see ClosureSet
     */
    private ClosureSet getOrCreateClosureSet(String event) {
        ClosureSet set = closureSets.get(event);

        if (null == set) {
            set = new ClosureSet();
            closureSets.put(event, set);
        }

        return set;
    }

    private ClosureSet getClosure(String event) {
        return closureSets.get(event);
    }

}
