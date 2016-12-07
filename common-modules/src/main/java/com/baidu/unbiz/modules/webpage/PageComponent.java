
package com.baidu.unbiz.modules.webpage;

import static com.baidu.unbiz.common.Assert.assertNotNull;
import static com.baidu.unbiz.common.Assert.assertTrue;

import java.net.URL;

import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.modules.template.Template;

/**
 * 代表一个简单的页面组件。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年1月27日 下午3:09:05
 */
public abstract class PageComponent {
    private final PageComponentRegistry registry;
    private final String componentPath;
    private final String componentName;
    private final Template template;

    /** 创建并注册页面组件。 */
    public PageComponent(PageComponentRegistry registry, String componentPath) {
        this.registry = assertNotNull(registry, "pageComponentRegistry");
        this.componentPath = componentPath = normalizeComponentPath(componentPath);

        registry.register(componentPath, this);

        // 根据naming convention创建template
        String className = getClass().getSimpleName();
        StringBuilder name = new StringBuilder(className);

        if (className.endsWith("Component")) {
            name.setLength(name.length() - "Component".length());
        }

        assertTrue(name.length() > 0, "Invalid page component name: %s", className);

        name.setCharAt(0, Character.toLowerCase(name.charAt(0)));

        this.componentName = name.toString();

        URL templateResource = assertNotNull(getClass().getResource(name + ".htm"),
                "Could not find template for page component class: %s", className);

        template = new Template(templateResource);
    }

    /** 规格化componentPath。 */
    static String normalizeComponentPath(String componentPath) {
        // 去除开始的/，确保以/结尾，除非是空路径
        componentPath = ServletUtil.normalizeURI(componentPath).replaceAll("^/|/$", "") + "/";

        if (componentPath.equals("/")) {
            componentPath = Emptys.EMPTY_STRING;
        }

        return componentPath;
    }

    public PageComponentRegistry getRegistry() {
        return registry;
    }

    public String getComponentPath() {
        return componentPath;
    }

    public String getComponentName() {
        return componentName;
    }

    public Template getTemplate() {
        return template;
    }

    /** 生成component资源的URL，供visitor调用。 */
    public String getComponentURL(RequestContext request, String relativeUrl) {
        return request.getResourceURL(getComponentPath() + relativeUrl);
    }
}
