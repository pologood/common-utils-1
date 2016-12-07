/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 可“启动”的接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-5 上午2:06:21
 */
public interface Startable {

    /**
     * 启动
     * 
     * @throws Exception
     */
    void start() throws Exception;

}
