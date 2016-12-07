package com.baidu.unbiz.dynamic.proxy.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import com.baidu.unbiz.common.ClassUtil;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.common.PackageUtil;
import com.baidu.unbiz.common.StringUtil;
import com.baidu.unbiz.dynamic.proxy.DefaultProxyCreator;
import com.baidu.unbiz.dynamic.proxy.ProxyCreator;

/**
 * 动态代理相关的工具类
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:40:28
 */
public abstract class ProxyUtil {

    public static final Object[] EMPTY_ARGUMENTS = Emptys.EMPTY_OBJECT_ARRAY;
    public static final Class<?>[] EMPTY_ARGUMENT_TYPES = Emptys.EMPTY_CLASS_ARRAY;
    private static final Map<Class<?>, Class<?>> WRAPPER_CLASS_MAP;
    private static final Map<Class<?>, Object> NULL_VALUE_MAP;

    static {
        Map<Class<?>, Class<?>> wrappers = CollectionUtil.createHashMap();
        // @see ClassUtil
        wrappers.put(Integer.TYPE, Integer.class);
        wrappers.put(Character.TYPE, Character.class);
        wrappers.put(Boolean.TYPE, Boolean.class);
        wrappers.put(Short.TYPE, Short.class);
        wrappers.put(Long.TYPE, Long.class);
        wrappers.put(Float.TYPE, Float.class);
        wrappers.put(Double.TYPE, Double.class);
        wrappers.put(Byte.TYPE, Byte.class);
        WRAPPER_CLASS_MAP = Collections.unmodifiableMap(wrappers);

        // @see ClassUtil
        Map<Class<?>, Object> nullValues = CollectionUtil.createHashMap();
        nullValues.put(Integer.TYPE, Integer.valueOf(0));
        nullValues.put(Long.TYPE, Long.valueOf(0));
        nullValues.put(Short.TYPE, Short.valueOf((short) 0));
        nullValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
        nullValues.put(Float.TYPE, Float.valueOf(0.0f));
        nullValues.put(Double.TYPE, Double.valueOf(0.0));
        nullValues.put(Character.TYPE, Character.valueOf((char) 0));
        nullValues.put(Boolean.TYPE, Boolean.FALSE);
        NULL_VALUE_MAP = Collections.unmodifiableMap(nullValues);
    }

    public static Class<?>[] getAllInterfaces(Class<?> cls) {
        return cls == null ? null : ClassUtil.getAllInterfaces(cls).toArray(Emptys.EMPTY_CLASS_ARRAY);
    }

    public static String getJavaClassName(Class<?> clazz) {
        if (clazz.isArray()) {
            return getJavaClassName(clazz.getComponentType()) + "[]";
        }

        return clazz.getName();
    }

    public static Class<?> getWrapperClass(Class<?> primitiveType) {
        return WRAPPER_CLASS_MAP.get(primitiveType);
    }

    public static <T> T nullValue(Class<T> type) {
        @SuppressWarnings("unchecked")
        T result = (T) NULL_VALUE_MAP.get(type);

        return result;
    }

    public static ProxyCreator getInstance() {
        return DefaultProxyCreator.INSTANCE;
    }

    public static boolean isHashCode(Method method) {
        return "hashCode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 0;
    }

    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0]);
    }

    public static String getProxyName(Class<?> targetClass, String prefix) {
        String className = PackageUtil.getPackageName(targetClass) + "." + prefix + targetClass.getSimpleName();
        return className;
    }

    public static String getProxyName(String targetClass, String prefix) {
        String className =
                PackageUtil.getPackageName(targetClass) + "." + prefix
                        + StringUtil.substringAfterLast(targetClass, ".");
        return className;
    }

}
