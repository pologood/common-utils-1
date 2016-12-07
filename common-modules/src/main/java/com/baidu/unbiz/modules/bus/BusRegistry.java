package com.baidu.unbiz.modules.bus;

/**
 * 消息总线注册器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:31:00
 */
public interface BusRegistry<T> {

    /**
     * 默认总线名称
     */
    String BUS_UNDEFINED = "BUS_UNDEFINED";

    /**
     * 绑定消息到总线
     * 
     * @param member 消息成员
     * @param busname 总线名称
     */
    public void bind(BusMember member, String busname);

    /**
     * 解绑
     * 
     * @param member 消息成员
     * @param busname 总线名称
     */
    public void unbind(BusMember member, String busname);

    /**
     * 发送消息
     * 
     * @param bus 总线名称
     * @param data 数据 @see ActionData
     */
    public void send(String bus, ActionData<T> data);

    /**
     * 发送消息
     * 
     * @param bus 总线名称
     * @param data 数据 @see ActionData
     * @param notifyConcurrent 是否异步
     */
    public void send(String bus, ActionData<T> data, boolean asyn);

    /**
     * 创建总线发布代理 @see BusPublisherProxy
     * 
     * @return 总线发布代理
     */
    public BusPublisherProxy<T> createPublisherProxy();

    /**
     * 创建总线发布代理 @see BusSubscriberProxy
     * 
     * @return 创建总线发布代理
     */
    public BusSubscriberProxy<T> createSubscriberProxy(BusListener<T> listener);

    /**
     * 获取消息总线管理器 @see BusSignalManager
     * 
     * @return 消息总线管理器
     */
    public BusSignalManager<T> getSignalManager();

}
