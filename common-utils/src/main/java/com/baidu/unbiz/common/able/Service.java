/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 代表一个服务
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-13 下午2:34:54
 */
public interface Service extends Startable, Stopable {

    /**
     * 是否激活
     * 
     * @return 如果是则返回<code>true</code>
     */
    boolean isActive();

}
