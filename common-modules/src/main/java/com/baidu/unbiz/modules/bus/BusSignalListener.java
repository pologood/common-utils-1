package com.baidu.unbiz.modules.bus;

/**
 * 总线监听器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:31:17
 */
public interface BusSignalListener<T> {

    /**
     * 发送数据
     * 
     * @param signal 数据
     */
    public void signalFired(T signal);
}
