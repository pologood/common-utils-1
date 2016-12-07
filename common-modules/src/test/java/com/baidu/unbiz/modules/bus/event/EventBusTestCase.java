package com.baidu.unbiz.modules.bus.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baidu.unbiz.common.able.Receiver;

/**
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2013-4-25 上午2:11:39
 */
public class EventBusTestCase {
    private AbstractApplicationContext ctx;

    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(new String[] { "spring/eventBus.xml" });
    }

    @After
    public void tearDown() throws Exception {
        ctx = null;
    }

    @Test
    public void testFireEvent() {

        SampleSignal signal = new SampleSignal();
        signal.setIntField(1);
        signal.setShortField((byte) 1);
        signal.setByteField((byte) 1);
        signal.setLongField(1L);
        signal.setStringField("中文");

        signal.setByteArrayField(new byte[] { 127 });

        @SuppressWarnings("unchecked")
        Receiver<SampleSignal> closure = (Receiver<SampleSignal>) ctx.getBean("eventDispatchClosure");
        closure.messageReceived(signal);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
