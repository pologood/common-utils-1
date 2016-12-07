/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.dynamic.proxy.util.AsmUtil;
import com.baidu.unbiz.dynamic.proxy.util.ClassOpUtil;
import com.baidu.unbiz.dynamic.proxy.util.InterfaceOpUtil;

/**
 * 字节码对注解的操作
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年12月7日 下午3:34:18
 */
public class AnnotationOperator extends AnnotationOpSupport {

    public AnnotationOperator(Class<?> targetClass) {
        super(targetClass);
    }

    public AnnotationOperator(Class<?> targetClass, String prefix) {
        super(targetClass, prefix);
    }

    @Override
    protected Class<?> createProxyClass(Class<?> targetClass, String prefix) {
        return !targetClass.isInterface() ? ClassOpUtil.createProxy(targetClass, prefix) : InterfaceOpUtil
                .createEmptyInterface(targetClass.getName(), prefix, targetClass);
    }

    @Override
    public AnnotationOperator copyClassAnnotation() {
        // FIXME visit annotations
        Annotation[] classAnnotations = proxyClass.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            // 继承注解忽略
            if (annotation.annotationType().getAnnotation(Inherited.class) != null) {
                continue;
            }
            Type type = Type.getType(annotation.annotationType());
            AnnotationVisitor av = cw.visitAnnotation(type.getDescriptor(), true);
            Method[] ams = annotation.annotationType().getDeclaredMethods();
            for (Method method : ams) {
                String name = method.getName();
                Object value =
                        ReflectionUtil.invokeMethod(annotation, name, Emptys.EMPTY_CLASS_ARRAY,
                                Emptys.EMPTY_OBJECT_ARRAY);
                av.visit(name, value);
            }
            av.visitEnd();
        }

        return this;
    }

    @Override
    public AnnotationOperator addAnnotation(final Field field, final VirturalAnnotation...annotations) {
        Type type = Type.getType(field.getType());
        // FIXME null,null
        FieldVisitor fv = cw.visitField(field.getModifiers(), field.getName(), type.getDescriptor(), null, null);
        if (ArrayUtil.isNotEmpty(annotations)) {
            for (VirturalAnnotation annotation : annotations) {
                visitAnnotation(fv, annotation);
            }
        }

        fv.visitEnd();
        return this;
    }

    @Override
    public AnnotationOperator addAnnotation(final Field[] fields, final VirturalAnnotation...annotations) {
        // visit fields
        for (Field field : fields) {
            addAnnotation(field, annotations);
        }

        return this;
    }

    @Override
    public AnnotationOperator addAnnotation(final Method[] methods, final VirturalAnnotation...annotations) {
        // visit methods
        for (Method method : methods) {
            addAnnotation(method, annotations);
        }

        return this;
    }

    @Override
    public AnnotationOperator addAnnotation(final Method method, final VirturalAnnotation...annotations) {
        Type type = Type.getType(method.getReturnType());
        // FIXME
        MethodVisitor mv =
                cw.visitMethod(method.getModifiers(), method.getName(), type.getDescriptor(), null,
                        AsmUtil.getInternalNames(method.getExceptionTypes()));
        if (ArrayUtil.isNotEmpty(annotations)) {
            for (VirturalAnnotation annotation : annotations) {
                visitAnnotation(mv, annotation);
            }
        }

        mv.visitEnd();
        return this;
    }

}
