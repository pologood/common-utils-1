/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.util;

import org.objectweb.asm.Opcodes;

import cn.wensiqun.asmsupport.core.builder.impl.ClassBuilderImpl;
import cn.wensiqun.asmsupport.core.loader.CachedThreadLocalClassLoader;
import cn.wensiqun.asmsupport.standard.def.clazz.ClassHolder;
import cn.wensiqun.asmsupport.standard.def.clazz.IClass;
import cn.wensiqun.asmsupport.standard.utils.IClassUtils;

import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.dynamic.proxy.bytecode.BytecodeOperator;

/**
 * 类操作工具类 FIXME remove?
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-12-9 上午2:17:11
 */
public abstract class ClassOpUtil extends LoggerSupport implements BytecodeOperator {

    private static final ClassHolder HOLDER = CachedThreadLocalClassLoader.getInstance(ClassLoaderUtil
            .getContextClassLoader());

    public static Class<?> createProxy(Class<?> targetClass, String prefix) {
        String className = ProxyUtil.getProxyName(targetClass, prefix);
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, targetClass.getInterfaces());

        ClassBuilderImpl creator =
                new ClassBuilderImpl(Opcodes.V1_6, Opcodes.ACC_PUBLIC, className, HOLDER.getType(targetClass), itfs);
        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

}
