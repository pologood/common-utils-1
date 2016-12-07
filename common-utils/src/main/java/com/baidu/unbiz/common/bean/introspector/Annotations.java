/**
 * 
 */
package com.baidu.unbiz.common.bean.introspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年11月5日 下午3:40:48
 */
public class Annotations extends StringableSupport {

    protected final AnnotatedElement annotatedElement;

    protected final Map<Class<? extends Annotation>, AnnotationDescriptor> annotationsMap;

    // cache
    private AnnotationDescriptor[] allAnnotations;

    public Annotations(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationsMap = inspectAnnotations();
    }

    private Map<Class<? extends Annotation>, AnnotationDescriptor> inspectAnnotations() {

        Annotation[] annotations = ReflectionUtil.getAnnotation(annotatedElement);
        if (ArrayUtil.isEmpty(annotations)) {
            return null;
        }

        Map<Class<? extends Annotation>, AnnotationDescriptor> map = CollectionUtil.createHashMap(annotations.length);

        for (Annotation annotation : annotations) {
            map.put(annotation.annotationType(), new AnnotationDescriptor(annotation));
        }

        return map;
    }

    public AnnotationDescriptor getAnnotationDescriptor(Class<? extends Annotation> clazz) {
        if (annotationsMap == null) {
            return null;
        }

        return annotationsMap.get(clazz);
    }

    public AnnotationDescriptor[] getAllAnnotationDescriptors() {
        if (annotationsMap == null) {
            return null;
        }

        if (allAnnotations == null) {
            AnnotationDescriptor[] allAnnotations = new AnnotationDescriptor[annotationsMap.size()];

            int index = 0;
            for (AnnotationDescriptor annotationDescriptor : annotationsMap.values()) {
                allAnnotations[index] = annotationDescriptor;
                index++;
            }

            Arrays.sort(allAnnotations, new Comparator<AnnotationDescriptor>() {
                public int compare(AnnotationDescriptor ad1, AnnotationDescriptor ad2) {
                    return ad1.getClass().getName().compareTo(ad2.getClass().getName());
                }
            });

            this.allAnnotations = allAnnotations;
        }

        return allAnnotations;
    }

}
