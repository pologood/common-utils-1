/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.dynamic.proxy.bytecode;

import java.lang.annotation.Annotation;
import java.util.List;

import com.baidu.unbiz.biz.entity.KeyAndValue;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * 虚拟的注解
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年12月7日 下午5:32:34
 */
public class VirturalAnnotation extends StringableSupport {

    private Class<? extends Annotation> annotationType;

    private List<KeyAndValue<String, Object>> nameValues;

    public VirturalAnnotation(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        nameValues = CollectionUtil.createArrayList();
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    public VirturalAnnotation addNameValue(String name, Object value) {
        nameValues.add(new KeyAndValue<String, Object>(name, value));
        return this;
    }

    public List<KeyAndValue<String, Object>> getNameValues() {
        return nameValues;
    }

}
