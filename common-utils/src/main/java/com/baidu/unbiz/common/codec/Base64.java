/**
 * 
 */
package com.baidu.unbiz.common.codec;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.baidu.unbiz.common.StringPool;

/**
 * to see RFC 2045
 * <p>
 * 编码支持带或不带“-”线分隔符。当带“-”，结果将有最大76个字符
 * <p>
 * 解码时，输入必须是有效的，没有非法字符。如果输入包含“-”，他们必须是76个字符长
 * <p>
 * FIXME CRLF ?? EOF
 */
public abstract class Base64 implements StringPool.Charset {

    public static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final int[] INV = new int[256];

    static {
        Arrays.fill(INV, -1);
        for (int i = 0, iS = CHARS.length; i < iS; i++) {
            INV[CHARS[i]] = i;
        }
        INV['='] = 0;
    }

    public byte[] decode(char[] chars) {
        int length = chars.length;
        if (length == 0) {
            return new byte[0];
        }

        int sndx = 0;
        int endx = length - 1;
        int pad = chars[endx] == '=' ? (chars[endx - 1] == '=' ? 2 : 1) : 0;
        int cnt = endx - sndx + 1;
        int sepCnt = length > 76 ? (chars[76] == '\r' ? cnt / 78 : 0) << 1 : 0;
        int len = ((cnt - sepCnt) * 6 >> 3) - pad;
        byte[] dest = new byte[len];

        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            int i = INV[chars[sndx++]] << 18 | INV[chars[sndx++]] << 12 | INV[chars[sndx++]] << 6 | INV[chars[sndx++]];

            dest[d++] = (byte) (i >> 16);
            dest[d++] = (byte) (i >> 8);
            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {
                sndx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            int i = 0;
            for (int j = 0; sndx <= endx - pad; j++) {
                i |= INV[chars[sndx++]] << (18 - j * 6);
            }
            for (int r = 16; d < len; r -= 8) {
                dest[d++] = (byte) (i >> r);
            }
        }

        return dest;
    }

    public static byte[] decode(byte[] bytes) {
        int length = bytes.length;
        if (length == 0) {
            return new byte[0];
        }

        int sndx = 0;
        int endx = length - 1;
        int pad = bytes[endx] == '=' ? (bytes[endx - 1] == '=' ? 2 : 1) : 0;
        int cnt = endx - sndx + 1;
        int sepCnt = length > 76 ? (bytes[76] == '\r' ? cnt / 78 : 0) << 1 : 0;
        int len = ((cnt - sepCnt) * 6 >> 3) - pad;
        byte[] dest = new byte[len];

        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            int i = INV[bytes[sndx++]] << 18 | INV[bytes[sndx++]] << 12 | INV[bytes[sndx++]] << 6 | INV[bytes[sndx++]];

            dest[d++] = (byte) (i >> 16);
            dest[d++] = (byte) (i >> 8);
            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {
                sndx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            int i = 0;
            for (int j = 0; sndx <= endx - pad; j++) {
                i |= INV[bytes[sndx++]] << (18 - j * 6);
            }
            for (int r = 16; d < len; r -= 8) {
                dest[d++] = (byte) (i >> r);
            }
        }

        return dest;
    }

    public static byte[] decode(String string) {
        int length = string.length();
        if (length == 0) {
            return new byte[0];
        }

        int sndx = 0;
        int endx = length - 1;
        int pad = string.charAt(endx) == '=' ? (string.charAt(endx - 1) == '=' ? 2 : 1) : 0;
        int cnt = endx - sndx + 1;
        int sepCnt = length > 76 ? (string.charAt(76) == '\r' ? cnt / 78 : 0) << 1 : 0;
        int len = ((cnt - sepCnt) * 6 >> 3) - pad;
        byte[] dest = new byte[len];

        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            int i = INV[string.charAt(sndx++)] << 18 | INV[string.charAt(sndx++)] << 12 
                    | INV[string.charAt(sndx++)] << 6 | INV[string.charAt(sndx++)];

            dest[d++] = (byte) (i >> 16);
            dest[d++] = (byte) (i >> 8);
            dest[d++] = (byte) i;

            if (sepCnt > 0 && ++cc == 19) {
                sndx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            int i = 0;
            for (int j = 0; sndx <= endx - pad; j++) {
                i |= INV[string.charAt(sndx++)] << (18 - j * 6);
            }
            for (int r = 16; d < len; r -= 8) {
                dest[d++] = (byte) (i >> r);
            }
        }

        return dest;
    }

