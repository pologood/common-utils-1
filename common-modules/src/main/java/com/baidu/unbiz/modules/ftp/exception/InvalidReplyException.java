package com.baidu.unbiz.modules.ftp.exception;

/**
 * MalformedReplyException
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午5:53:51
 */
public class InvalidReplyException extends FtpException {

    private static final long serialVersionUID = 718494116070892846L;

    public InvalidReplyException(String message) {
        super(message);
    }

    public InvalidReplyException(String message, Object...params) {
        super(message, params);
    }

}
