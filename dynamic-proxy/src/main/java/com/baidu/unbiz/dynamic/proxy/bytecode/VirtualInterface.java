/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * 模拟java中的接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-11-5 下午6:03:50
 */
public class VirtualInterface extends StringableSupport {

    private String name;

    private VirtualMethod[] methods;

    private VirtualField[] constants;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VirtualMethod[] getMethods() {
        return methods;
    }

    public void setMethods(VirtualMethod[] methods) {
        this.methods = methods;
    }

    public VirtualField[] getConstants() {
        return constants;
    }

    public void setConstants(VirtualField[] constants) {
        this.constants = constants;
    }

}
