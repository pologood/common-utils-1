/**
 * 
 */
package com.baidu.unbiz.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 非同步的<code>ByteArrayInputStream</code>替换方案。本代码移植自IBM developer works文章：
 * <ul>
 * <li><a href="http://www.ibm.com/developerworks/cn/java/j-io1/index.shtml">彻底转变流，第 1 部分：从输出流中读取</a>
 * <li><a href="http://www.ibm.com/developerworks/cn/java/j-io2/index.shtml">彻底转变流，第 2 部分：优化 Java 内部 I/O</a>
 * </ul>
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月19日 上午3:25:03
 */
public class ByteArrayInputStream extends InputStream {
    // buffer from which to read
    private byte[] buffer;
    private int index;
    private int limit;
    private int mark;

    // is the stream closed?
    private boolean closed;

    public ByteArrayInputStream(byte[] data) {
        this(data, 0, data.length);
    }

    public ByteArrayInputStream(byte[] data, int offset, int length) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || offset + length > data.length || length < 0) {
            throw new IndexOutOfBoundsException();
        }

        buffer = data;
        index = offset;
        limit = offset + length;
        mark = offset;

    }

    @Override
    public int read() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        if (index >= limit) {
            return -1; // EOF
        }
        return buffer[index++] & 0xff;
    }

    @Override
    public int read(byte[] data, int offset, int length) throws IOException {
        if (data == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || offset + length > data.length || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (closed) {
            throw new IOException("Stream closed");
        }
        if (index >= limit) {
            return -1; // EOF
        }

        // restrict length to available data
        if (length > limit - index) {
            length = limit - index;
        }

        // copy out the subarray
        System.arraycopy(buffer, index, data, offset, length);
        index += length;
        return length;

    }

    @Override
    public long skip(long amount) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        if (amount <= 0) {
            return 0;
        }

        // restrict amount to available data
        if (amount > limit - index) {
            amount = limit - index;
        }

        index += (int) amount;
        return amount;

    }

    @Override
    public int available() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        return limit - index;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public void mark(int readLimit) {
        mark = index;
    }

    @Override
    public void reset() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        index = mark;
    }

    @Override
    public boolean markSupported() {
        return true;
    }
}
