/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 通知
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-10-21 上午1:17:52
 */
public interface Notifier {

    /**
     * 将消息对象广播给所有对等实体
     * 
     * @param bean 消息对象
     */
    void notifyAll(Object bean);

    /**
     * 将消息对象广播给所有对等实体
     * 
     * @param callback 回调接口
     * @param bean 消息对象
     */
    <T> void notifyAll(ResponseClosure<T> callback, Object bean);

}
