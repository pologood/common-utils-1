package com.baidu.unbiz.common.logger.log4j;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.baidu.unbiz.common.logger.LogConfigurator;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 上午10:53:54
 */
public class Log4jConfigurator extends LogConfigurator {
    @Override
    protected void doConfigure(URL configFile, Map<String, String> propsMap) {
        Properties props = new Properties();
        props.putAll(propsMap);

        DOMConfigurator.configure(configFile, props);
    }

    @Override
    public void shutdown() {
    }
}
