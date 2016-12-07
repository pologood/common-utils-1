package com.baidu.unbiz.dynamic.proxy.sample;

import java.io.IOException;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-9 下午11:31:59
 */
public interface Echo {

    public void echo();

    public String echoBack(String message);

    public String echoBack(String[] messages);

    public String echoBack(String[][] messages);

    public String echoBack(String[][][] messages);

    public int echoBack(int i);

    public boolean echoBack(boolean b);

    public String echoBack(String message1, String message2);

    public void illegalArgument();

    public void ioException() throws IOException;

}
