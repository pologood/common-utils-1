/**
 * 
 */
package com.baidu.unbiz.common.lang;

import com.baidu.unbiz.common.ReflectionUtil;
import com.baidu.unbiz.common.able.ToStringable;
import com.baidu.unbiz.common.cache.FieldCache;

/**
 * 字符串输出，打印对象各 {@link Field java.lang.reflect.Field}
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月16日 下午11:56:52
 */
public class StringableSupport implements ToStringable {

    private static final FieldCache fieldCache = FieldCache.getInstance();

    public String toString() {
        return ReflectionUtil.dump(this, fieldCache.getInstanceFields(this.getClass()));
    }

}
