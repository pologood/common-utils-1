/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.source;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 会意注解，表示被谁使用
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-11-23 上午4:15:36
 */
@Retention(RetentionPolicy.SOURCE)
public @interface UsedBy {

    /**
     * 信息描述
     * 
     * @return 描述
     */
    String[] value();

}
