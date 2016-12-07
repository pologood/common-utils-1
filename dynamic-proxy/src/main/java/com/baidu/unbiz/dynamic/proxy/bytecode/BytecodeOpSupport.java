/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.ResourceUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 字节码操作支持
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-12-9 上午1:31:38
 */
public abstract class BytecodeOpSupport extends LoggerSupport implements BytecodeOperator {

    protected ClassWriter cw;

    protected Class<?> proxyClass;

    protected String className;

    protected BytecodeOpSupport(Class<?> targetClass) {
        this(targetClass, DEFAULT_PROXY_PREFIX);
    }

    protected BytecodeOpSupport(Class<?> targetClass, String prefix) {
        proxyClass = createProxyClass(targetClass, prefix);

        className = proxyClass.getName();

        cw = visitClass(proxyClass);
    }

    protected abstract Class<?> createProxyClass(Class<?> targetClass, String prefix);

    protected void visit(Class<?> targetType, String...interfaceNames) {
        String superClass =
                targetType.isInterface() ? "java/lang/Object" : ResourceUtil.getResourceNameFor(targetType
                        .getSuperclass());
        int access =
                targetType.isInterface() ? Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE : 
                    Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER;
        cw.visit(Opcodes.V1_6, access, ResourceUtil.getResourceNameFor(targetType), null, superClass, interfaceNames);
    }

    protected ClassWriter visitClass(Class<?> targetType) {
        // FIXME
        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        String[] interfaceNames = getInterfaceNames(targetType);

        visit(targetType, interfaceNames);

        return cw;
    }

    protected String[] getInterfaceNames(Class<?> targetType) {
        Class<?>[] classes = targetType.getInterfaces();
        if (ArrayUtil.isEmpty(classes)) {
            return null;
        }

        int length = classes.length;
        String[] ret = new String[length];
        for (int i = 0; i < length; i++) {
            ret[i] = ResourceUtil.getResourceNameFor(classes[i]);
        }

        return ret;
    }

    @Override
    public Class<?> define() {
        return new ClassLoader(ClassLoaderUtil.getContextClassLoader()) {
            public Class<?> defineClass() {
                cw.visitEnd();
                byte[] bytes = cw.toByteArray();
                return defineClass(className, bytes, 0, bytes.length);
            }
        }.defineClass();
    }

}
