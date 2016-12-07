package com.baidu.unbiz.dynamic.proxy.provider;

import java.io.Serializable;

import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.dynamic.proxy.ObjectProvider;

public class BeanProvider<T> implements ObjectProvider<T>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6261791550469264285L;

    private final Class<? extends T> beanClass;

    public BeanProvider(Class<? extends T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T getObject() {
        return ReflectionUtil.newInstance(beanClass);
    }
}
