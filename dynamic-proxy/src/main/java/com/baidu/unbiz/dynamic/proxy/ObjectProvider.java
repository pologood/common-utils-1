package com.baidu.unbiz.dynamic.proxy;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午10:42:08
 */
public interface ObjectProvider<T> extends Serializable {

    T getObject();
}