    public static String decodeToString(byte[] bytes) {
        try {
            return new String(decode(bytes), UTF_8);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static String decodeToString(String string) {
        try {
            return new String(decode(string), UTF_8);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static byte[] encodeToByte(String string) {
        try {
            return encodeToByte(string.getBytes(UTF_8), false);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static byte[] encodeToByte(String string, boolean lineSep) {
        try {
            return encodeToByte(string.getBytes(UTF_8), lineSep);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static byte[] encodeToByte(byte[] bytes) {
        return encodeToByte(bytes, false);
    }

    public static byte[] encodeToByte(byte[] bytes, boolean lineSep) {
        int len = bytes != null ? bytes.length : 0;
        if (len == 0) {
            return new byte[0];
        }

        int evenlen = (len / 3) * 3;
        int cnt = ((len - 1) / 3 + 1) << 2;
        int destlen = cnt + (lineSep ? (cnt - 1) / 76 << 1 : 0);
        byte[] dest = new byte[destlen];

        for (int s = 0, d = 0, cc = 0; s < evenlen;) {
            int i = (bytes[s++] & 0xff) << 16 | (bytes[s++] & 0xff) << 8 | (bytes[s++] & 0xff);

            dest[d++] = (byte) CHARS[(i >>> 18) & 0x3f];
            dest[d++] = (byte) CHARS[(i >>> 12) & 0x3f];
            dest[d++] = (byte) CHARS[(i >>> 6) & 0x3f];
            dest[d++] = (byte) CHARS[i & 0x3f];

            if (lineSep && ++cc == 19 && d < destlen - 2) {
                dest[d++] = '\r';
                dest[d++] = '\n';
                cc = 0;
            }
        }

        int left = len - evenlen;
        if (left > 0) {
            int i = ((bytes[evenlen] & 0xff) << 10) | (left == 2 ? ((bytes[len - 1] & 0xff) << 2) : 0);

            dest[destlen - 4] = (byte) CHARS[i >> 12];
            dest[destlen - 3] = (byte) CHARS[(i >>> 6) & 0x3f];
            dest[destlen - 2] = left == 2 ? (byte) CHARS[i & 0x3f] : (byte) '=';
            dest[destlen - 1] = '=';
        }
        return dest;
    }

    public static String encodeToString(String string) {
        try {
            return new String(encodeToChar(string.getBytes(UTF_8), false));
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static String encodeToString(String string, boolean lineSep) {
        try {
            return new String(encodeToChar(string.getBytes(UTF_8), lineSep));
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static String encodeToString(byte[] bytes) {
        return new String(encodeToChar(bytes, false));
    }

    public static String encodeToString(byte[] bytes, boolean lineSep) {
        return new String(encodeToChar(bytes, lineSep));
    }

    public static char[] encodeToChar(byte[] bytes, boolean lineSeparator) {
        int len = bytes != null ? bytes.length : 0;
        if (len == 0) {
            return new char[0];
        }

        int evenlen = (len / 3) * 3;
        int cnt = ((len - 1) / 3 + 1) << 2;
        int destLen = cnt + (lineSeparator ? (cnt - 1) / 76 << 1 : 0);
        char[] dest = new char[destLen];

        for (int s = 0, d = 0, cc = 0; s < evenlen;) {
            int i = (bytes[s++] & 0xff) << 16 | (bytes[s++] & 0xff) << 8 | (bytes[s++] & 0xff);

            dest[d++] = CHARS[(i >>> 18) & 0x3f];
            dest[d++] = CHARS[(i >>> 12) & 0x3f];
            dest[d++] = CHARS[(i >>> 6) & 0x3f];
            dest[d++] = CHARS[i & 0x3f];

            if (lineSeparator && (++cc == 19) && (d < (destLen - 2))) {
                dest[d++] = '\r';
                dest[d++] = '\n';
                cc = 0;
            }
        }

        int left = len - evenlen; // 0 - 2.
        if (left > 0) {
            int i = ((bytes[evenlen] & 0xff) << 10) | (left == 2 ? ((bytes[len - 1] & 0xff) << 2) : 0);

            dest[destLen - 4] = CHARS[i >> 12];
            dest[destLen - 3] = CHARS[(i >>> 6) & 0x3f];
            dest[destLen - 2] = left == 2 ? CHARS[i & 0x3f] : '=';
            dest[destLen - 1] = '=';
        }
        return dest;
    }

}