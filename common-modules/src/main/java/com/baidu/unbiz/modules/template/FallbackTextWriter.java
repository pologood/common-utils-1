package com.baidu.unbiz.modules.template;

import java.io.IOException;
import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;

/**
 * 一个将template的内容输出到<code>Appendable</code> 的visitor，且当遇到未定义的placeholder时，不会报错，而是从内部的context中取值。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:16:15
 */
public class FallbackTextWriter<A extends Appendable> extends TextWriter<A> implements FallbackVisitor {
    private final Map<String, Object> context = CollectionUtil.createHashMap();

    public FallbackTextWriter() {
        super();
    }

    public FallbackTextWriter(A out) {
        super(out);
    }

    public Map<String, Object> context() {
        return context;
    }

    public FallbackTextWriter<A> set(String key, Object value) {
        context.put(key, value);
        return this;
    }

    @Override
    public boolean visitPlaceholder(String name, Object[] params) throws IOException {
        if (context.containsKey(name)) {
            Object value = context.get(name);

            if (value != null) {
                out().append(value.toString());
            }
        } else {
            out().append("${").append(name).append("}");
        }

        return true;
    }
}
