package com.baidu.unbiz.modules.webpage;

/**
 * 代表一个简单的组件注册表。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 上午2:28:15
 */
public interface PageComponentRegistry {
    /** 注册组件。 */
    void register(String componentPath, PageComponent component);

    /** 取得所有的componentPaths。 */
    String[] getComponentPaths();

    /** 取得指定名称的组件。 */
    <PC extends PageComponent> PC getComponent(String componentPath, Class<PC> componentClass);
}
