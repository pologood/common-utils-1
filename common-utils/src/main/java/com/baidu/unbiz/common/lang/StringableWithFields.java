/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.able.ToStringable;
import com.baidu.unbiz.common.cache.FieldCache;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-3 上午1:13:30
 */
public class StringableWithFields implements ToStringable {

    /**
     * 表示打印对象时需要显示的 {@link Field java.lang.reflect.Field}
     * 
     * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
     * @version create on 2014-12-3 上午12:59:33
     */
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public static @interface ShowField {

    }

    private static final FieldCache fieldCache = FieldCache.getInstance();

    @Override
    public String toString() {

        Field[] fields = fieldCache.getFields(this.getClass(), ShowField.class);

        return ReflectionUtil.dump(this, fields);

    }

}
