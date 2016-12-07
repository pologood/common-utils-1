/**
 * 
 */
package com.baidu.unbiz.common;

import java.util.Random;
import java.util.concurrent.Callable;

import com.baidu.unbiz.common.able.Computable;
import com.baidu.unbiz.common.cache.ConcurrentCache;
import com.baidu.unbiz.common.codec.Base64;

/**
 * 有关随机数的工具类，依赖于<code>Random</code>实现
 * <p>
 * 因为<code>Random</code>内部实现为伪随机算法；注意：<br>
 * 如果用相同的种子创建两个 Random 实例，则对每个实例进行相同的方法调用序列，它们将生成并返回相同的数字序列；<br>
 * 且内部使<code>synchronized</code>，JDK1.5以后使用<code>java.util.concurrent.atomic<code>包中的自旋锁；<br>
 * 当并发需要时，如使用相同的<code>Random</code>对象该类提供的方法会有性能问题。可使用 {@link #next(Class, int, int)}或 {@link #next(Random, int, int)}
 * 来提升性能
 * 
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月15日 下午3:10:04
 */
public abstract class RandomUtil {

    private static final Computable<Long, Random> SEED_MAP = ConcurrentCache.createComputable();

    private static final Computable<Class<?>, Random> CLASS_BINDS = ConcurrentCache.createComputable();

    private static final Random RANDOM = new Random();

    private static Random getRandom(final long seed) {
        return SEED_MAP.get(seed, new Callable<Random>() {
            @Override
            public Random call() throws Exception {
                return new Random(seed);
            }
        });
    }

