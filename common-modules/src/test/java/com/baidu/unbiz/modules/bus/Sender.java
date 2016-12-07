package com.baidu.unbiz.modules.bus;

/**
 * Sender
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 下午2:03:53
 */
public class Sender {

    public Sender(BusSignalManager<Events> bsm) {
        this.bsm = bsm;
    }

    public void send() {
        bsm.signal(new Events("test"));
    }

    public void send(boolean asyn) {
        bsm.signal(new Events("test"), asyn);
    }

    private final BusSignalManager<Events> bsm;
}
