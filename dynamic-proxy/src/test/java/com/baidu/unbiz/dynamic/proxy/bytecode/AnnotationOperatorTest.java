/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.unbiz.common.ReflectionUtil;

/**
 * AnnotationOperatorTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年12月8日 上午12:03:44
 */
public class AnnotationOperatorTest {

    @Test
    public void addFieldAnnotation() throws Exception {
        System.out.println(Arrays.toString(ReflectionUtil.getAllMethodsOfClass(Bar.class)));
        AnnotationOperator as = new AnnotationOperator(Foo.class);
        VirturalAnnotation annotation = new VirturalAnnotation(Bar.class);
        annotation.addNameValue("value", "ggg");
        Class<?> klass = as.addAnnotation(Foo.class.getDeclaredField("name"), annotation).define();
        System.out.println(klass);
        Field nameField = klass.getDeclaredField("name");
        Assert.assertEquals(Bar.class, nameField.getAnnotations()[0].annotationType());

        Field ageField = klass.getDeclaredField("age");
        Assert.assertArrayEquals(new Annotation[0], ageField.getAnnotations());
    }

    @Test
    public void addAnnotation() throws Exception {
        System.out.println(Arrays.toString(ReflectionUtil.getAllMethodsOfClass(Bar.class)));
        AnnotationOperator as = new AnnotationOperator(Foo.class, "Test");
        VirturalAnnotation annotation = new VirturalAnnotation(Bar.class);
        annotation.addNameValue("value", "ggg");
        Class<?> klass = as.createAnnotation(annotation).define();
        System.out.println(klass);
        System.out.println(Arrays.toString(klass.getAnnotations()));

        // Field nameField = klass.getDeclaredField("name");
        // Assert.assertEquals(Bar.class, nameField.getAnnotations()[0].annotationType());
        //
        // Field ageField = klass.getDeclaredField("age");
        // Assert.assertArrayEquals(new Annotation[0], ageField.getAnnotations());
    }

    @Test
    public void addInterface() throws Exception {
        System.out.println(Arrays.toString(ReflectionUtil.getAllMethodsOfClass(Bar.class)));
        AnnotationOperator as = new AnnotationOperator(Go.class, "Dd");
        VirturalAnnotation annotation = new VirturalAnnotation(Bar.class);
        annotation.addNameValue("value", "xxx");
        Class<?> klass = as.createAnnotation(annotation).define();

        // Class<?> klass = as.createAnnotation(annotation).define();
        System.out.println(klass);
        System.out.println("==================");
        System.out.println(Arrays.toString(Go.class.getDeclaredMethods()));
        System.out.println(Arrays.toString(klass.getMethods()));
        System.out.println(Arrays.toString(klass.getAnnotations()));
        System.out.println(Arrays.toString(klass.getInterfaces()));

        // Field nameField = klass.getDeclaredField("name");
        // Assert.assertEquals(Bar.class, nameField.getAnnotations()[0].annotationType());
        //
        // Field ageField = klass.getDeclaredField("age");
        // Assert.assertArrayEquals(new Annotation[0], ageField.getAnnotations());
    }

}
