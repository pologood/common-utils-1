/**
 * 
 */
package com.baidu.unbiz.modules.template;

import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.StringUtil;

/**
 * 调用指定visitor中的visitPlaceholder()方法。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:17:19
 */
public class FallbackToVisitor {
    /**
     * 访问者
     */
    private final Object visitor;

    public FallbackToVisitor(Object visitor) {
        this.visitor = Assert.assertNotNull(visitor, "fallback to visitor");
    }

    public Object getVisitor() {
        return visitor;
    }

    /**
     * 调用指定visitor中的visitPlaceholder()方法。
     * 
     * @param name 名称
     * @param params 参数数组
     * @return 是否成功
     * @throws Exception
     */
    public boolean visitPlaceholder(String name, Object[] params) throws Exception {
        try {
            visitor.getClass().getMethod("visit" + StringUtil.capitalize(name)).invoke(visitor);
        } catch (NoSuchMethodException e) {
            if (visitor instanceof FallbackVisitor) {
                return ((FallbackVisitor) visitor).visitPlaceholder(name, params);
            }
            return false;
        }

        return true;
    }
}
