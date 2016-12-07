package com.baidu.unbiz.modules.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.able.Receiver;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 事件处理分发器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-21 下午5:31:00
 */
public class DispatchClosure extends LoggerSupport implements Receiver<Object> {

    /**
     * 事件总线 @see EventBus
     */
    private EventBus eventBus;

    private final Map<Class<?>, String> events = CollectionUtil.createHashMap();

    /**
     * 获取事件类型集
     * 
     * @return 事件类型集
     */
    public Collection<String> getTypeEvents() {
        Collection<String> ret = CollectionUtil.createArrayList();

        for (Map.Entry<Class<?>, String> entry : events.entrySet()) {
            ret.add(entry.getKey().getSimpleName() + "<-->" + entry.getValue());
        }

        return ret;
    }

    public Map<Class<?>, String> getEvents() {
        return Collections.unmodifiableMap(events);
    }

    public void setEvents(Map<Class<?>, String> events) {
        this.events.clear();

        for (Map.Entry<Class<?>, String> entry : events.entrySet()) {
            if (null != entry.getValue()) {
                this.events.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void messageReceived(Object input) {
        if (null == input) {
            return;
        }

        String event = events.get(input.getClass());
        if (null == event) {
            // 默认请求类型为事件名
            event = input.getClass().getName();
        }

        logger.debug("fire event [{}] <--> {}", event, input);

        eventBus.fireEvent(event, input);
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

}
