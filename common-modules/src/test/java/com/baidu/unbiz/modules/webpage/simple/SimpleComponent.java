package com.baidu.unbiz.modules.webpage.simple;

import java.io.IOException;
import java.io.PrintWriter;

import com.baidu.unbiz.modules.template.FallbackTextWriter;
import com.baidu.unbiz.modules.webpage.PageComponent;
import com.baidu.unbiz.modules.webpage.PageComponentRegistry;
import com.baidu.unbiz.modules.webpage.RequestContext;

/**
 * SimpleComponent
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 下午11:33:06
 */
public class SimpleComponent extends PageComponent {
    public SimpleComponent(PageComponentRegistry registry, String componentPath) {
        super(registry, componentPath);
    }

    public void visitTemplate(PrintWriter out, RequestContext request) {
        getTemplate().accept(new SimpleVisitor(out, request));
    }

    @SuppressWarnings("unused")
    private class SimpleVisitor extends FallbackTextWriter<PrintWriter> {
        private final RequestContext request;

        private SimpleVisitor(PrintWriter out, RequestContext request) {
            super(out);
            this.request = request;
        }

        public void visitUrl(String relativeUrl) throws IOException {
            out().append(getComponentURL(request, relativeUrl));
        }
    }
}
