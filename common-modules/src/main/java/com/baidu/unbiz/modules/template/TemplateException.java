package com.baidu.unbiz.modules.template;

/**
 * 代表一个通用的模板异常。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:20:41
 */
public class TemplateException extends RuntimeException {
    private static final long serialVersionUID = -2283261056076685158L;

    public TemplateException() {
        super();
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }
}
