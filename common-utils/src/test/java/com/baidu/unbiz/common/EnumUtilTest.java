/**
 *
 */
package com.baidu.unbiz.common;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.common.able.Valuable;
import com.baidu.unbiz.common.file.FileType;
import com.baidu.unbiz.common.logger.CachedLogger;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月1日 下午4:28:51
 */
public class EnumUtilTest extends CachedLogger {

    @Test
    public void find() {
        FileType type = EnumUtil.find(FileType.class, "excel");

        assertEquals(FileType.EXCEL, type);
    }

    @Test
    public void findByBitMask() {
        List<Mask> types = EnumUtil.findByBitMask(Mask.class, 2 + 8, false);
        System.out.println(types);
        assertEquals(2, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 2 + 8, true);
        System.out.println(types);
        assertEquals(2, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 4 + 8, false);
        System.out.println(types);
        assertEquals(1, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 4 + 8, true);
        System.out.println(types);
        assertEquals(0, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32, true);
        System.out.println(types);
        assertEquals(5, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32 + 99, false);
        System.out.println(types);
        assertEquals(0, types.size());

        types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32 + 99, true);
        System.out.println(types);
        assertEquals(0, types.size());
    }

    @Test
    public void isMatchBitMask() {
        assertEquals(true, EnumUtil.isMatchBitMask(Mask.class, 2 + 8, false));
        assertEquals(true, EnumUtil.isMatchBitMask(Mask.class, 2 + 8, true));
        assertEquals(true, EnumUtil.isMatchBitMask(Mask.class, 4 + 8, false));
        assertEquals(false, EnumUtil.isMatchBitMask(Mask.class, 4 + 8, true));
    }

    public static enum Mask implements Valuable<Integer> {

        BIT_MASK_1(1, "one"),
        BIT_MASK_2(2, "two"),
        BIT_MASK_8(8, "eight"),
        BIT_MASK_16(16, "sixteen"),
        BIT_MASK_32(32, "thirth-two");

        private Mask(int mask, String desc) {
            this.mask = mask;
            this.desc = desc;
        }

        private int mask;

        private String desc;

        public int getMask() {
            return mask;
        }

        public void setMask(int mask) {
            this.mask = mask;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public Integer value() {
            return mask;
        }

    }

}
