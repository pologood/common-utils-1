package com.baidu.unbiz.modules.bus;

/**
 * 总线发布代理
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:30:49
 */
public class BusPublisherProxy<T> extends AbstractBusMember<T> implements BusPublisher<T>, BusListener<T> {

    public BusPublisherProxy(BusRegistry<T> registry) {
        super(registry);
    }

    @Override
    public void receive(ActionData<T> event) throws ActionException {
        // nothing
    }

    @Override
    public void send(String bus, ActionData<T> data) {
        registry.send(bus, data);
    }

    /**
     * 发布
     * 
     * @param busName 总线类型
     * @param data 携带数据 @see ActionData
     * @param asyn 是否异步
     */
    public void send(String bus, ActionData<T> data, boolean asyn) {
        registry.send(bus, data, asyn);
    }

    /**
     * 发布
     * 
     * @param data 携带数据 @see ActionData
     */
    public void send(ActionData<T> data) {
        if (data == null) {
            logger.warn("send() null parameter");
            return;
        }

        final String bus = data.getBus();
        if (bus == null) {
            logger.warn("send() Logical bus not set.  The following data was not sent: {}", data.getPayload());
            return;
        }

        registry.send(bus, data);
    }

    /**
     * 发布
     * 
     * @param data 携带数据 @see ActionData
     * @param asyn 是否异步
     */
    public void send(ActionData<T> data, boolean asyn) {
        if (data == null) {
            logger.warn("send() null parameter");
            return;
        }

        final String bus = data.getBus();

        if (bus == null) {
            logger.warn("send() Logical bus not set.  The following data was not sent: {}", data.getPayload());
            return;
        }

        registry.send(bus, data, asyn);
    }

}
