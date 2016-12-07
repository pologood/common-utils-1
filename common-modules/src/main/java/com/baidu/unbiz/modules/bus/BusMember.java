package com.baidu.unbiz.modules.bus;

import java.util.List;

/**
 * 代表一个总线成员
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-9 下午10:30:08
 */
public interface BusMember {
    /**
     * 绑定消息
     * 
     * @param busName 消息类型
     */
    public void bind(String busName);

    /**
     * 取消绑定
     * 
     * @param busName 消息类型
     */
    public void unbind(String busName);

    /**
     * 获取所有注册消息
     * 
     * @return 所有注册消息
     */
    public List<String> getRegisteredBuses();

    /**
     * 处理器
     * 
     * @return 处理器
     */
    public Object getMemberHandle();

    /**
     * 设置处理器
     * 
     * @param handle 处理器
     */
    public void setMemberHandle(Object handle);
}
