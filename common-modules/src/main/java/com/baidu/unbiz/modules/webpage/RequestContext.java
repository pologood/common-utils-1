package com.baidu.unbiz.modules.webpage;

import static com.baidu.unbiz.common.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.baidu.unbiz.common.Assert.ExceptionType;

/**
 * * 代表一个request/response context。
 * <p>
 * 使用此抽象类，而不是使用标准的servlet api，目的是为了尽量减少对外界api的依赖。
 * </p>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 上午2:28:27
 */
public abstract class RequestContext {
    private final String baseURL;
    private final String resourceName;
    private PrintWriter writer;
    private OutputStream stream;

    public RequestContext(String baseURL, String resourceName) {
        if (!baseURL.endsWith("/")) {
            baseURL += "/";
        }

        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }

        this.baseURL = baseURL;
        this.resourceName = resourceName;
    }

    /** 取得URL中所请求的资源名称。 */
    public final String getResourceName() {
        return resourceName;
    }

    /** 取得基准URL。 */
    public final String getBaseURL() {
        return baseURL;
    }

    /** 取得指定资源名称所对应的URL。 */
    public final String getResourceURL(String resourceName) {
        return ServletUtil.normalizeURI(baseURL + resourceName);
    }

    /**
     * 取得已经取得过的writer。
     *
     * @throws IllegalStateException 如果<code>getWriter(contentType)</code> 还没有被调用过。
     */
    public final PrintWriter getWriter() {
        return assertNotNull(writer, ExceptionType.ILLEGAL_STATE, "call getWriter(contentType) first");
    }

    /** 取得用来输出文本页面的<code>Writer</code>。 */
    public final PrintWriter getWriter(String contentType) throws IOException {
        if (writer == null) {
            writer = doGetWriter(contentType);
        }

        return writer;
    }

    protected abstract PrintWriter doGetWriter(String contentType) throws IOException;

    /**
     * 取得已经取得过的stream。
     *
     * @throws IllegalStateException 如果<code>getOutputStream(contentType)</code> 还没有被调用过。
     */
    public final OutputStream getOutputStream() {
        return assertNotNull(stream, ExceptionType.ILLEGAL_STATE, "call getOutputStream(contentType) first");
    }

    /** 取得用来输出二进制内容的<code>OutputStream</code>。 */
    public final OutputStream getOutputStream(String contentType) throws IOException {
        if (stream == null) {
            stream = doGetOutputStream(contentType);
        }

        return stream;
    }

    protected abstract OutputStream doGetOutputStream(String contentType) throws IOException;

    /** 资源未找到。 */
    public abstract void resourceNotFound(String resourceName) throws IOException;

    /** 重定向页面。 */
    public abstract void redirectTo(String location) throws IOException;

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), resourceName);
    }
}
