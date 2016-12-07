package com.baidu.unbiz.dynamic.proxy.template;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.baidu.unbiz.common.CollectionUtil;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:46:02
 */
public class ClassCache {

    private final Map<ClassLoader, Map<Set<Class<?>>, WeakReference<Class<?>>>> loaderToClassCache = CollectionUtil
            .createWeakHashMap();
    
    private final ClassGenerator proxyClassGenerator;

    public ClassCache(ClassGenerator proxyClassGenerator) {
        this.proxyClassGenerator = proxyClassGenerator;
    }

    private Map<Set<Class<?>>, WeakReference<Class<?>>> getClassCache(ClassLoader classLoader) {
        Map<Set<Class<?>>, WeakReference<Class<?>>> cache = loaderToClassCache.get(classLoader);
        if (cache == null) {
            cache = CollectionUtil.createHashMap();
            loaderToClassCache.put(classLoader, cache);
        }

        return cache;
    }

    private Set<Class<?>> toClassCacheKey(Class<?>[] proxyClasses) {
        return CollectionUtil.createHashSet(Arrays.asList(proxyClasses));
    }

    public synchronized Class<?> getProxyClass(ClassLoader classLoader, Class<?>[] proxyClasses) {
        Map<Set<Class<?>>, WeakReference<Class<?>>> classCache = getClassCache(classLoader);
        Set<Class<?>> key = toClassCacheKey(proxyClasses);
        Class<?> proxyClass;
        Reference<Class<?>> proxyClassReference = classCache.get(key);

        if (proxyClassReference == null) {
            proxyClass = proxyClassGenerator.generateProxyClass(classLoader, proxyClasses);
            classCache.put(key, new WeakReference<Class<?>>(proxyClass));
        } else {
            synchronized (proxyClassReference) {
                proxyClass = proxyClassReference.get();
                if (proxyClass == null) {
                    proxyClass = proxyClassGenerator.generateProxyClass(classLoader, proxyClasses);
                    classCache.put(key, new WeakReference<Class<?>>(proxyClass));
                }
            }
        }

        return proxyClass;
    }
}
