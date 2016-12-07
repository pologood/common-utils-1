/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp.exception;

import java.io.IOException;

/**
 * ControlChannelIOException
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:43:40
 */
public class ControlChannelIOException extends IOException {

    private static final long serialVersionUID = 7082740758898725631L;

    public ControlChannelIOException() {
        super();
    }

    public ControlChannelIOException(String message) {
        super(message);
    }

}
