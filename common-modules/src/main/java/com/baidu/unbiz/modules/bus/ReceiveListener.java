/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.bus;

import com.baidu.unbiz.common.able.Receiver;

/**
 * 消息接收器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-7 上午12:16:15
 */
public class ReceiveListener<T> implements BusSignalListener<T> {

    /**
     * 消息接收 @see Receiver
     */
    private Receiver<T> receiver;

    public ReceiveListener(BusRegistry<T> registry,Class<T> clazz) {
        registry.getSignalManager().bind(clazz, this);
    }

    @Override
    public void signalFired(T signal) {
        receiver.messageReceived(signal);
    }

    public void setReceiver(Receiver<T> receiver) {
        this.receiver = receiver;
    }

}
