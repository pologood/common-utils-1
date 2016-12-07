package com.baidu.unbiz.modules.bus;

import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;

/**
 * 消息总线管理器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-9 下午10:31:31
 */
public class BusSignalManager<T> {

    /**
     * 监听器与订阅者的映射表
     */
    private final Map<BusSignalListener<T>, BusSubscriberProxy<T>> listeners;

    /**
     * 消息发布者 @see BusPublisherProxy
     */
    private final BusPublisherProxy<T> publisher;

    /**
     * 总线监听器 @see BusRegistry
     */
    private final BusRegistry<T> registry;

    // @Inject
    public BusSignalManager(BusRegistry<T> registry) {
        listeners = CollectionUtil.createHashMap();
        publisher = registry.createPublisherProxy();
        this.registry = registry;
    }

    /**
     * 绑定消息
     * 
     * @param signalClass 消息类型
     * @param listener 监听器 @see BusSignalListener
     */
    public void bind(Class<?> signalClass, BusSignalListener<T> listener) {
        BusSubscriberProxy<T> proxy = listeners.get(listener);
        if (proxy == null) {
            proxy = registry.createSubscriberProxy(new BusListenerProxy(listener));
            listeners.put(listener, proxy);
        }

        proxy.bind(signalClass.getName());
    }

    /**
     * 绑定消息
     * 
     * @param bus 消息类型
     * @param listener 消息监听器 @see BusSignalListener
     */
    public void bind(String bus, BusSignalListener<T> listener) {
        BusSubscriberProxy<T> proxy = listeners.get(listener);
        if (proxy == null) {
            proxy = registry.createSubscriberProxy(new BusListenerProxy(listener));
            listeners.put(listener, proxy);
        }

        proxy.bind(bus);
    }

    /**
     * 解绑
     * 
     * @param signalClass 消息类型
     * @param listener 监听器 @see BusSignalListener
     */
    public void unbind(Class<?> signalClass, BusSignalListener<T> listener) {
        BusSubscriberProxy<T> proxy = listeners.get(listener);
        if (proxy != null) {
            proxy.unbind(signalClass.getName());

            /** If we are not registered on any buses we can free some objects in memory **/
            if (proxy.getRegisteredBuses().isEmpty()) {
                listeners.remove(listener);
            }
        }
    }

    /**
     * 解绑
     * 
     * @param bus 消息类型
     * @param listener 消息监听器 @see BusSignalListener
     */
    public void unbind(String bus, BusSignalListener<T> listener) {
        BusSubscriberProxy<T> proxy = listeners.get(listener);
        if (proxy != null) {
            proxy.unbind(bus);

            // If we are not registered on any buses we can free some objects in memory
            if (proxy.getRegisteredBuses().isEmpty()) {
                listeners.remove(listener);
            }
        }
    }

    /**
     * 发送信息
     * 
     * @param signal 携带信息
     */
    public void signal(T signal) {
        ActionData<T> data = new ActionData<T>();
        data.setPayload(signal);
        data.setBus(signal.getClass().getName());
        publisher.send(data, false);
    }

    /**
     * 发送信息
     * 
     * @param signal 携带信息
     * @param asyn 是否异步
     */
    public void signal(T signal, boolean asyn) {
        ActionData<T> data = new ActionData<T>();
        data.setPayload(signal);
        data.setBus(signal.getClass().getName());
        publisher.send(data, asyn);
    }

    /**
     * 发送信息
     * 
     * @param bus 消息类型
     * @param signal 发送数据
     */
    public void signal(String bus, T signal) {
        ActionData<T> data = new ActionData<T>();
        data.setPayload(signal);
        data.setBus(bus);
        publisher.send(data, false);
    }

    /**
     * 发送信息
     * 
     * @param bus 消息类型
     * @param signal 发送数据
     * @param asyn 是否异步
     */
    public void signal(String bus, T signal, boolean asyn) {
        ActionData<T> data = new ActionData<T>();
        data.setPayload(signal);
        data.setBus(bus);
        publisher.send(data, asyn);
    }

    /**
     * 清除工作
     */
    public void cleanup() {
        listeners.clear();
    }

    /**
     * 接听器代理
     * 
     * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
     * @version create on 2015-7-5 上午5:38:37
     */
    private class BusListenerProxy implements BusListener<T> {

        /**
         * 监听器 @see BusSignalListener
         */
        BusSignalListener<T> listener;

        public BusListenerProxy(BusSignalListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void receive(ActionData<T> event) throws ActionException {
            listener.signalFired(event.getPayload());
        }
    }

}
