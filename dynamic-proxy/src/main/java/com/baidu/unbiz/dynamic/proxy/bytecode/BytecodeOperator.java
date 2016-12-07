/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

/**
 * 字节码操作标识接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-11-5 下午4:29:10
 */
public interface BytecodeOperator {

    String DEFAULT_PROXY_PREFIX = "Proxy";

    Class<?> define();

}
