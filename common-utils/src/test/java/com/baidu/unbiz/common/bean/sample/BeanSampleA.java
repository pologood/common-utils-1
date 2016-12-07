package com.baidu.unbiz.common.bean.sample;

import com.baidu.unbiz.common.lang.StringableSupport;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年11月18日 下午9:04:44
 */
public class BeanSampleA extends StringableSupport {

    protected Integer shared;

    private String fooProp = "abean_value";

    public void setFooProp(String v) {
        fooProp = v;
    }

    public String getFooProp() {
        return fooProp;
    }

    public boolean isSomething() {
        return true;
    }
}
