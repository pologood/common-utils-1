package com.baidu.unbiz.dynamic.proxy.template;

import com.baidu.unbiz.dynamic.proxy.Interceptor;
import com.baidu.unbiz.dynamic.proxy.ObjectInvoker;
import com.baidu.unbiz.dynamic.proxy.ObjectProvider;
import com.baidu.unbiz.dynamic.proxy.ProxyCreator;

/**
 * CreatorTemplate
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:50:09
 */
public abstract class CreatorTemplate implements ProxyCreator {

    @Override
    public boolean canProxy(Class<?>...proxyClasses) {
        for (Class<?> proxyClass : proxyClasses) {
            if (!proxyClass.isInterface()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public <T> T createDelegatorProxy(ObjectProvider<?> delegateProvider, Class<?>...proxyClasses) {
        return createDelegatorProxy(Thread.currentThread().getContextClassLoader(), delegateProvider, proxyClasses);
    }

    @Override
    public <T> T createInterceptorProxy(Object target, Interceptor interceptor, Class<?>...proxyClasses) {
        return createInterceptorProxy(Thread.currentThread().getContextClassLoader(), 
                target, interceptor, proxyClasses);
    }

    @Override
    public <T> T createInvokerProxy(ObjectInvoker invoker, Class<?>...proxyClasses) {
        return createInvokerProxy(Thread.currentThread().getContextClassLoader(), invoker, proxyClasses);
    }

}
