/**
 * 
 */
package com.baidu.unbiz.common;

import java.io.UnsupportedEncodingException;

/**
 * 字符相关的工具类。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月22日 下午1:14:57
 */
public abstract class CharUtil implements StringPool.Charset {

    private static final String CHAR_STRING = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007"
            + "\b\t\n\u000b\f\r\u000e\u000f" + "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017"
            + "\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f" + "\u0020\u0021\"\u0023\u0024\u0025\u0026\u0027"
            + "\u0028\u0029\u002a\u002b\u002c\u002d\u002e\u002f" + "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037"
            + "\u0038\u0039\u003a\u003b\u003c\u003d\u003e\u003f" + "\u0040\u0041\u0042\u0043\u0044\u0045\u0046\u0047"
            + "\u0048\u0049\u004a\u004b\u004c\u004d\u004e\u004f" + "\u0050\u0051\u0052\u0053\u0054\u0055\u0056\u0057"
            + "\u0058\u0059\u005a\u005b\\\u005d\u005e\u005f" + "\u0060\u0061\u0062\u0063\u0064\u0065\u0066\u0067"
            + "\u0068\u0069\u006a\u006b\u006c\u006d\u006e\u006f" + "\u0070\u0071\u0072\u0073\u0074\u0075\u0076\u0077"
            + "\u0078\u0079\u007a\u007b\u007c\u007d\u007e\u007f";

    /**
     * 十六进制数组
     */
    private static final char[] HEX_CHARS =
            new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * cached 0-127 for chars
     */
    private static final String[] CHAR_STRING_ARRAY = new String[128];

    /**
     * cached 0-127 for chars
     */
    private static final Character[] CHAR_ARRAY = new Character[128];

    /**
     * 缓存0-127的字符
     */
    static {
        for (int i = 127; i >= 0; i--) {
            CHAR_STRING_ARRAY[i] = CHAR_STRING.substring(i, i + 1);
            CHAR_ARRAY[i] = new Character((char) i);
        }
    }

    // ========================== basic conversions =========================

    /**
     * 将字符转成<code>ASCII</code>码
     * 
     * @param ch 字符
     * @return <code>ASCII</code>码
     */
    public static int toAscii(char ch) {
        if (ch <= 0xFF) {
            return ch;
        }
        return 0x3F;
    }

    /**
     * 转换成Character对象
     * 
     * @param ch 字符
     * @return Character包装类对象
     */
    public static Character toCharacterObject(char ch) {
        if (ch < CHAR_ARRAY.length) {
            return CHAR_ARRAY[ch];
        }
        return new Character(ch);
    }

    /**
     * 转换成Character对象
     * 
     * @param str 字符串
     * @return Character包装类对象
     */
    public static Character toCharacterObject(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        return toCharacterObject(str.charAt(0));
    }

    /**
     * byte转成char
     * 
     * @param b 字节
     * @return 字符
     */
    public static char toChar(byte b) {
        return (char) (b & 0xFF);
    }

