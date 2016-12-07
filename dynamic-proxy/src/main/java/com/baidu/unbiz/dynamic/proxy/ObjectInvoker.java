package com.baidu.unbiz.dynamic.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 麻痹，因maven编译器不通过改名字
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:38:14
 */
public interface ObjectInvoker extends Serializable {

    Object invoke(Object proxy, Method method, Object...arguments) throws Throwable;

}
