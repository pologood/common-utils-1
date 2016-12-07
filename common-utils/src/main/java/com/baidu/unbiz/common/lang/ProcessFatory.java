/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.lang;

import com.baidu.unbiz.common.ArrayUtil;
import com.baidu.unbiz.common.SystemUtil;
import com.baidu.unbiz.common.SystemUtil.OsInfo;
import com.baidu.unbiz.common.able.Processable;

/**
 * 创建系统进程的工厂
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-24 下午1:12:07
 */
public abstract class ProcessFatory {

    /**
     * 根据当前系统创建进程
     * 
     * @return 系统进程 @see Processable
     */
    public static Processable create() {
        OsInfo os = SystemUtil.getOsInfo();
        if (os.isWindows()) {
            return new Windows();
        }

        if (os.isMac()) {
            return new Mac();
        }

        return new Linux();
    }

    static class Windows extends ProcessClosure {
        @Override
        protected String[] environment(String...inputs) {
            return ArrayUtil.addAll(new String[] { "cmd", "/c" }, inputs);
        }
    }

    static class Linux extends ProcessClosure {
        @Override
        protected String[] environment(String...inputs) {
            return ArrayUtil.addAll(new String[] { "sh", "-c" }, inputs);
        }
    }

    static class Mac extends ProcessClosure {
        @Override
        protected String[] environment(String...inputs) {
            // if ext is sh ?FIXME
            return ArrayUtil.addAll(new String[] { "sh -c" }, inputs);
        }
    }

}