    private static Random getRandom(final Class<?> clazz) {
        return CLASS_BINDS.get(clazz, new Callable<Random>() {
            @Override
            public Random call() throws Exception {
                return new Random();
            }
        });
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param seed 随机数种子
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static int next(final long seed, int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        Random random = getRandom(seed);

        int minus = random.nextInt(end - begin + 1);
        return (begin + minus);
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param seed 随机数种子
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static long next(final long seed, long begin, long end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        Random random = getRandom(seed);

        long minus = random.nextInt((int) (end - begin + 1));
        return (begin + minus);
    }

    public static int next(Random random, int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        int minus = random.nextInt(end - begin + 1);
        return (begin + minus);
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static int next(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        int minus = RANDOM.nextInt(end - begin + 1);
        return (begin + minus);
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static long next(long begin, long end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        long minus = RANDOM.nextInt((int) (end - begin + 1));
        return (begin + minus);
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param clazz 绑定的Class，一个类绑定一个Random，提高性能
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static int next(final Class<?> clazz, int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        Random random = getRandom(clazz);

        int minus = random.nextInt(end - begin + 1);
        return (begin + minus);
    }

    /**
     * 获取begin和end之间的整数 [begin,end]
     * 
     * @param clazz 绑定的Class，一个类绑定一个Random，提高性能
     * @param begin 起始值
     * @param end 终止值
     * @return begin和end之间的整数
     */
    public static long next(final Class<?> clazz, long begin, long end) {
        if (end < begin) {
            throw new IllegalArgumentException("end must not smaller than begin");
        }

        Random random = getRandom(clazz);

        long minus = random.nextInt((int) (end - begin + 1));
        return (begin + minus);
    }

    /**
     * 获取pSngBegin和pSngEnd之间的数值 [pSngBegin,pSngEnd)
     * 
     * @param pSngBegin 起始值
     * @param pSngEnd 终止值
     * @return pSngBegin和pSngEnd之间的数值
     */
    public static double getRandomNum(double pSngBegin, double pSngEnd) {
        if (pSngEnd < pSngBegin) {
            throw new IllegalArgumentException("pSngEnd must not smaller than pSngBegin");
        }

        return (pSngEnd - pSngBegin) * Math.random() + pSngBegin;
    }

    /**
     * 获取pSngBegin和pSngEnd之间的数值 [pSngBegin,pSngEnd)
     * 
     * @param seed 随机数种子
     * @param pSngBegin 起始值
     * @param pSngEnd 终止值
     * @return pSngBegin和pSngEnd之间的数值
     */
    public static double getRandomNum(final long seed, double pSngBegin, double pSngEnd) {
        if (pSngEnd < pSngBegin) {
            throw new IllegalArgumentException("pSngEnd must not smaller than pSngBegin");
        }

        return (pSngEnd - pSngBegin) * Math.random() + pSngBegin;
    }

    /**
     * 按照一定概率进行随机<br>
     * 该方法参数太多，不做合法检测<br>
     * FIXME
     * 
     * @param pSngBegin 随机数范围的开始数字
     * @param pSngEnd 随机数范围结束数字
     * @param pSngPB 要随机的数字的开始数字
     * @param pSngPE 要随机的数字的结束数字
     * @param pBytP 要随机的数字随机概率
     * @return 按照一定概率随机的数字
     */
    public static double getRndNumP(double pSngBegin, double pSngEnd, double pSngPB, double pSngPE, double pBytP) {
        double sngPLen = pSngPE - pSngPB;
        // total length
        double sngTLen = pSngEnd - pSngBegin;
        // FIXME may throw java.lang.ArithmeticException : / by zero
        if ((sngPLen / sngTLen) * 100 == pBytP) {
            return getRandomNum(pSngBegin, pSngEnd);
        }

        // ((sngPLen + sngIncreased) / (sngTLen + sngIncreased)) * 100 =
        // bytP
        double sngIncreased = ((pBytP / 100) * sngTLen - sngPLen) / (1 - (pBytP / 100));
        // 缩放回原来区间
        double sngResult = getRandomNum(pSngBegin, pSngEnd + sngIncreased);
        if (pSngBegin <= sngResult && sngResult <= pSngPB) {
            return sngResult;
        }

        if (pSngPB <= sngResult && sngResult <= (pSngPE + sngIncreased)) {
            return pSngPB + (sngResult - pSngPB) * sngPLen / (sngPLen + sngIncreased);
        }

        if ((pSngPE + sngIncreased) <= sngResult && sngResult <= (pSngEnd + sngIncreased)) {
            return sngResult - sngIncreased;
        }

        return 0d;
    }

    /**
     * 按照一定概率进行随机<br>
     * 该方法参数太多，不做合法检测<br>
     * FIXME
     * 
     * @param pSngBegin 随机数范围的开始数字
     * @param pSngEnd 随机数范围结束数字
     * @param pSngPB 要随机的数字的开始数字
     * @param pSngPE 要随机的数字的结束数字
     * @param pBytP 要随机的数字随机概率
     * @return 按照一定概率随机的数字
     */
    public static double getRndNumP(final long seed, double pSngBegin, double pSngEnd, double pSngPB, double pSngPE,
            double pBytP) {
        double sngPLen = pSngPE - pSngPB;
        // total length
        double sngTLen = pSngEnd - pSngBegin;
        // FIXME may throw java.lang.ArithmeticException : / by zero
        if ((sngPLen / sngTLen) * 100 == pBytP) {
            return getRandomNum(pSngBegin, pSngEnd);
        }

        // ((sngPLen + sngIncreased) / (sngTLen + sngIncreased)) * 100 =
        // bytP
        double sngIncreased = ((pBytP / 100) * sngTLen - sngPLen) / (1 - (pBytP / 100));
        // 缩放回原来区间
        double sngResult = getRandomNum(pSngBegin, pSngEnd + sngIncreased);
        if (pSngBegin <= sngResult && sngResult <= pSngPB) {
            return sngResult;
        }

        if (pSngPB <= sngResult && sngResult <= (pSngPE + sngIncreased)) {
            return pSngPB + (sngResult - pSngPB) * sngPLen / (sngPLen + sngIncreased);
        }

        if ((pSngPE + sngIncreased) <= sngResult && sngResult <= (pSngEnd + sngIncreased)) {
            return sngResult - sngIncreased;
        }

        return 0d;
    }

    // ============================= for string ========================

    protected static final char[] ALPHA_RANGE = new char[] { 'A', 'Z', 'a', 'z' };

    protected static final char[] ALPHA_NUMERIC_RANGE = new char[] { '0', '9', 'A', 'Z', 'a', 'z' };

    // =========================== for string ============================

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>， 字符在指定的字符数组中选择。
     * 
     * @param count 指定的字符数
     * @param chars 指定的字符数组
     * @return 随机字符串
     */
    public static String randomString(int count, char[] chars) {
        if (count == 0 || ArrayUtil.isEmpty(chars)) {
            return Emptys.EMPTY_STRING;
        }
        char[] result = new char[count];
        while (count-- > 0) {
            result[count] = chars[RANDOM.nextInt(chars.length)];
        }
        return new String(result);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>， 字符在指定的字符数组中选择。
     * 
     * @param count 指定的字符数
     * @param chars 指定的字符数组
     * @return 随机字符串
     */
    public static String randomString(int count, String chars) {
        // FIXME
        if (chars == null) {
            return Emptys.EMPTY_STRING;
        }

        return randomString(count, chars.toCharArray());
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。 字符是从所提供的范围内选择。
     * 
     * @param count 指定的字符数
     * @param start 开始字符
     * @param end 结束字符
     * @return 随机字符串
     */
    public static String randomString(int count, char start, char end) {
        if (count == 0) {
            return Emptys.EMPTY_STRING;
        }
        char[] result = new char[count];
        int len = end - start + 1;
        while (count-- > 0) {
            result[count] = (char) (RANDOM.nextInt(len) + start);
        }
        return new String(result);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。
     * <p>
     * 字符从ASCII值<code>32</code>和<code>126</code>（含）之间选择。
     *
     * @param count 指定的字符数
     * @return 随机字符串
     */
    public static String randomAscii(int count) {
        return randomString(count, (char) 32, (char) 126);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。字符为数字类型
     * 
     * @param count 指定的字符数
     * @return 随机字符串
     */
    public static String randomNumeric(int count) {
        return randomString(count, '0', '9');
    }

    private static String randomRanges(int count, char...ranges) {
        if (count == 0 || ArrayUtil.isEmpty(ranges)) {
            return Emptys.EMPTY_STRING;
        }

        int i = 0;
        int len = 0;
        int[] lens = new int[ranges.length];
        while (i < ranges.length) {
            int gap = ranges[i + 1] - ranges[i] + 1;
            len += gap;
            lens[i] = len;
            i += 2;
        }

        char[] result = new char[count];
        while (count-- > 0) {
            char c = 0;
            int r = RANDOM.nextInt(len);
            for (i = 0; i < ranges.length; i += 2) {
                if (r < lens[i]) {
                    r += ranges[i];
                    if (i != 0) {
                        r -= lens[i - 2];
                    }
                    c = (char) r;
                    break;
                }
            }
            result[count] = c;
        }
        return new String(result);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。字符为ALPHA
     * 
     * @param count 指定的字符数
     * @return 随机字符串
     */
    public static String randomAlpha(int count) {
        return randomRanges(count, ALPHA_RANGE);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。字符为ALPHA和数字类型
     * 
     * @param count 指定的字符数
     * @return 随机字符串
     */
    public static String randomAlphaNumeric(int count) {
        return randomRanges(count, ALPHA_NUMERIC_RANGE);
    }

    /**
     * 创建随机字符串，其长度为指定的字符数<code>count</code>。字符为Base64支持类型
     * 
     * @param count 指定的字符数
     * @return 随机字符串
     */
    public static String randomBase64(int count) {
        return randomString(count, Base64.CHARS);
    }
}
