/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import com.baidu.unbiz.biz.result.ResultCode;
import com.baidu.unbiz.biz.result.ResultCodeMessage;
import com.baidu.unbiz.biz.result.ResultCodeUtil;

/**
 * FtpResult
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:18:43
 */
public enum FtpResultCode implements ResultCode {

    /** 成功执行命令。 */
    SUCCESS,

    /** 参数不合法。 */
    INVALID_PARAM,

    /**
     * 表示抛出未预料到异常，或者<code>isSuccess()</code>为<code>false</code>却未指明具体的 <code>ResultCode</code>。
     */
    GENERIC_FAILURE,

    /**
     * 表示service抛出未预料到异常，或者<code>isSuccess()</code>为<code>false</code>却未指明具体的 <code>ResultCode</code>。
     */
    SERVICE_FAILURE,

    /**
     * 表示DAO抛出未预料到异常，或者<code>isSuccess()</code>为<code>false</code>却未指明具体的 <code>ResultCode</code>。
     */
    DAO_FAILURE,

    /** 执行业务逻辑操作失败。 */
    BIZ_FAILURE,

    /**
     * 执行命令错误，一般会带有捕获的异常
     */
    ERROR;

    private final ResultCodeUtil util = new ResultCodeUtil(this);

    public String getName() {
        return util.getName();
    }

    public ResultCodeMessage getMessage() {
        return util.getMessage();
    }

    public int getCode() {
        return util.getCode();
    }

}
