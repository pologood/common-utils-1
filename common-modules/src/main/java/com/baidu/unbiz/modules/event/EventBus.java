package com.baidu.unbiz.modules.event;

import com.baidu.unbiz.common.able.Closure;

/**
 * 事件总线
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-21 下午5:30:46
 */
public interface EventBus {

    /**
     * 注册
     * 
     * @param event 事件名称
     * @param closure ## @see Closure
     */
    void registerObserver(final String event, final Closure closure);

    /**
     * 注册
     * 
     * @param event 事件名称
     * @param target 注册对象
     * @param callback 回调方法
     */
    void registerObserver(String event, Object target, String callback);

    /**
     * 注册
     * 
     * @param event 事件名称
     * @param target 注册对象
     * @param callback 回调方法
     * @param parameterTypes 回调方法参数类型
     */
    void registerObserver(String event, Object target, String callback, Class<?>[] parameterTypes);

    /**
     * 触发事件
     * 
     * @param event 事件名称
     * @param inputs 传入参数 {@link Closure#execute(Object...)}
     */
    void fireEvent(String event, Object...inputs);
}
