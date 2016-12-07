package com.baidu.unbiz.modules.pool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.common.FileUtil;
import com.baidu.unbiz.common.io.StreamUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 写文件的批处理执行器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-3-10 上午11:07:55
 */
public class FileWriteExecutor extends LoggerSupport implements IBatchExecutor<String> {
    /**
     * 文件目录
     */
    private String fileDir;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * The default maximum file size is 64MB.
     */
    private int maxFileSize = 64 * 1024 * 1024;

    @Override
    public void execute(List<String> strings) {
        logger.debug("execute - size=[{}]", strings.size());

        // FIXME 缓存index
        File file = null;
        try {
            file = genFile(0);
        } catch (IOException e) {
            logger.error("", e);
        }
        if (file != null) {
            writeToFile(file, strings);
        }
    }

    /**
     * 生成文件
     * 
     * @param index 索引号
     * @return 文件
     * @throws IOException
     */
    File genFile(int index) throws IOException {
        String filepath =
                fileDir + File.separator + DateUtil.formatDate(new Date(), "yyyy-MM-dd") + File.separator + fileName
                        + "." + index;

        File file = new File(filepath);
        if (file.exists()) {
            int fileSize = FileUtil.getFileSize(filepath);
            if (fileSize >= maxFileSize) {
                index = index + 1;
                return genFile(index);
            }

            return file;
        }

        logger.info("create file [{}]", filepath);

        try {
            return FileUtil.createAndReturnFile(filepath);
        } catch (Exception e) {
            logger.error(">>>> Create File Exception: ", e);
            return null;
        }

    }

    /**
     * 将记录写入文件
     * 
     * @param file 文件
     * @param strings 记录信息
     */
    private void writeToFile(File file, List<String> strings) {
        BufferedWriter bufferedOut = null;
        try {

            FileOutputStream out = new FileOutputStream(file, true);
            bufferedOut = new BufferedWriter(new PrintWriter(out));
            for (String s : strings) {
                bufferedOut.write(s + "\n");
            }

            bufferedOut.flush();
        } catch (Exception e) {
            logger.error(">>>> Excute write Exception: ", e);
        } finally {
            StreamUtil.close(bufferedOut);
        }
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
