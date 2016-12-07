package com.baidu.unbiz.common.internal;

import java.util.ArrayList;

import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.common.StringUtil;

/**
 * 支持分级缩进的string builder。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 上午11:01:16
 */
public class IndentableStringBuilder extends NormalizableStringBuilder<IndentableStringBuilder> {
    private final IndentStack indents = new IndentStack();
    private final int defaultIndent;
    private int indentLevel;
    private int quoteLevel;
    private boolean lazyAppendNewLine; // 推迟输出换行，推迟到下一个字符被输出前
    private boolean lazyStartHangingIndent; // 推迟启动缩进，推迟到下一个换行后或下一个start()。其效果为悬挂缩进
    private int hangingIndent;

    public IndentableStringBuilder() {
        this(-1);
    }

    public IndentableStringBuilder(int indent) {
        this.defaultIndent = indent <= 0 ? 2 : indent;
    }

    @Override
    public void clear() {
        super.clear();

        indents.clear();
        indentLevel = 0;
        quoteLevel = 0;
        lazyAppendNewLine = false;
        lazyStartHangingIndent = false;
        hangingIndent = 0;
    }

    /** 此处收到的字符中，所有 CR/LF/CRLF 均已被规格化成统一的LF了。 */
    @Override
    protected void visit(char c) {
        boolean newLine = endsWithNewLine();

        if (c == LF && lazyStartHangingIndent) {
            appendInternalNewLine();
            doStartHanglingIndentIfRequired();
            return;
        }

        // 在end quote后追加换行
        if (!newLine && lazyAppendNewLine) {
            appendInternalNewLine();
            newLine = true;
        }

        // 输出begin quotes
        for (; quoteLevel < indentLevel; quoteLevel++) {
            String beginQuote = indents.getBeginQuote(quoteLevel);

            if (StringUtil.isEmpty(beginQuote)) {
                if (!newLine && indents.independent(quoteLevel)) {
                    appendInternalNewLine();
                    newLine = true;
                }
            } else {
                if (newLine) {
                    appendIndent(quoteLevel);
                } else {
                    if (!endsWith(" ")) {
                        appendInternal(" "); // begin quote前空一格
                    }
                }

                appendInternal(beginQuote);
                appendInternalNewLine();

                newLine = true;
            }
        }

        lazyAppendNewLine = false;

        // 输出字符
        if (c == LF) {
            appendInternalNewLine();
        } else {
            if (newLine) {
                appendIndent(indentLevel);
            }

            appendInternal(c);
        }
    }

    /** 创建一级缩进。 */
    public IndentableStringBuilder start() {
        return start(null, null, -1);
    }

    /** 创建一级缩进。 */
    public IndentableStringBuilder start(int indent) {
        return start(null, null, indent);
    }

    /** 创建一级缩进，使用指定的前后括弧。 */
    public IndentableStringBuilder start(String beginQuote, String endQuote) {
        return start(beginQuote, endQuote, -1);
    }

    /** 创建一级缩进，使用指定的前后括弧。 */
    public IndentableStringBuilder start(String beginQuote, String endQuote, int indent) {
        doStartHanglingIndentIfRequired();
        indents.pushIndent(beginQuote, endQuote, indent);
        indentLevel++;
        return this;
    }

    /** 从下一个换行或start()开始悬挂缩进。 */
    public IndentableStringBuilder startHangingIndent() {
        return startHangingIndent(0);
    }

    /** 从下一个换行或start()开始悬挂缩进。 */
    public IndentableStringBuilder startHangingIndent(int indentOffset) {
        doStartHanglingIndentIfRequired();

        lazyStartHangingIndent = true;

        if (!lazyAppendNewLine && lineLength() - currentIndent() > 0 && quoteLevel >= indentLevel) {
            hangingIndent = defaultIndent(lineLength() - currentIndent() + indentOffset);
        } else {
            hangingIndent = defaultIndent(indentOffset);
        }

        return this;
    }

