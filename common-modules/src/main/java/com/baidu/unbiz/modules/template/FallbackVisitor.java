package com.baidu.unbiz.modules.template;

/**
 * 当visit方法找不到时，调用此visitor。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:17:43
 */
public interface FallbackVisitor {
    /**
     * 访问<code>${placeholder:param1, param2, ...}</code>。 返回<code>true</code> 表示访问成功，否则还是会抛出
     * <code>NoSuchMethodException</code>。
     * 
     * @param name 名称
     * @param params 参数数组
     * @return 是否成功
     * @throws Exception
     */
    boolean visitPlaceholder(String name, Object[] params) throws Exception;
}
