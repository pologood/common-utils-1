/**
 * 
 */
package com.baidu.unbiz.common.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import com.baidu.unbiz.common.StringPool;
import com.baidu.unbiz.common.able.Processable;
import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 代表一个进程处理
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月31日 下午8:29:32
 */
public abstract class ProcessClosure extends LoggerSupport implements Processable, StringPool.Charset {

    protected static final int OK = 0;

    protected String info;

    protected String encode = GBK;

    protected int exitCode;

    @Override
    public void execute(Object...inputs) {
        for (Object cmd : inputs) {
            if (cmd.getClass().isArray()) {
                this.execute(environment(object2Array(cmd)));
                continue;
            }

            this.execute(environment(cmd.toString()));
        }
    }

    @Override
    public void execute(String...inputs) {
        try {
            work(environment(inputs));
        } catch (Exception e) {
            logger.error("execute error", e);
        }
    }

    @Override
    public void execute(String input) {
        try {
            work(environment(input));
        } catch (Exception e) {
            logger.error("execute error", e);
        }
    }

    private void work(String...cmds) throws Exception {
        // just test StringBander
        StringBander commandLine = new StringBander(cmds.length * 2);

        for (String command : cmds) {
            commandLine.append(command);
            commandLine.append(StringPool.Symbol.SPACE);
        }

        Process process = Runtime.getRuntime().exec(commandLine.toString().trim(), null, null);

        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), encode);
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), encode);
        int exitCode = process.waitFor();

        RunnableFuture<String> resultFuture = new FutureTask<String>(outputGobbler);
        RunnableFuture<String> errorFuture = new FutureTask<String>(errorGobbler);

        errorFuture.run();
        resultFuture.run();
        info = resultFuture.get();

        this.exitCode = exitCode;
    }

    private String[] object2Array(Object obj) {
        Object[] cmds = (Object[]) obj;
        int length = cmds.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = cmds[i].toString();
        }
        return result;
    }

    @Override
    public boolean isSucces() {
        return exitCode == OK;
    }

    public String getInfo() {
        return info;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public class StreamGobbler implements Callable<String> {

        protected final InputStream is;

        protected final String encode;

        public StreamGobbler(InputStream is, String encode) {
            this.is = is;
            this.encode = encode;
        }

        @Override
        public String call() throws Exception {
            StringBuilder builder = new StringBuilder();

            try {
                InputStreamReader isr = new InputStreamReader(is, encode);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    logger.info(line);
                    builder.append(line).append("\n");
                }

                return builder.toString();
            } catch (IOException e) {
                logger.error("call error", e);
                return e.toString();
            }

        }
    }

    /**
     * 环境化
     * 
     * @param inputs 命令参数
     * @return 环境化后的命令参数
     */
    protected abstract String[] environment(String...inputs);

}
