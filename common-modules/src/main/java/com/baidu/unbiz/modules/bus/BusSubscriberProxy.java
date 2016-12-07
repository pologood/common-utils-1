package com.baidu.unbiz.modules.bus;

/**
 * 订阅代理
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:32:12
 */
public class BusSubscriberProxy<T> extends AbstractBusMember<T> implements BusSubscriber<T> {

    /**
     * 消息接听器
     */
    protected BusListener<T> listener;

    public BusSubscriberProxy(BusListener<T> listener, BusRegistry<T> registry) {
        super(listener, registry);
    }

    @Override
    public void receive(ActionData<T> data) throws ActionException {
        final BusListener<T> listener = getBusListener();
        if (listener != null) {
            listener.receive(data);
        }
    }

}
