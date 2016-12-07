/**
 * 
 */
package com.baidu.unbiz.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.baidu.unbiz.common.StringUtil;
import com.baidu.unbiz.common.io.OutputEngine.OutputStreamFactory;

/**
 * OutputEngineTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015年11月27日 上午1:48:25
 */
public class OutputEngineTest {
    private String charData;
    private byte[] data;

    @Before
    public void init() throws Exception {
        charData = StringUtil.repeat("中华民国", 1024 / 16 * 100);
        data = charData.getBytes("UTF-8");
    }

    @Test
    public void compressInputStream() throws Exception {

        // GZIPInputStream是对压缩流进行解压缩：read() 原始数据 <- decompress <- compressed data stream
        // GZIPOutputStream是对输出流进行压缩：write() 原始数据 -> compress -> compressed data stream
        // 但是JDK中不存在这样一个流：read() compressed data <- compress <- 原始数据流
        // 利用OutputEngine就可以实现这样的流。

        // 原始数据输入流
        InputStream rawDataStream = new ByteArrayInputStream(data);

        // OutputEngine：读取输入流，输出到GZIPOutputStream，实现压缩。
        OutputEngine isoe = new InputStreamOutputEngine(rawDataStream, new OutputStreamFactory() {
            public OutputStream getOutputStream(OutputStream out) throws IOException {
                return new GZIPOutputStream(out);
            }
        });

        // 从OutputEngine中直接取得压缩输入流
        OutputEngineInputStream compressedDataStream = new OutputEngineInputStream(isoe);

        byte[] compressedData = StreamUtil.readBytes(compressedDataStream, true).toByteArray();

        assertTrue(compressedData.length < data.length);

        // 从压缩流中恢复
        InputStream zis = new GZIPInputStream(new ByteArrayInputStream(compressedData));

        byte[] decompressedData = StreamUtil.readBytes(zis, true).toByteArray();

        assertArrayEquals(data, decompressedData);
    }

    @Test
    public void compressInputStreamFromReader() throws Exception {

        // 创建这样的输入流：read() compressed data <- compress <- 原始char数据流

        // 原始数据输入流
        Reader rawDataStream = new StringReader(charData);

        // OutputEngine：读取输入流，输出到GZIPOutputStream，实现压缩。
        OutputEngine isoe = new ReaderOutputEngine(rawDataStream, new OutputStreamFactory() {
            public OutputStream getOutputStream(OutputStream out) throws IOException {
                return new GZIPOutputStream(out);
            }
        }, "UTF-8");

        // 从OutputEngine中直接取得压缩输入流
        OutputEngineInputStream compressedDataStream = new OutputEngineInputStream(isoe);

        byte[] compressedData = StreamUtil.readBytes(compressedDataStream, true).toByteArray();

        assertTrue(compressedData.length < charData.length());

        // 从压缩流中恢复
        Reader zis = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(compressedData)), "UTF-8");

        String decompressedData = StreamUtil.readText(zis, true);

        assertEquals(charData, decompressedData);
    }
}
