package com.baidu.unbiz.dynamic.proxy;

import java.io.Serializable;

/**
 * 定义拦截器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:09:33
 */
public interface Interceptor extends Serializable {

    Object intercept(Invocation invocation) throws Throwable;

}
