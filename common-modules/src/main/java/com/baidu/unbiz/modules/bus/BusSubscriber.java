package com.baidu.unbiz.modules.bus;

/**
 * 总线信息订阅
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:31:46
 */
public interface BusSubscriber<T> extends BusMember {

    /**
     * 消息接收
     * 
     * @param data 数据
     * @throws ActionException
     */
    public void receive(ActionData<T> data) throws ActionException;
}