    /** 确保悬挂缩进（如果有的话）已经启动。 */
    private void doStartHanglingIndentIfRequired() {
        if (lazyStartHangingIndent) {
            lazyStartHangingIndent = false;
            start(Emptys.EMPTY_STRING, Emptys.EMPTY_STRING, hangingIndent);
        }
    }

    /** 结束一级缩进。注意，输出结果之前，须至少调用一次end()，以确保最后的换行可以被输出。 */
    public IndentableStringBuilder end() {
        flush();

        // 结束未发生的悬挂缩进
        if (lazyStartHangingIndent) {
            if (!endsWithNewLine()) {
                lazyAppendNewLine = true;
            }

            lazyStartHangingIndent = false;
            return this;
        }

        // 对于刚开始就结束的，不输出end quote
        if (indentLevel > quoteLevel) {
            indentLevel--;
        } else {
            Assert.assertTrue(indentLevel == quoteLevel, "indentLevel != quoteLevel");

            if (indentLevel > 0) {
                indentLevel--;
                quoteLevel--;

                String endQuote = indents.getEndQuote(indentLevel);

                if (StringUtil.isNotEmpty(endQuote)) {
                    // 确保end quote之前换行
                    if (!endsWithNewLine()) {
                        appendInternalNewLine();
                    }

                    // 输出end quote
                    appendIndent(indentLevel);
                    appendInternal(endQuote);
                }

                lazyAppendNewLine = true;
            }
        }

        indents.popIndent();

        return this;
    }

    /** 取得当前缩进的数量。 */
    public int currentIndent() {
        return indents.getCurrentIndent();
    }

    /** 如果indent未指定，则取得默认indent。 */
    private int defaultIndent(int indent) {
        return indent <= 0 ? defaultIndent : indent;
    }

    private void appendIndent(int indentLevel) {
        int indent = indents.getIndent(indentLevel - 1);

        for (int j = 0; j < indent; j++) {
            appendInternal(' ');
        }
    }

    /** 存放缩进信息的栈。 */
    private class IndentStack extends ArrayList<Object> {
        private static final long serialVersionUID = -876139304840511103L;
        private static final int ENTRY_SIZE = 4;

        public String getBeginQuote(int indentLevel) {
            if (indentLevel < 0 || indentLevel >= depth()) {
                return Emptys.EMPTY_STRING;
            }

            return (String) super.get(indentLevel * ENTRY_SIZE);
        }

        public String getEndQuote(int indentLevel) {
            if (indentLevel < 0 || indentLevel >= depth()) {
                return Emptys.EMPTY_STRING;
            }

            return (String) super.get(indentLevel * ENTRY_SIZE + 1);
        }

        public int getIndent(int indentLevel) {
            if (indentLevel < 0 || indentLevel >= depth()) {
                return 0;
            }

            return (Integer) super.get(indentLevel * ENTRY_SIZE + 2);
        }

        /** 如果当前level依附于后一个level，则返回false。 */
        public boolean independent(int indentLevel) {
            if (indentLevel < 0 || indentLevel >= depth() - 1) {
                return true;
            }

            int i1 = (Integer) super.get(indentLevel * ENTRY_SIZE + 3);
            int i2 = (Integer) super.get((indentLevel + 1) * ENTRY_SIZE + 3);

            return i1 != i2;
        }

        public int getCurrentIndent() {
            int depth = depth();

            if (depth > 0) {
                return getIndent(depth - 1);
            } else {
                return 0;
            }
        }

        public int depth() {
            return super.size() / ENTRY_SIZE;
        }

        public void pushIndent(String beginQuote, String endQuote, int indent) {
            super.add(StringUtil.defaultIfNull(beginQuote, "{"));
            super.add(StringUtil.defaultIfNull(endQuote, "}"));
            super.add(defaultIndent(indent) + getCurrentIndent());
            super.add(length());
        }

        public void popIndent() {
            int length = super.size();

            if (length > 0) {
                for (int i = 0; i < ENTRY_SIZE; i++) {
                    super.remove(--length);
                }
            }
        }
    }
}
