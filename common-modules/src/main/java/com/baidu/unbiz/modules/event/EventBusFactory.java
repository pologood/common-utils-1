package com.baidu.unbiz.modules.event;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.ClassUtil;
import com.baidu.unbiz.common.PackageUtil;
import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.exception.ClassInstantiationException;
import com.baidu.unbiz.common.logger.Logger;
import com.baidu.unbiz.common.logger.LoggerFactory;

/**
 * 事件总线工厂
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-21 下午5:31:33
 */
public abstract class EventBusFactory {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EventBusFactory.class);

    /**
     * 检查并获取事件总线
     * 
     * @param type Class类型
     * @param packages 包集合
     * @param assignableClass 目标类型
     * @return 事件总线 @see EventBus
     */
    public static EventBus detectEvents(Class<?> type, Collection<String> packages, Class<?> assignableClass) {
        EventBus eventBus = null;
        try {
            eventBus = ClassLoaderUtil.newInstance(type.getCanonicalName());
        } catch (Exception e) {
            logger.error("detectEvents: ", e);
            throw new RuntimeException(e);
        }

        if (null == packages) {
            return eventBus;
        }

        for (String pkgName : packages) {

            try {
                List<String> clsNames = PackageUtil.getClassesInPackage(pkgName);

                for (String clsName : clsNames) {
                    try {
                        detectEvent(eventBus, assignableClass, clsName);
                    } catch (Exception e) {
                        logger.error("detectEvents: ", e);
                    }
                }
            } catch (IOException e) {
                logger.error("detectEvents: ", e);
            }
        }

        return eventBus;
    }

    /**
     * 检查并获取事件总线
     * 
     * @param packages 包集合
     * @param assignableClass 目标类型
     * @return 事件总线 @see EventBus
     */
    public static EventBus detectEvents(Collection<String> packages, Class<?> assignableClass) {
        return detectEvents(DefaultEventBus.class, packages, assignableClass);
    }
    
    /**
     * 检查并获取事件总线
     * 
     * @param packages 包集合
     * @return 事件总线 @see EventBus
     */
    public static final EventBus detectEvents(Collection<String> packages) {
        return detectEvents(packages, Object.class);
    }

    /**
     * 检查事件总线
     * 
     * @param eventBus 事件类型
     * @param assignableClass 目标类型
     * @param clsName Class名称
     * @throws ClassNotFoundException
     * @throws ClassInstantiationException
     */
    private static void detectEvent(EventBus eventBus, Class<?> assignableClass, String clsName)
            throws ClassNotFoundException, ClassInstantiationException {

        Class<?> cls = ClassLoaderUtil.loadClass(clsName);
        if (!ClassUtil.canInstatnce(cls)) {
            return;
        }

        if (ClassUtil.isAssignable(assignableClass, cls)) {
            Method[] methods = ReflectionUtil.getAllMethodsOfClass(cls);
            Object target = ClassLoaderUtil.newInstance(cls);
            for (Method method : methods) {
                Action action = method.getAnnotation(Action.class);
                if (action != null && action.event() != null) {
                    eventBus.registerObserver(action.event(), target, method.getName(), method.getParameterTypes());
                    logger.info("add event: {}:=>{}.{}", action.event(), cls.getName(), method.getName());
                }
            }
        }

    }

}
