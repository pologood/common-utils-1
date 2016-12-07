package com.baidu.unbiz.modules.pool;

import java.util.List;

import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 批处理回声器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:07:27
 */
public class EchoExecutor extends LoggerSupport implements IBatchExecutor<String> {

    @Override
    public void execute(List<String> records) {
        for (String record : records) {
            logger.info(record);
        }
    }

}
