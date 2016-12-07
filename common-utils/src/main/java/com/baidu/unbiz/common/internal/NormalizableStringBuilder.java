/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.internal;

import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.StringUtil;

/**
 * 将CR/LF/CRLF统一成LF的string builder。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月19日 上午3:37:21
 */
abstract class NormalizableStringBuilder<B extends NormalizableStringBuilder<B>> implements Appendable {
    protected static final char CR = '\r';
    protected static final char LF = '\n';
    private static final char NONE = '\0';
    private final StringBuilder out = new StringBuilder();
    private final String newLine;
    private int newLineStartIndex = 0;
    private char readAheadBuffer = '\0';

    public NormalizableStringBuilder() {
        this(null);
    }

    public NormalizableStringBuilder(String newLine) {
        this.newLine = StringUtil.defaultIfNull(newLine, String.valueOf(LF));
    }

    /** 清除所有内容。 */
    public void clear() {
        out.setLength(0);
        newLineStartIndex = 0;
        readAheadBuffer = '\0';
    }

    /** 取得buffer中内容的长度。 */
    public final int length() {
        return out.length();
    }

    /** 取得当前行的长度。 */
    public final int lineLength() {
        return out.length() - newLineStartIndex;
    }

    /** <code>Appendable</code>接口方法。 */
    public final B append(CharSequence csq) {
        return append(csq, 0, csq.length());
    }

    /** <code>Appendable</code>接口方法。 */
    public final B append(CharSequence csq, int start, int end) {
        for (int i = start; i < end; i++) {
            append(csq.charAt(i));
        }

        return thisObject();
    }

    /** <code>Appendable</code>接口方法。 */
    public final B append(char c) {
        // 将 CR|LF|CRLF 转化成统一的 LF
        switch (readAheadBuffer) {
            case NONE:
                switch (c) {
                    case CR: // \r
                        readAheadBuffer = CR;
                        break;

                    case LF: // \n
                        readAheadBuffer = NONE;
                        visit(LF);
                        break;

                    default:
                        readAheadBuffer = NONE;
                        visit(c);
                        break;
                }

                break;

            case CR:
                switch (c) {
                    case CR: // \r\r
                        readAheadBuffer = CR;
                        visit(LF);
                        break;

                    case LF: // \r\n
                        readAheadBuffer = NONE;
                        visit(LF);
                        break;

                    default:
                        readAheadBuffer = NONE;
                        visit(LF);
                        visit(c);
                        break;
                }

                break;

            default:
                Assert.unreachableCode();
                break;
        }

        return thisObject();
    }

    /** 子类覆盖此方法，以便接收所有字符。其中，所有 CR/LF/CRLF 均已被规格化成统一的LF了。 */
    protected abstract void visit(char c);

    /** 子类通过此方法向内部buffer中添加内容。 */
    protected final void appendInternal(String s) {
        out.append(s);
    }

    /** 子类通过此方法向内部buffer中添加内容。 */
    protected final void appendInternal(char c) {
        out.append(c);
    }

    /**
     * 子类通过此方法向内部buffer中添加换行。
     * <p>
     * 子类必须通过此方法来换行，否则<code>newLineStartIndex</code>会不正确。
     * </p>
     */
    protected final void appendInternalNewLine() {
        out.append(newLine);
        newLineStartIndex = out.length();
    }

    /** 判断buf是否以指定字符串结尾。 */
    public final boolean endsWith(String testStr) {
        if (testStr == null) {
            return false;
        }

        int testStrLength = testStr.length();
        int bufferLength = out.length();

        if (bufferLength < testStrLength) {
            return false;
        }

        int baseIndex = bufferLength - testStrLength;

        for (int i = 0; i < testStrLength; i++) {
            if (out.charAt(baseIndex + i) != testStr.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /** 判断out是否以换行结尾，或者是空buffer。 */
    public final boolean endsWithNewLine() {
        return out.length() == 0 || endsWith(newLine);
    }

    private B thisObject() {
        @SuppressWarnings("unchecked")
        B buf = (B) this;
        return buf;
    }

    /** 确保最后一个换行被输出。 */
    public final void flush() {
        if (readAheadBuffer == CR) {
            readAheadBuffer = NONE;
            visit(LF);
        }
    }

    @Override
    public final String toString() {
        return out.toString();
    }
}
