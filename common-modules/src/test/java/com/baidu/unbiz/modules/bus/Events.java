package com.baidu.unbiz.modules.bus;

/**
 * Events
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 下午2:03:11
 */
public class Events {

    public Events(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;
}
