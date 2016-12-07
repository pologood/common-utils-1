package com.baidu.unbiz.modules.bus;

import java.util.HashMap;

import com.baidu.unbiz.common.StringUtil;

/**
 * 传输的数据
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-1-8 下午10:37:39
 */
public class ActionData<T> extends HashMap<String, String> {

    private static final long serialVersionUID = 3669371717887649410L;

    /**
     * 默认的catalog，即没有
     */
    public static final String NO_CATALOG = "NO_CATALOG";

    /**
     * 类型
     */
    private String catalog;

    /**
     * 总线名称
     */
    private String bus;

    /**
     * 携带数据
     */
    private T payload;

    public ActionData() {
        this(NO_CATALOG);
    }

    public ActionData(String catalog) {
        this.catalog = StringUtil.isBlank(catalog) ? NO_CATALOG : catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
