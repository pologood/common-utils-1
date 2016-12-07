package com.baidu.unbiz.modules.template;

/**
 * 代表解析模板时发生的错误。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:21:11
 */
public class TemplateParseException extends TemplateException {
    private static final long serialVersionUID = -2092817090358032551L;

    public TemplateParseException() {
        super();
    }

    public TemplateParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateParseException(String message) {
        super(message);
    }

    public TemplateParseException(Throwable cause) {
        super(cause);
    }
}
