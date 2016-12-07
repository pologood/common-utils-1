package com.baidu.unbiz.modules.template;

import static com.baidu.unbiz.common.ArrayUtil.isEmpty;
import static com.baidu.unbiz.common.Assert.assertNotNull;
import static com.baidu.unbiz.common.Assert.ExceptionType.ILLEGAL_STATE;
import static com.baidu.unbiz.common.ExceptionUtil.getRootCause;

import java.io.IOException;

import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 一个将template的内容输出到<code>Appendable</code>的visitor。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:22:00
 */
public abstract class TextWriter<A extends Appendable> extends LoggerSupport implements VisitorInvocationErrorHandler {

    /**
     * 输出对象
     */
    private A out;

    public TextWriter() {
    }

    public TextWriter(A out) {
        setOut(out);
    }

    /**
     * 访问纯文本
     * 
     * @param text 文本
     * @throws IOException
     */
    public final void visitText(String text) throws IOException {
        out().append(text);
    }

    /**
     * 取得用于输出的<code>Appendable</code>实例
     * 
     * @return 用于输出的<code>Appendable</code>实例
     */
    public final A out() {
        return assertNotNull(out, ILLEGAL_STATE, "setOut() not called yet");
    }

    /**
     * 设置用于输出的<code>Appendable</code>实例
     * 
     * @param out 输出对象
     */
    public final void setOut(A out) {
        this.out = out;
    }

    @Override
    public void handleInvocationError(String desc, Throwable e) throws Exception {
        logger.debug("Error rendering " + desc, e);

        e = getRootCause(e);

        String msg = e.getClass().getSimpleName() + " - " + e.getMessage();

        StackTraceElement[] stackTrace = e.getStackTrace();

        if (!isEmpty(stackTrace)) {
            msg += " - " + stackTrace[0];
        }

        out.append(msg);
    }
}
