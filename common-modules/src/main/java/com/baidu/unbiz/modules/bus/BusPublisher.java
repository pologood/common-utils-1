package com.baidu.unbiz.modules.bus;

/**
 * 发布主题消息
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:30:18
 */
public interface BusPublisher<T> {
    /**
     * 发布
     * 
     * @param busName 总线类型
     * @param data 携带数据 @see ActionData
     */
    public void send(String busName, ActionData<T> data);
}
