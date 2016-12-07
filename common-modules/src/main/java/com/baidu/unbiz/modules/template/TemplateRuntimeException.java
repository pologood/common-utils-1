package com.baidu.unbiz.modules.template;

/**
 * 代表执行模板时发生的错误。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:21:26
 */
public class TemplateRuntimeException extends TemplateException {
    private static final long serialVersionUID = -1675637580195040129L;

    public TemplateRuntimeException() {
        super();
    }

    public TemplateRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateRuntimeException(String message) {
        super(message);
    }

    public TemplateRuntimeException(Throwable cause) {
        super(cause);
    }
}
