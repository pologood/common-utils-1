/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 可“停止”的接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-5 上午2:06:32
 */
public interface Stopable {
    /**
     * 停止
     * 
     * @throws Exception
     */
    void stop() throws Exception;
}
