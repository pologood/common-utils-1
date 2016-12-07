/**
 * 
 */
package com.baidu.unbiz.common.io;

import java.io.IOException;
import java.io.Writer;

/**
 * like {@link FastByteArrayOutputStream}
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月18日 上午3:19:38
 */
public class FastCharArrayWriter extends Writer {

    private final FastCharBuffer buffer;

    public FastCharArrayWriter() {
        this(1024);
    }

    public FastCharArrayWriter(int size) {
        buffer = new FastCharBuffer(size);
    }

    @Override
    public void write(char[] b, int off, int len) {
        buffer.append(b, off, len);
    }

    @Override
    public void write(int b) {
        buffer.append((char) b);
    }

    @Override
    public void write(String s, int off, int len) {
        write(s.toCharArray(), off, len);
    }

    public int size() {
        return buffer.size();
    }

    @Override
    public void close() {
        // nop
    }

    @Override
    public void flush() {
        // nop
    }

    public void reset() {
        buffer.clear();
    }

    public void writeTo(Writer out) throws IOException {
        int index = buffer.index();
        for (int i = 0; i < index; i++) {
            char[] buf = buffer.array(i);
            out.write(buf);
        }
        out.write(buffer.array(index), 0, buffer.offset());
    }

    public char[] toCharArray() {
        return buffer.toArray();
    }

    @Override
    public String toString() {
        return new String(toCharArray());
    }
}