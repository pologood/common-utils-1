/**
 * 
 */
package com.baidu.unbiz.common.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.baidu.unbiz.common.i18n.LocaleInfo.UnknownCharset;

import static com.baidu.unbiz.common.test.TestUtil.exception;

/**
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月24日 上午3:57:04
 */
public class UnknownCharsetTests {
    private UnknownCharset charset;

    @Test
    public void constructor() {
        try {
            new UnknownCharset(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e, exception("charset name"));
        }
    }

    @Test
    public void name() {
        charset = new UnknownCharset("test");
        assertEquals("test", charset.name());
    }

    @Test
    public void toString_() {
        charset = new UnknownCharset("test");
        assertEquals("test", charset.name());
    }

    @Test
    public void newEncoder() {
        charset = new UnknownCharset("test");

        try {
            charset.newEncoder();
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e, exception("Could not create encoder for unknown charset: test"));
        }
    }

    @Test
    public void newDecoder() {
        charset = new UnknownCharset("test");

        try {
            charset.newDecoder();
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e, exception("Could not create decoder for unknown charset: test"));
        }
    }
}
