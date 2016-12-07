/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common;

/**
 * Byte Util
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-7-13 下午5:42:59
 */
public abstract class ByteUtil {

    public static String hexDump(byte[] bytes, int maxShowBytes) {
        if (bytes == null || maxShowBytes <= 0) {
            return null;
        }

        int idx = 0;
        StringBuilder body = new StringBuilder();
        body.append("bytes size is:[");
        body.append(bytes.length);
        body.append("]\r\n");
        for (byte b : bytes) {
            int hex = ((int) b) & 0xff;
            String shex = Integer.toHexString(hex).toUpperCase();
            if (1 == shex.length()) {
                body.append("0");
            }
            body.append(shex);
            body.append(" ");
            idx++;
            if (16 == idx) {
                body.append("\r\n");
                idx = 0;
            }
            maxShowBytes--;
            if (maxShowBytes <= 0) {
                break;
            }
        }
        if (idx != 0) {
            body.append("\r\n");
        }

        return body.toString();
    }

}
