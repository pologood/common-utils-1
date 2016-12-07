/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * 模拟java中的method
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-11-5 下午5:54:13
 */
public class VirtualMethod extends StringableSupport {

    private String methodName;

    private Class<?>[] argumentTypes;

    private Class<?> returnType = void.class;

    private Class<?>[] exceptionTypes;

    public VirtualMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public VirtualMethod setArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public VirtualMethod setReturnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public Class<?>[] getExceptionTypes() {
        return exceptionTypes;
    }

    public VirtualMethod setExceptionTypes(Class<?>[] exceptionTypes) {
        this.exceptionTypes = exceptionTypes;
        return this;
    }

}
