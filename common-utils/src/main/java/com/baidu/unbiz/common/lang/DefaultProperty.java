package com.baidu.unbiz.common.lang;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.able.Propertyable;

/**
 * 默认的键值对实现
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午5:50:52
 */
public class DefaultProperty extends StringableSupport implements Propertyable<Object>, Serializable {

    private static final long serialVersionUID = 7718207939067495513L;
    
    private Map<String, Object> properties = CollectionUtil.createHashMap();

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties.clear();

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (null != entry.getValue()) {
                this.properties.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Propertyable<Object> setAndReturn(String key, Object value) {
        properties.put(key, value);

        return this;
    }

}
