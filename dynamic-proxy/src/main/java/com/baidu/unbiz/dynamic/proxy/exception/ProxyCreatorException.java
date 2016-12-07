package com.baidu.unbiz.dynamic.proxy.exception;

import com.baidu.unbiz.common.MessageUtil;
import com.baidu.unbiz.common.exception.MessageRuntimeException;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:52:50
 */
public class ProxyCreatorException extends MessageRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5278506190465957728L;

    /**
     * 构造一个空的异常.
     */
    public ProxyCreatorException() {
        super();
    }

    /**
     * 构造一个异常, 指明异常的详细信息.
     * 
     * @param message 详细信息
     */
    public ProxyCreatorException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param cause 异常的起因
     */
    public ProxyCreatorException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param message 详细信息
     * @param cause 异常的起因
     */
    public ProxyCreatorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个异常, 参数化详细信息
     * 
     * @param message 详细信息
     * @param params 参数表
     */
    public ProxyCreatorException(String message, Object...params) {
        super(MessageUtil.formatLogMessage(message, params));
    }

    /**
     * 构造一个异常, 参数化详细信息,指明引起这个异常的起因
     * 
     * @param message 详细信息
     * @param cause 异常的起因
     * @param params 参数表
     */
    public ProxyCreatorException(String message, Throwable cause, Object...params) {
        super(MessageUtil.formatLogMessage(message, params), cause);
    }

}
