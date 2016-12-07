package com.baidu.unbiz.dynamic.proxy.util;

import org.objectweb.asm.Opcodes;

import cn.wensiqun.asmsupport.core.builder.impl.ClassBuilderImpl;
import cn.wensiqun.asmsupport.core.builder.impl.InterfaceBuilderImpl;
import cn.wensiqun.asmsupport.core.loader.CachedThreadLocalClassLoader;
import cn.wensiqun.asmsupport.standard.def.clazz.ClassHolder;
import cn.wensiqun.asmsupport.standard.def.clazz.IClass;
import cn.wensiqun.asmsupport.standard.utils.IClassUtils;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.dynamic.proxy.bytecode.BytecodeOperator;
import com.baidu.unbiz.dynamic.proxy.bytecode.VirtualField;
import com.baidu.unbiz.dynamic.proxy.bytecode.VirtualInterface;
import com.baidu.unbiz.dynamic.proxy.bytecode.VirtualMethod;

/**
 * 接口操作工具类
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-11-5 下午5:01:26
 */
public abstract class InterfaceOpUtil extends LoggerSupport implements BytecodeOperator {

    private static final ClassHolder HOLDER = CachedThreadLocalClassLoader.getInstance(ClassLoaderUtil
            .getContextClassLoader());

    public static Class<?> addInterfaces2Class(Class<?> targetClass, Class<?>...interfaces) {
        return addInterfaces2Class(DEFAULT_PROXY_PREFIX, targetClass, interfaces);
    }

    public static Class<?> addInterfaces2Class(String prefix, Class<?> targetClass, Class<?>...interfaces) {
        String className = ProxyUtil.getProxyName(targetClass, prefix);
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, interfaces);

        ClassBuilderImpl creator =
                new ClassBuilderImpl(Opcodes.V1_6, Opcodes.ACC_PUBLIC, className, HOLDER.getType(targetClass), itfs);
        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

    public static Class<?> createEmptyInterface(String targetName, String prefix, Class<?>...extendInterfaces) {
        String className = ProxyUtil.getProxyName(targetName, prefix);
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, extendInterfaces);

        InterfaceBuilderImpl creator = new InterfaceBuilderImpl(Opcodes.V1_6, className, itfs);
        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

    public static Class<?> createEmptyInterface(String targetName, Class<?>...extendInterfaces) {
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, extendInterfaces);

        InterfaceBuilderImpl creator = new InterfaceBuilderImpl(Opcodes.V1_6, targetName, itfs);
        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

    public static Class<?> createInterface(String targetName, String prefix, VirtualInterface virtualInterface,
            Class<?>...extendInterfaces) {
        String className = ProxyUtil.getProxyName(targetName, prefix);
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, extendInterfaces);
        InterfaceBuilderImpl creator = new InterfaceBuilderImpl(Opcodes.V1_6, className, itfs);

        createMethods(creator, virtualInterface.getMethods());
        createGlobalVariables(creator, virtualInterface.getConstants());

        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

    public static Class<?> createInterface(String targetName, VirtualInterface virtualInterface,
            Class<?>...extendInterfaces) {
        IClass[] itfs = IClassUtils.convertToAClass(HOLDER, extendInterfaces);
        InterfaceBuilderImpl creator = new InterfaceBuilderImpl(Opcodes.V1_6, targetName, itfs);

        createMethods(creator, virtualInterface.getMethods());
        createGlobalVariables(creator, virtualInterface.getConstants());

        creator.setClassOutPutPath(ClassLoaderUtil.getClasspath());

        return creator.startup();
    }

    private static void createMethods(InterfaceBuilderImpl creator, VirtualMethod...virtualMethods) {
        if (ArrayUtil.isEmpty(virtualMethods)) {
            return;
        }

        for (VirtualMethod vm : virtualMethods) {
            IClass[] argClasses = IClassUtils.convertToAClass(HOLDER, vm.getArgumentTypes());
            IClass returnClass = HOLDER.getType(vm.getReturnType());
            IClass[] exceptionClasses = IClassUtils.convertToAClass(HOLDER, vm.getExceptionTypes());

            creator.createMethod(vm.getMethodName(), argClasses, returnClass, exceptionClasses);
        }
    }

    private static void createGlobalVariables(InterfaceBuilderImpl creator, final VirtualField...constants) {
        if (ArrayUtil.isEmpty(constants)) {
            return;
        }

        for (VirtualField constant : constants) {
            IClass iClass = HOLDER.getType(constant.getFieldType());
            creator.createField(constant.getFieldName(), iClass, constant.getFieldValue());
        }

    }

}
