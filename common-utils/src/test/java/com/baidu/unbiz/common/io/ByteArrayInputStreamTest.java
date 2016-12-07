/**
 * 
 */
package com.baidu.unbiz.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * ByteArrayInputStreamTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月27日 上午1:50:14
 */
public class ByteArrayInputStreamTest {
    private byte[] data = "0123456789".getBytes();

    @Test
    public void read() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        bais.mark(100);

        byte[] readData = new byte[data.length];

        assertEquals(data.length, bais.read(readData));
        assertArrayEquals(data, readData);

        bais.reset();

        byte[] readData2 = new byte[data.length];

        assertEquals(data.length, bais.read(readData2));
        assertArrayEquals(data, readData2);

        bais.close();
    }
}
