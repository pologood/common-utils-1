package com.baidu.unbiz.modules.bus;

/**
 * Receiver
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 下午2:03:27
 */
public class Receiver implements BusSignalListener<Events> {

    public Receiver(BusSignalManager<Events> bsm) {
        bsm.bind(Events.class, this);
    }

    @Override
    public void signalFired(Events signal) {
        System.out.println("signal-->" + signal.getName());
    }

}
