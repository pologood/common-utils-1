package com.baidu.unbiz.modules.event;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 事件类型
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月15日 上午2:25:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Action {

    /**
     * 事件名称
     * 
     * @return 事件名称
     */
    String event() default "";
}