    /**
     * 包装类转char
     * 
     * @param ch 包装类
     * @return char基本类型
     */
    public static char toChar(Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The Character must not be null");
        }
        return ch.charValue();
    }

    /**
     * 包装类转char，如果<code>ch</code>为<code>null</code>，则返回<code>defaultValue</code>
     * 
     * @param ch 包装类
     * @param defaultValue 默认值
     * @return char基本类型
     */
    public static char toChar(Character ch, char defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return ch.charValue();
    }

    /**
     * 字符串转char类型
     * 
     * @param str 字符串
     * @return char类型
     */
    public static char toChar(String str) {
        if (StringUtil.isEmpty(str)) {
            throw new IllegalArgumentException("The String must not be empty");
        }
        return str.charAt(0);
    }

    /**
     * 字符串转char类型，如果<code>str</code>为<code>null</code>，则返回<code>defaultValue</code>
     * 
     * @param str 字符串
     * @param defaultValue 默认值
     * @return char基本类型
     */
    public static char toChar(String str, char defaultValue) {
        if (StringUtil.isEmpty(str)) {
            return defaultValue;
        }
        return str.charAt(0);
    }

    /**
     * char转int
     * 
     * @param ch 字符
     * @return int基本类型
     */
    public static int toIntValue(char ch) {
        if (!isDigit(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
        }
        return ch - 48;
    }

    /**
     * char转int，如果<code>ch</code>不为数字，则返回<code>defaultValue</code>
     * 
     * @param ch 字符
     * @param defaultValue 默认值
     * @return int基本类型
     */
    public static int toIntValue(char ch, int defaultValue) {
        if (!isDigit(ch)) {
            return defaultValue;
        }
        return ch - 48;
    }

    /**
     * 字符包装类转int
     * 
     * @param ch 字符包装类
     * @return int基本类型
     */
    public static int toIntValue(Character ch) {
        if (ch == null) {
            throw new IllegalArgumentException("The character must not be null");
        }
        return toIntValue(ch.charValue());
    }

    /**
     * 字符包装类转int
     * 
     * @param ch 字符包装类
     * @param defaultValue 默认值
     * @return int基本类型
     */
    public static int toIntValue(Character ch, int defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return toIntValue(ch.charValue(), defaultValue);
    }

    /**
     * char转字符串
     * 
     * @param ch 字符
     * @return 字符串
     */
    public static String toString(char ch) {
        if (ch < 128) {
            return CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[] { ch });
    }

    /**
     * 字符包装类转字符串
     * 
     * @param ch 字符包装类
     * @return 字符串
     */
    public static String toString(Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }

    /**
     * 字符转unicode编码的字符串
     * 
     * @param ch 字符
     * @return unicode编码的字符串
     */
    public static String toUnicode(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        }
        if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        }
        if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    /**
     * 字符包装类转unicode编码的字符串
     * 
     * @param ch 字符包装类
     * @return unicode编码的字符串
     */
    public static String toUnicode(Character ch) {
        if (ch == null) {
            return null;
        }
        return toUnicode(ch.charValue());
    }

    /**
     * 转换成“大写的Ascii码”
     * 
     * @param ch 给定字符
     * @return “大写的Ascii码”
     */
    public static char toUpperAscii(char ch) {
        if (isLowercaseLetter(ch)) {
            ch -= (char) 0x20;
        }
        return ch;
    }

    /**
     * 转换成“小写的Ascii码”
     * 
     * @param ch 给定字符
     * @return “大写的Ascii码”
     */
    public static char toLowerAscii(char ch) {
        if (isUppercaseLetter(ch)) {
            ch += (char) 0x20;
        }
        return ch;
    }

    /**
     * 将字符<code>c</code>转换成int型
     * 
     * @param c 待转换字符
     * @return 转换后的值
     */
    public static int hexToInt(char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return c - '0';
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return c - 55;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                return c - 87;
            default:
                throw new IllegalArgumentException("Not a hex: " + c);
        }
    }

    public static char intToHex(int i) {
        return HEX_CHARS[i];
    }

    // ========================== byte char array conversions =========================

    /**
     * 简单的将char数组强转成byte数组
     * 
     * @param charArray char数组
     * @return byte数组
     */
    public static byte[] toSimpleByteArray(char[] charArray) {
        if (charArray == null) {
            return null;
        }
        byte[] barr = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            barr[i] = (byte) charArray[i];
        }
        return barr;
    }

    /**
     * 简单的将CharSequence强转成byte数组
     * 
     * @param charSequence 字符序列 @see CharSequence
     * @return byte数组
     */
    public static byte[] toSimpleByteArray(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        byte[] barr = new byte[charSequence.length()];
        for (int i = 0; i < barr.length; i++) {
            barr[i] = (byte) charSequence.charAt(i);
        }
        return barr;
    }

    /**
     * 简单的将byte数组转成char数组
     * 
     * @param byteArray byte数组
     * @return char数组
     */
    public static char[] toSimpleCharArray(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] carr = new char[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            carr[i] = (char) (byteArray[i] & 0xFF);
        }
        return carr;
    }

    /**
     * 将char数组转换成Ascii码的byte数组
     * 
     * @param charArray 字符串数组
     * @return byte数组
     */
    public static byte[] toAsciiByteArray(char[] charArray) {
        if (charArray == null) {
            return null;
        }
        byte[] barr = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            barr[i] = (byte) ((int) (charArray[i] <= 0xFF ? charArray[i] : 0x3F));
        }
        return barr;
    }

    /**
     * 将CharSequence转换成Ascii码的byte数组
     * 
     * @param charSequence 字符序列 @see CharSequence
     * @return byte数组
     */
    public static byte[] toAsciiByteArray(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        byte[] barr = new byte[charSequence.length()];
        for (int i = 0; i < barr.length; i++) {
            char c = charSequence.charAt(i);
            barr[i] = (byte) ((int) (c <= 0xFF ? c : 0x3F));
        }
        return barr;
    }

    /**
     * 将char数组转换成byte数组
     * 
     * @param charArray char数组
     * @return byte数组
     */
    public static byte[] toRawByteArray(char[] charArray) {
        if (charArray == null) {
            return null;
        }
        byte[] barr = new byte[charArray.length << 1];
        for (int i = 0, bpos = 0; i < charArray.length; i++) {
            char c = charArray[i];
            barr[bpos++] = (byte) ((c & 0xFF00) >> 8);
            barr[bpos++] = (byte) (c & 0x00FF);
        }
        return barr;
    }

    /**
     * 将byte数组转换成char数组
     * 
     * @param byteArray byte数组
     * @return char数组
     */
    public static char[] toRawCharArray(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        int carrLen = byteArray.length >> 1;
        if (carrLen << 1 < byteArray.length) {
            carrLen++;
        }
        char[] carr = new char[carrLen];
        int i = 0;
        int j = 0;
        while (i < byteArray.length) {
            char c = (char) (byteArray[i] << 8);
            i++;

            if (i != byteArray.length) {
                c += byteArray[i] & 0xFF;
                i++;
            }
            carr[j++] = c;
        }
        return carr;
    }

    /**
     * 将char数组转成基于UTF-8的byte数组
     * 
     * @param charArray 字符数组
     * @return byte数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] toByteArray(char[] charArray) throws UnsupportedEncodingException {
        if (charArray == null) {
            return null;
        }
        return new String(charArray).getBytes(UTF_8);
    }

    /**
     * 将char数组转成基于<code>charset</code>编码的byte数组
     * 
     * @param charArray 字符数组
     * @param charset 字符编码
     * @return byte数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] toByteArray(char[] charArray, String charset) throws UnsupportedEncodingException {
        if (charArray == null) {
            return null;
        }
        return new String(charArray).getBytes(charset);
    }

    /**
     * 将byte数组转成基于UTF-8的char数组
     * 
     * @param byteArray byte数组
     * @return char数组
     * @throws UnsupportedEncodingException
     */
    public static char[] toCharArray(byte[] byteArray) throws UnsupportedEncodingException {
        if (byteArray == null) {
            return null;
        }
        return new String(byteArray, UTF_8).toCharArray();
    }

    /**
     * 将byte数组转成基于<code>charset</code>编码的char数组
     * 
     * @param byteArray byte数组
     * @param charset 字符编码
     * @return char数组
     * @throws UnsupportedEncodingException
     */
    public static char[] toCharArray(byte[] byteArray, String charset) throws UnsupportedEncodingException {
        if (byteArray == null) {
            return null;
        }
        return new String(byteArray, charset).toCharArray();
    }

    // ========================== for match =========================

    /**
     * <code>ch</code>是否匹配<code>match</code>中任意一位
     * 
     * @param ch 给定字符
     * @param match 给定字符数组
     * @return 是否匹配
     */
    public static boolean equalsOne(char ch, char[] match) {
        if (match == null) {
            return false;
        }
        for (char aMatch : match) {
            if (ch == aMatch) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找第一个匹配的索引位
     * 
     * @param source 给定字符数组
     * @param index 索引位
     * @param match 需匹配字符数组
     * @return 第一个匹配的索引位
     */
    public static int findFirstEqual(char[] source, int index, char[] match) {
        if (source == null || match == null) {
            return -1;
        }

        for (int i = index, length = source.length; i < length; i++) {
            if (equalsOne(source[i], match)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找第一个匹配的索引位
     * 
     * @param source 给定字符数组
     * @param index 索引位
     * @param match 需匹配字符
     * @return 第一个匹配的索引位
     */
    public static int findFirstEqual(char[] source, int index, char match) {
        if (source == null) {
            return -1;
        }
        for (int i = index; i < source.length; i++) {
            if (source[i] == match) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找第一个不同的索引位
     * 
     * @param source 给定字符数组
     * @param index 索引位
     * @param match 需匹配字符数组
     * @return 第一个匹配的索引位
     */
    public static int findFirstDiff(char[] source, int index, char[] match) {
        if (source == null || match == null) {
            return -1;
        }
        for (int i = index; i < source.length; i++) {
            if (!equalsOne(source[i], match)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找第一个不同的索引位
     * 
     * @param source 给定字符数组
     * @param index 索引位
     * @param match 需匹配字符
     * @return 第一个不同的索引位
     */
    public static int findFirstDiff(char[] source, int index, char match) {
        if (source == null) {
            return -1;
        }
        for (int i = index; i < source.length; i++) {
            if (source[i] != match) {
                return i;
            }
        }
        return -1;
    }

    // ========================== special char =========================

    /**
     * 是否为“空白字符”
     * 
     * @param ch 字符
     * @return 如果是“空白字符”则返回<code>true</code>
     */
    public static boolean isWhitespace(char ch) {
        return ch <= ' ';
    }

    /**
     * 是否为小写字母
     * 
     * @param ch 字符
     * @return 如果是小写字母则返回<code>true</code>
     */
    public static boolean isLowercaseLetter(char ch) {
        return (ch >= 'a') && (ch <= 'z');
    }

    /**
     * 是否为大写字母
     * 
     * @param ch 字符
     * @return 如果是大写字母则返回<code>true</code>
     */
    public static boolean isUppercaseLetter(char ch) {
        return (ch >= 'A') && (ch <= 'Z');
    }

    /**
     * 是否为字母
     * 
     * @param ch 字符
     * @return 如果是字母则返回<code>true</code>
     */
    public static boolean isLetter(char ch) {
        return ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'));
    }

    /**
     * 是否为数字
     * 
     * @param ch 字符
     * @return 如果是数字则返回<code>true</code>
     */
    public static boolean isDigit(char ch) {
        return (ch >= '0') && (ch <= '9');
    }

    /**
     * 是否为字母或数字
     * 
     * @param ch 字符
     * @return 如果是字母或数字则返回<code>true</code>
     */
    public static boolean isLetterOrDigit(char ch) {
        return isDigit(ch) || isLetter(ch);
    }

    /**
     * 是否为字母或数字或"_"
     * 
     * @param ch 字符
     * @return 如果是字母或数字或"_"则返回<code>true</code>
     */
    public static boolean isWordChar(char ch) {
        return isDigit(ch) || isLetter(ch) || (ch == '_');
    }

    /**
     * 是否为字母或数字或("_" "." "[" "]")
     * 
     * @param ch 字符
     * @return 如果是字母或数字或("_" "." "[" "]")则返回<code>true</code>
     */
    public static boolean isPropertyNameChar(char ch) {
        return isDigit(ch) || isLetter(ch) || (ch == '_') || (ch == '.') || (ch == '[') || (ch == ']');
    }

    /**
     * 是否为ASCII码
     * 
     * @param ch 字符
     * @return 如果是ASCII码则返回<code>true</code>
     */
    public static boolean isAscii(char ch) {
        return ch < 128;
    }

    /**
     * 是否为“可见”的ASCII码
     * 
     * @param ch 字符
     * @return 如果是“可见”的ASCII码则返回<code>true</code>
     */
    public static boolean isAsciiPrintable(char ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * 是否为“不可见”的控制ASCII码键
     * 
     * @param ch 字符
     * @return 如果是“不可见”的控制ASCII码键则返回<code>true</code>
     */
    public static boolean isAsciiControl(char ch) {
        return ch < 32 || ch == 127;
    }

    /**
     * 是否为中文字符
     * 
     * @param ch 字符
     * @return 如果是中文字符则返回<code>true</code>
     */
    public static boolean isChinese(char ch) {
        return (ch >= 0x4e00 && ch <= 0x9fa5);
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
            final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }

        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        while (tmpLen-- > 0) {
            char c1 = cs.charAt(index1++);
            char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The same check as in String.regionMatches():
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                    && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;

    }

    /**
     * 是否为ALPHA字符，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是ALPHA字符则返回<code>true</code>
     */
    public static boolean isAlpha(char ch) {
        return ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'));
    }

    /**
     * 是否为十六进制中的字符
     * 
     * @param ch 字符
     * @return 如果是十六进制中的字符则返回<code>true</code>
     */
    public static boolean isHexDigit(char ch) {
        return (ch >= '0' && ch <= '9') || ((ch >= 'a') && (ch <= 'f')) || ((ch >= 'A') && (ch <= 'F'));
    }

    /**
     * 是否为“通用分隔符”，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是“通用分隔符”的字符则返回<code>true</code>
     */
    public static boolean isGenericDelimiter(int ch) {
        switch (ch) {
            case ':':
            case '/':
            case '?':
            case '#':
            case '[':
            case ']':
            case '@':
                return true;
            default:
                return false;
        }
    }

    /**
     * 是否为“子分隔符”，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是“子分隔符”的字符则返回<code>true</code>
     */
    public static boolean isSubDelimiter(int ch) {
        switch (ch) {
            case '!':
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case ';':
            case '=':
                return true;
            default:
                return false;
        }
    }

    /**
     * 是否为“保留字符”，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是“保留字符”的字符则返回<code>true</code>
     */
    public static boolean isReserved(char ch) {
        return isGenericDelimiter(ch) || isSubDelimiter(ch);
    }

    /**
     * 是否为“非保留字符”，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是“非保留字符”的字符则返回<code>true</code>
     */
    public static boolean isUnreserved(char ch) {
        return isAlpha(ch) || isDigit(ch) || ch == '-' || ch == '.' || ch == '_' || ch == '~';
    }

    /**
     * 是否为“pchar”，@see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
     *
     * @param ch 字符
     * @return 如果是“pchar”的字符则返回<code>true</code>
     */
    public static boolean isPchar(char ch) {
        return isUnreserved(ch) || isSubDelimiter(ch) || ch == ':' || ch == '@';
    }

}
