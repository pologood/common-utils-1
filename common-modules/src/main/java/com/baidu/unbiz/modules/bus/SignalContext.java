/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.bus;

/**
 * 信号信息上下文
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-6-3 下午8:20:13
 */
public class SignalContext<T> {

    /**
     * 消息总线注册器 @see BusRegistry
     */
    private BusRegistry<T> registry;

    /**
     * 注册
     * 
     * @param clazz 类型
     * @param listeners 监听器列表 @see BusSignalListener
     */
    public void registerSignal(Class<?> clazz, BusSignalListener<T>...listeners) {
        for (BusSignalListener<T> listerner : listeners) {
            registry.getSignalManager().bind(clazz, listerner);
        }
    }

    /**
     * 注册
     * 
     * @param listeners 监听器列表 @see BusSignalListener
     * @param classes 类型数组
     */
    public void registerSignal(BusSignalListener<T> listerner, Class<?>...classes) {
        for (Class<?> clazz : classes) {
            registry.getSignalManager().bind(clazz, listerner);
        }
    }

    public void setRegistry(BusRegistry<T> registry) {
        this.registry = registry;
    }

}
