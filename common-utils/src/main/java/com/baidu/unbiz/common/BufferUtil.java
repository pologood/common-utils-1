/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common;

import java.nio.ByteBuffer;

/**
 * 字节缓冲区相关的工具类
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-9-2 下午10:40:39
 */
public abstract class BufferUtil {

    public static byte[] readBytes(ByteBuffer buffer, int length) {
        if (buffer == null || length <= 0) {
            return null;
        }

        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return bytes;
    }

    public static byte[] toBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value).flip();

        return readBytes(buffer, 4);
    }

}
