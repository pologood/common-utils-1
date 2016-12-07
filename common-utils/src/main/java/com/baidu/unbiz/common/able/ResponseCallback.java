/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

/**
 * 通信后回调接口
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-7 上午12:59:30
 */
public interface ResponseCallback<T> extends ExceptionListener {

    void onSuccess(T result);

}
