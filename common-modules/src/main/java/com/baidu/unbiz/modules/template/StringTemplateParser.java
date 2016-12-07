/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.template;

import java.util.Map;

import com.baidu.unbiz.common.Emptys;
import com.baidu.unbiz.common.StringUtil;

/**
 * 类似Spring中${xxx}变量的解析
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-11 上午9:26:57
 */
public class StringTemplateParser {
    /**
     * 默认的宏标志前缀
     */
    public static final String DEFAULT_MACRO_START = "${";
    /**
     * 默认的宏标志后缀
     */
    public static final String DEFAULT_MACRO_END = "}";

    /**
     * 是否替换缺失键
     */
    protected boolean replaceMissingKey = true;
    /**
     * 缺失键替换值
     */
    protected String missingKeyReplacement;
    /**
     * 是否解析转义字符
     */
    protected boolean resolveEscapes = true;
    /**
     * 宏标志前缀
     */
    protected String macroStart = DEFAULT_MACRO_START;
    /**
     * 宏标志后缀
     */
    protected String macroEnd = DEFAULT_MACRO_END;
    /**
     * char的转义
     */
    protected char escapeChar = '\\';
    /**
     * 是否解析值
     */
    protected boolean parseValues;

    /**
     * 解析
     * 
     * @param template 模版
     * @param macroResolver 宏解析器
     * @return 解析结果
     */
    public String parse(String template, MacroResolver macroResolver) {
        StringBuilder result = new StringBuilder(template.length());

        int i = 0;
        int len = template.length();

        int startLen = macroStart.length();
        int endLen = macroEnd.length();

        while (i < len) {
            int ndx = template.indexOf(macroStart, i);
            if (ndx == -1) {
                result.append(i == 0 ? template : template.substring(i));
                break;
            }

            // check escaped
            int j = ndx - 1;
            boolean escape = false;
            int count = 0;

            while ((j >= 0) && (template.charAt(j) == escapeChar)) {
                escape = !escape;
                if (escape) {
                    count++;
                }
                j--;
            }
            if (resolveEscapes) {
                result.append(template.substring(i, ndx - count));
            } else {
                result.append(template.substring(i, ndx));
            }
            if (escape) {
                result.append(macroStart);
                i = ndx + startLen;
                continue;
            }

            // find macros end
            ndx += startLen;
            int ndx2 = template.indexOf(macroEnd, ndx);
            if (ndx2 == -1) {
                throw new IllegalArgumentException("Invalid string template, unclosed macro at: " + (ndx - startLen));
            }

            // detect inner macros, there is no escaping
            int ndx1 = ndx;
            while (ndx1 < ndx2) {
                int n = StringUtil.indexOf(template, macroStart, ndx1, ndx2);
                if (n == -1) {
                    break;
                }
                ndx1 = n + startLen;
            }

            String name = template.substring(ndx1, ndx2);

            // find value and append
            Object value;
            if (missingKeyReplacement != null || !replaceMissingKey) {
                try {
                    value = macroResolver.resolve(name);
                } catch (Exception ignore) {
                    value = null;
                }

                if (value == null) {
                    if (replaceMissingKey) {
                        value = missingKeyReplacement;
                    } else {
                        value = template.substring(ndx1 - startLen, ndx2 + 1);
                    }
                }
            } else {
                value = macroResolver.resolve(name);
                if (value == null) {
                    value = Emptys.EMPTY_STRING;
                }
            }

            if (ndx == ndx1) {
                String stringValue = value.toString();
                if (parseValues) {
                    if (stringValue.contains(macroStart)) {
                        stringValue = parse(stringValue, macroResolver);
                    }
                }
                result.append(stringValue);
                i = ndx2 + endLen;
            } else {
                // inner macro
                template =
                        template.substring(0, ndx1 - startLen) + value.toString() + template.substring(ndx2 + endLen);
                len = template.length();
                i = ndx - startLen;
            }
        }
        return result.toString();
    }

    public boolean isReplaceMissingKey() {
        return replaceMissingKey;
    }

    public void setReplaceMissingKey(boolean replaceMissingKey) {
        this.replaceMissingKey = replaceMissingKey;
    }

    public String getMissingKeyReplacement() {
        return missingKeyReplacement;
    }

    public void setMissingKeyReplacement(String missingKeyReplacement) {
        this.missingKeyReplacement = missingKeyReplacement;
    }

    public boolean isResolveEscapes() {
        return resolveEscapes;
    }

    public void setResolveEscapes(boolean resolveEscapes) {
        this.resolveEscapes = resolveEscapes;
    }

    public String getMacroStart() {
        return macroStart;
    }

    public void setMacroStart(String macroStart) {
        this.macroStart = macroStart;
    }

    public String getMacroEnd() {
        return macroEnd;
    }

    public void setMacroEnd(String macroEnd) {
        this.macroEnd = macroEnd;
    }

    public char getEscapeChar() {
        return escapeChar;
    }

    public void setEscapeChar(char escapeChar) {
        this.escapeChar = escapeChar;
    }

    public boolean isParseValues() {
        return parseValues;
    }

    public void setParseValues(boolean parseValues) {
        this.parseValues = parseValues;
    }

    /**
     * 宏解析器
     * 
     * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
     * @version create on 2015-7-5 下午9:04:57
     */
    public interface MacroResolver {
        /**
         * 解析
         * 
         * @param macroName 宏名称
         * @return 解析结果
         */
        String resolve(String macroName);

    }

    /**
     * 创建Map组装的宏解析器
     * 
     * @param map 映射表
     * @return 宏解析器 @see MacroResolver
     */
    public static MacroResolver createMapMacroResolver(final Map<?, ?> map) {
        return new MacroResolver() {
            @Override
            public String resolve(String macroName) {
                Object value = map.get(macroName);

                if (value == null) {
                    return null;
                }

                return value.toString();
            }
        };
    }

}
