/**
 * 
 */
package com.baidu.unbiz.modules.webpage.build;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.ObjectUtil;
import com.baidu.unbiz.common.StringUtil;

import net.sf.cglib.asm.Type;
import net.sf.cglib.core.Signature;

/**
 * DynamicClassBuilder
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 上午2:42:46
 */
abstract class DynamicClassBuilder {
    private static final int PUBLIC_STATIC_MODIFIERS = PUBLIC | STATIC;
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final ClassLoader classLoader;

    public DynamicClassBuilder() {
        this(null);
    }

    public DynamicClassBuilder(ClassLoader cl) {
        this.classLoader = cl;
    }

    public ClassLoader getClassLoader() {
        return classLoader == null ? ClassLoaderUtil.getContextClassLoader() : classLoader;
    }

    protected Signature getSignature(Method method, String rename) {
        String name = ObjectUtil.defaultIfNull(StringUtil.trimToNull(rename), method.getName());
        Type returnType = Type.getType(method.getReturnType());
        Type[] paramTypes = Type.getArgumentTypes(method);

        return new Signature(name, returnType, paramTypes);
    }

    protected boolean isPublicStatic(Method method) {
        return (method.getModifiers() & PUBLIC_STATIC_MODIFIERS) == PUBLIC_STATIC_MODIFIERS;
    }

    protected boolean isEqualsMethod(Method method) {
        if (!"equals".equals(method.getName())) {
            return false;
        }

        Class<?>[] paramTypes = method.getParameterTypes();

        return paramTypes.length == 1 && paramTypes[0] == Object.class;
    }

    protected boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && method.getParameterTypes().length == 0;
    }

    protected boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && method.getParameterTypes().length == 0;
    }
}
