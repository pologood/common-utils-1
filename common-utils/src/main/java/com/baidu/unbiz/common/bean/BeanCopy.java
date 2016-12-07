/**
 * 
 */
package com.baidu.unbiz.common.bean;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.baidu.unbiz.common.ClassUtil;
import com.baidu.unbiz.common.ObjectUtil;
import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.able.Transformer;
import com.baidu.unbiz.common.cache.BeanInfoCache;
import com.baidu.unbiz.common.cache.FieldCache;

/**
 * BeanCopy
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月15日 下午4:09:04
 */
public abstract class BeanCopy {

    protected static final FieldCache FIELD_CACHE = FieldCache.getInstance();

    protected static final BeanInfoCache BEAN_INFO_CACHE = BeanInfoCache.getInstance();

    public static void copy(Object src, Object dest, boolean useAnnotation) {
        if (!useAnnotation) {
            copyProperties(src, dest);
            return;
        }

        copyByAnnotation(src, dest);
    }

    public static void copyProperties(Object src, Object dest) {
        copyProperties(src, dest, false);
    }

    // FIXME just simple
    public static void copyProperties(Object src, Object dest, Transformer<String, String> transformer) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        Class<?> srcClazz = src.getClass();
        Field[] destFields = FIELD_CACHE.getInstanceFields(dest.getClass());

        for (Field field : destFields) {
            if (ReflectionUtil.isFinal(field)) {
                continue;
            }
            Field srcField = FIELD_CACHE.getInstanceField(srcClazz, field.getName());
            if (srcField != null) {
                String descField = transformer.transform(srcField.getName());
                Object value = ReflectionUtil.readField(srcField, src);
                // Object target, String fieldName, Object value
                ReflectionUtil.writeField(dest, descField, value);
            }
        }
    }

    public static void copyProperties(Object src, Object dest, boolean ignoreNull) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        Class<?> srcClazz = src.getClass();
        Field[] destFields = FIELD_CACHE.getInstanceFields(dest.getClass());

        for (Field field : destFields) {
            if (ReflectionUtil.isFinal(field)) {
                continue;
            }
            Field srcField = FIELD_CACHE.getInstanceField(srcClazz, field.getName());
            if (srcField != null) {
                Object value = ReflectionUtil.readField(srcField, src);
                if (value != null) {
                    ReflectionUtil.writeField(field, dest, value);
                    continue;
                }
                if (!ignoreNull) {
                    ReflectionUtil.writeField(field, dest, value);
                }
            }
        }
    }

    public static void copyByAnnotation(Object src, Object dest) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        Class<?> srcClazz = src.getClass();
        Class<?> destClazz = dest.getClass();
        Field[] destFields = FIELD_CACHE.getInstanceFields(dest.getClass());

        for (Field field : destFields) {
            if (ReflectionUtil.isFinal(field) || ReflectionUtil.hasAnnotation(field, IgnoreField.class)) {
                continue;
            }
            Field srcField = FIELD_CACHE.getField(srcClazz, CopyField.class, field.getName());
            if (srcField != null && supportFor(destClazz, srcField.getAnnotation(CopyField.class))) {
                Object value = ReflectionUtil.readField(srcField, src);
                ReflectionUtil.writeField(field, dest, value);
            }

        }

    }

    public static void copyByMethod(Object src, Object dest) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        BeanInfo beanInfo = BEAN_INFO_CACHE.getBeanInfo(dest.getClass());

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor destPd : propertyDescriptors) {
            PropertyDescriptor srcPd = BEAN_INFO_CACHE.getPropertyDescriptor(src.getClass(), destPd.getName());
            if (srcPd == null) {
                continue;
            }

            Method readMethod = srcPd.getReadMethod();
            Object value = ReflectionUtil.invokeMethod(readMethod, src);
            Method writeMethod = destPd.getWriteMethod();
            ReflectionUtil.invokeMethod(writeMethod, dest, value);

        }
    }

    private static boolean supportFor(Class<?> fromClass, CopyField copyField) {
        for (Class<?> clazz : copyField.supportFor()) {
            if (ClassUtil.isAssignable(clazz, fromClass)) {
                return true;
            }
        }

        return false;
    }

    public static void map2Object(Object object, Map<?, ?> map) {
        Field[] fields = ReflectionUtil.getAllFieldsOfClass(object.getClass(), true);

        for (Field field : fields) {
            Object value = map.get(field.getName());
            if (value != null) {
                ReflectionUtil.writeField(object, field.getName(), value);
            }
        }
    }

    // FIXME by 2
    public static void map2Object(Object object, Map<?, ?> map, Transformer<String, String> transformer) {
        Field[] fields = ReflectionUtil.getAllFieldsOfClass(object.getClass(), true);

        for (Field field : fields) {
            String fieldName = field.getName();
            Object value = map.get(fieldName);
            if (value == null) {
                String transName = transformer.transform(fieldName);
                value = map.get(transName);
            }
            if (value != null) {
                ReflectionUtil.writeField(object, fieldName, value);
            }
        }
    }

    public static void object2Map(Object object, Map<String, Object> map) {
        Field[] fields = ReflectionUtil.getAllFieldsOfClass(object.getClass(), true);

        for (Field field : fields) {
            Object value = ReflectionUtil.readField(field, object);
            if (value != null) {
                map.put(field.getName(), value);
            }
        }
    }
}
