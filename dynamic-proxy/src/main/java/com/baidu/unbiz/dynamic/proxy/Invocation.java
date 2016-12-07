package com.baidu.unbiz.dynamic.proxy;

import java.lang.reflect.Method;

/**
 * 调用接口，类似<code>org.aopalliance.intercept</code>中的<code>Invocation</code>接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:16:59
 */
public interface Invocation {

    /**
     * 获取方法调用参数
     * 
     * @return 方法调用参数
     */
    Object[] getArguments();

    /**
     * 返回方法对象
     * 
     * @return 方法对象
     */
    Method getMethod();

    /**
     * 返回代理对象
     * 
     * @return 代理对象
     */
    Object getProxy();

    /**
     * 触发拦截器调用链
     * 
     * @return 调用结果
     * @throws Throwable
     */
    Object proceed() throws Throwable;
}
