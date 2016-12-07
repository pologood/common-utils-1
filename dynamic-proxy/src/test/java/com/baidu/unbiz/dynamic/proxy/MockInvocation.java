package com.baidu.unbiz.dynamic.proxy;

import java.lang.reflect.Method;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.ObjectUtil;
import com.baidu.unbiz.dynamic.proxy.Invocation;
import com.baidu.unbiz.dynamic.proxy.util.ProxyUtil;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午11:10:22
 */
public class MockInvocation implements Invocation {

    private final Method method;
    private final Object[] arguments;
    private final Object returnValue;

    public MockInvocation(Method method, Object returnValue, Object...arguments) {
        this.returnValue = returnValue;
        this.arguments = ObjectUtil.defaultIfNull(ArrayUtil.clone(arguments), ProxyUtil.EMPTY_ARGUMENTS);
        this.method = method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable {
        return returnValue;
    }
}
