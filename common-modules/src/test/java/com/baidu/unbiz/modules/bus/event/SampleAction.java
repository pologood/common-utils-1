package com.baidu.unbiz.modules.bus.event;

import com.baidu.unbiz.modules.event.Action;
import com.baidu.unbiz.modules.event.Actionable;

/**
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2013-4-25 上午2:11:49
 */
public class SampleAction implements Actionable {

    @Action(event = "event.ben.sample")
    public void execute(SampleSignal signal) {
        System.out.println("execute:" + signal);
    }

    // @Action(event = "event.ben.sample")
    public void executeDuplicated(SampleSignal signal) {
        System.out.println("executeDuplicated:" + signal);
    }

}
