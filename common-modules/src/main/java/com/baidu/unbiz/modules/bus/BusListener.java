package com.baidu.unbiz.modules.bus;

/**
 * 消息接听器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:29:54
 */
public interface BusListener<T> {
    /**
     * 接收消息
     * 
     * @param event 事件 @see ActionData
     * @throws ActionException
     */
    public void receive(ActionData<T> event) throws ActionException;
}
