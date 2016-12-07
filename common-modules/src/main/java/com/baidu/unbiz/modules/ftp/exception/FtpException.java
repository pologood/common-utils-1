/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp.exception;

import com.baidu.unbiz.common.MessageUtil;
import com.baidu.unbiz.common.exception.MessageException;
import com.baidu.unbiz.modules.ftp.FtpResponse;

/**
 * FTPException
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:15:31
 */
public class FtpException extends MessageException {

    private static final long serialVersionUID = -6927755626117584666L;

    private int replyCode = -1;

    /**
     * 构造一个空的异常.
     */
    public FtpException() {
        super();
    }

    /**
     * 构造一个异常, 指明异常的详细信息.
     * 
     * @param message 详细信息
     */
    public FtpException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param cause 异常的起因
     */
    public FtpException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     * 
     * @param message 详细信息
     * @param cause 异常的起因
     */
    public FtpException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个异常, 参数化详细信息
     * 
     * @param message 详细信息
     * @param params 参数表
     */
    public FtpException(String message, Object...params) {
        super(MessageUtil.formatLogMessage(message, params));
    }

    /**
     * 构造一个异常, 参数化详细信息,指明引起这个异常的起因
     * 
     * @param message 详细信息
     * @param cause 异常的起因
     * @param params 参数表
     */
    public FtpException(String message, Throwable cause, Object...params) {
        super(MessageUtil.formatLogMessage(message, params), cause);
    }

    public FtpException(FtpResponse reply) {
        super(reply.getReplyText());

        // extract reply code if possible
        try {
            this.replyCode = Integer.parseInt(reply.getReplyCode());
        } catch (NumberFormatException ex) {
            this.replyCode = -1;
        }
    }

    public int getReplyCode() {
        return replyCode;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(getClass().getName());
        b.append(": ");
        if (replyCode > 0) {
            b.append(replyCode).append(" ");
        }

        b.append(getMessage());
        return b.toString();
    }

}
