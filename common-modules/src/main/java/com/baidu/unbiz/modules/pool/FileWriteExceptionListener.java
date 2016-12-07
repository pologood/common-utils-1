package com.baidu.unbiz.modules.pool;

import java.util.List;

/**
 * 写文件的异常监听器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:07:09
 */
public class FileWriteExceptionListener implements ExceptionListener<String> {

    /**
     * 写文件批量执行器 @see FileWriteExecutor
     */
    private FileWriteExecutor fileWriteExecutor;

    @Override
    public void onException(List<String> strings) {
        fileWriteExecutor.execute(strings);
    }

    /**
     * 设置备份目录
     * 
     * @param backupDir 备份目录
     */
    public void setBackupDir(String backupDir) {
        fileWriteExecutor.setFileDir(backupDir);
    }

    /**
     * 设置文件名
     * 
     * @param fileName 设置文件名
     */
    public void setFileName(String fileName) {
        fileWriteExecutor.setFileName(fileName);
    }

    public void setFileWriteExecutor(FileWriteExecutor fileWriteExecutor) {
        this.fileWriteExecutor = fileWriteExecutor;
    }

}
