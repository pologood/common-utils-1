/**
 * 
 */
package com.baidu.unbiz.common;

import org.junit.Test;

import com.baidu.unbiz.common.logger.CachedLogger;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月18日 下午4:00:13
 */
public class NetworkUtilTest extends CachedLogger {

    @Test
    public void getLocalHostname() {
        // rdqawin32-023 10.94.22.70
        // assertEquals("rdqawin32-023", NetworkUtil.getLocalHostname());
        logger.info(NetworkUtil.getLocalHostname());
        logger.info(NetworkUtil.getLocalHostname());
    }

    @Test
    public void getLocalHostIp() {
        logger.info(NetworkUtil.getLocalHostIp());
        // assertEquals("10.94.22.70", NetworkUtil.getLocalHostIp());
    }

}
