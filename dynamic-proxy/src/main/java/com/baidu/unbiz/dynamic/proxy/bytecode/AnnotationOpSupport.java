/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.baidu.unbiz.biz.entity.KeyAndValue;

/**
 * 字节码对注解的支持
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年12月7日 下午3:34:18
 */
public abstract class AnnotationOpSupport extends BytecodeOpSupport {

    public AnnotationOpSupport(Class<?> targetClass) {
        super(targetClass);
    }

    public AnnotationOpSupport(Class<?> targetClass, String prefix) {
        super(targetClass, prefix);
    }

    protected abstract AnnotationOpSupport copyClassAnnotation();

    public AnnotationOpSupport createAnnotation(final VirturalAnnotation...annotations) {
        for (VirturalAnnotation annotation : annotations) {
            visitAnnotation(annotation);
        }

        return this;
    }

    public abstract AnnotationOpSupport addAnnotation(final Field field, final VirturalAnnotation...annotations);

    public abstract AnnotationOpSupport addAnnotation(final Field[] fields, final VirturalAnnotation...annotations);

    public abstract AnnotationOpSupport addAnnotation(final Method[] methods, final VirturalAnnotation...annotations);

    public abstract AnnotationOpSupport addAnnotation(final Method method, final VirturalAnnotation...annotations);

    protected MethodVisitor visitAnnotation(MethodVisitor mv, VirturalAnnotation annotation) {
        Type type = Type.getType(annotation.getAnnotationType());
        AnnotationVisitor av = mv.visitAnnotation(type.getDescriptor(), true);
        List<KeyAndValue<String, Object>> nameValues = annotation.getNameValues();

        for (KeyAndValue<String, Object> nameValue : nameValues) {
            av.visit(nameValue.getKey(), nameValue.getValue());
        }

        av.visitEnd();
        return mv;
    }

    protected FieldVisitor visitAnnotation(FieldVisitor fv, VirturalAnnotation annotation) {
        Type type = Type.getType(annotation.getAnnotationType());
        AnnotationVisitor av = fv.visitAnnotation(type.getDescriptor(), true);
        List<KeyAndValue<String, Object>> nameValues = annotation.getNameValues();

        for (KeyAndValue<String, Object> nameValue : nameValues) {
            av.visit(nameValue.getKey(), nameValue.getValue());
        }

        av.visitEnd();
        return fv;
    }

    protected AnnotationVisitor visitAnnotation(VirturalAnnotation annotation) {
        Type type = Type.getType(annotation.getAnnotationType());
        AnnotationVisitor av = cw.visitAnnotation(type.getDescriptor(), true);
        List<KeyAndValue<String, Object>> nameValues = annotation.getNameValues();

        for (KeyAndValue<String, Object> nameValue : nameValues) {
            av.visit(nameValue.getKey(), nameValue.getValue());
        }

        av.visitEnd();
        return av;
    }

}
