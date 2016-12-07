/**
 * 
 */
package com.baidu.unbiz.common.codec;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.baidu.unbiz.common.StringPool;

/**
 * URL Decoder
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月18日 上午3:45:29
 */
public class URLDecoder implements StringPool.Charset {

    public static String decode(String url) {
        return decode(url, UTF_8, false);
    }

    public static String decode(String source, String encoding) {
        return decode(source, encoding, false);
    }

    private static String decode(String source, String encoding, boolean decodePlus) {
        int length = source.length();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        boolean changed = false;

        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            // FIXME
            if (ch == '%') {
                if ((i + 2) >= length) {
                    throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
                }

                char hex1 = source.charAt(i + 1);
                char hex2 = source.charAt(i + 2);
                int u = Character.digit(hex1, 16);
                int l = Character.digit(hex2, 16);
                if (u == -1 || l == -1) {
                    throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
                }
                bos.write((char) ((u << 4) + l));
                i += 2;
                changed = true;
                break;
            }

            if (ch == '+' && decodePlus) {
                ch = ' ';
                changed = true;
            }

            bos.write(ch);
        }
        try {
            return changed ? new String(bos.toByteArray(), encoding) : source;
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    public static String decodeQuery(String source) {
        return decode(source, UTF_8, true);
    }

    public static String decodeQuery(String source, String encoding) {
        return decode(source, encoding, true);
    }

}