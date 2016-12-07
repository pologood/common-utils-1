package com.baidu.unbiz.dynamic.proxy.sample;

import com.baidu.unbiz.dynamic.proxy.Interceptor;
import com.baidu.unbiz.dynamic.proxy.Invocation;

public class SuffixInterceptor implements Interceptor {

    private static final long serialVersionUID = 2275465822166741737L;

    private final String suffix;

    public SuffixInterceptor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Object intercept(Invocation methodInvocation) throws Throwable {
        Object result = methodInvocation.proceed();
        if (result instanceof String) {
            result = ((String) result) + suffix;
        }

        return result;
    }
}
