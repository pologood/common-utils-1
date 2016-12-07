package com.baidu.unbiz.modules.bus;

import org.junit.Test;

/**
 * Bus test
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 下午2:02:35
 */
public class BusTest {

    @Test
    public void test() {
        BusRegistry<Events> busRegistry = new DefaultBusRegistry<Events>();
        new Receiver(busRegistry.getSignalManager());

        Sender sender = new Sender(busRegistry.getSignalManager());
        sender.send(true);
        System.out.println("done");
    }

}
