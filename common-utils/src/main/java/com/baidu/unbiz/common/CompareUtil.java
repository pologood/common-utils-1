/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common;

/**
 * Compare Util
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-21 下午7:06:41
 */
public abstract class CompareUtil {

    public static <T extends Comparable<? super T>> boolean between(T key, T begin, T end) {
        if (ObjectUtil.isAnyNull(key, begin, end)) {
            return false;
        }

        if (begin.compareTo(end) > 0) {
            return false;
        }

        // FIXME
        return key.compareTo(begin) >= 0 && key.compareTo(end) < 0;
    }

}
