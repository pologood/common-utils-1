/**
 *
 */
package com.baidu.unbiz.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.baidu.unbiz.common.able.Computable;
import com.baidu.unbiz.common.able.Valuable;
import com.baidu.unbiz.common.cache.ConcurrentCache;
import com.baidu.unbiz.common.convert.ConvertUtil;

/**
 * Enum Util
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月25日 上午12:46:30
 */
public abstract class EnumUtil {

    private static final Computable<String, Enum<?>> COMPUTABLE = ConcurrentCache.createComputable();

    private static final Computable<String, List<Enum<?>>> COMPUTABLE2 = ConcurrentCache.createComputable();

    private static final Computable<Class<Enum<?>>, Integer> ENUM_TOTAL = ConcurrentCache.createComputable();

    public static <E extends Enum<E>> E parseName(Class<E> enumType, String name) {
        if (enumType == null || StringUtil.isBlank(name)) {
            return null;
        }

        return Enum.valueOf(enumType, name);
    }

    public static <E extends Enum<E>> int total(final Class<E> enumType) {
        if (enumType == null) {
            return -1;
        }

        @SuppressWarnings({ "unchecked" })
        Class<Enum<?>> enumClass = (Class<Enum<?>>) enumType;
        return ENUM_TOTAL.get(enumClass, new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                E[] enums = enumType.getEnumConstants();
                // E[] enums = ReflectionUtil.invokeStaticMethod(enumType, "values");
                return enums.length;
            }
        });

    }

    public static <E extends Enum<E>> E find(final Class<E> enumType, final Object value) {
        if (ObjectUtil.isAnyNull(enumType, value)) {
            return null;
        }

        if (!ClassUtil.isInterfaceImpl(enumType, Valuable.class)) {
            return null;
        }

        String key = enumType + ":(" + value.getClass() + ":" + value + ")";
        @SuppressWarnings("unchecked")
        E result = (E) COMPUTABLE.get(key, new Callable<Enum<?>>() {

            @Override
            public Enum<?> call() throws Exception {
                // E[] values = enumType.getEnumConstants();
                Valuable<?>[] values = ReflectionUtil.invokeStaticMethod(enumType, "values");

                for (Valuable<?> e : values) {
                    if (value.equals(e.value())) {
                        @SuppressWarnings("unchecked")
                        E result = (E) e;
                        return result;
                    }
                }

                return null;
            }
        });

        return result;
    }

    /**
     * 简单理解为value & enumType中定义所有值的和是否>0，也就说bit位是否match上。
     * <p/>
     * 强烈建议参考{@link #findByBitMask(Class, Number, boolean)}看如何实现的。
     * 
     * @param enumType bit位定义枚举值
     * @param value 待验证的值
     * @param mustMatchAllBit 是否value的所有二进制表示的位在enumType都存在可以找到
     * 
     * @return 如果value中的bit位和枚举中定义的值符合，则返回true，否则返回false
     * 
     * @see #findByBitMask(Class, Number, boolean)
     */
    public static <E extends Enum<E>, V extends Number> boolean isMatchBitMask(final Class<E> enumType, final V value,
            final boolean mustMatchAllBit) {
        List<E> enumTypes = findByBitMask(enumType, value, mustMatchAllBit);
        if (CollectionUtil.isEmpty(enumTypes)) {
            return false;
        }
        return true;
    }

    /**
     * 尝试从某个bit位定义<code>enumType</code>中找到某个值<code>value</code>符合的位
     * <p/>
     * 例如，bit位二进制为111b，十进制为7；某个值二进制100b，十进制为4，则7 & 4 = 100b > 0，那么返回bit位定义中为4的那个枚举值，如果符合多个位返回List列表
     * <p/>
     * 使用条件是：<br/>
     * <ul>
     * <li>1）枚举定义class不能为空，否则返回null</li>
     * <li>2）枚举定义class必须实现{@link Valuable}接口，否则返回null</li>
     * <li>3）枚举定义class实现的{@link Valuable}接口，其泛型必须为Long, Int, Short等整形表示可按位操作的，如果位string则无法比较位</li>
     * <li>4）某个值value的值不能大于枚举定义中所有值“或”的结果（也就是所有值相加），否则返回空list</li>
     * <li>5）提供严格校验的一个参数<code>mustMatchAllBit</code>，如果bit位枚举中空缺了某些位，value中恰巧有，则也会返回空list，否则不强制要求，把能match的都返回</li>
     * </ul>
     * <p/>
     * 例如定义枚举bit位如下：
     * 
     * <pre>
     * public static enum Mask implements Valuable&lt;Integer&gt; {
     * 
     *     BIT_MASK_1(1, &quot;one&quot;), BIT_MASK_2(2, &quot;two&quot;), BIT_MASK_8(8, &quot;eight&quot;), BIT_MASK_16(16, &quot;sixteen&quot;), BIT_MASK_32(32,
     *             &quot;thirth-two&quot;);
     * 
     *     private Mask(int mask, String desc) {
     *         this.mask = mask;
     *         this.desc = desc;
     *     }
     * 
     *     private int mask;
     * 
     *     private String desc;
     * 
     *     public int getMask() {
     *         return mask;
     *     }
     * 
     *     public void setMask(int mask) {
     *         this.mask = mask;
     *     }
     * 
     *     public String getDesc() {
     *         return desc;
     *     }
     * 
     *     public void setDesc(String desc) {
     *         this.desc = desc;
     *     }
     * 
     *     public Integer value() {
     *         return mask;
     *     }
     * 
     * }
     * 
     * </pre>
     * 
     * 使用方法如下：
     * 
     * <pre>
     * List&lt;Mask&gt; types = EnumUtil.findByBitMask(Mask.class, 2 + 8, false);
     * System.out.println(types); // [BIT_MASK_2, BIT_MASK_8]
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 2 + 8, true);
     * System.out.println(types); // [BIT_MASK_2, BIT_MASK_8]
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 4 + 8, false);
     * System.out.println(types); // [BIT_MASK_8]
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 4 + 8, true);
     * System.out.println(types); // []
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32, true);
     * System.out.println(types); // [BIT_MASK_1, BIT_MASK_2, BIT_MASK_8, BIT_MASK_16, BIT_MASK_32]
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32 + 99, false);
     * System.out.println(types); // []
     * 
     * types = EnumUtil.findByBitMask(Mask.class, 1 + 2 + 8 + 16 + 32 + 99, true);
     * System.out.println(types); // []
     * </pre>
     * 
     * @param enumType bit位定义枚举值
     * @param value 待验证的值
     * @param mustMatchAllBit 是否value的所有二进制表示的位在enumType都存在可以找到
     * 
     * @return 找到的枚举值的列表
     */
    public static <E extends Enum<E>, V extends Number> List<E> findByBitMask(final Class<E> enumType, final V value,
            final boolean mustMatchAllBit) {
        if (ObjectUtil.isAnyNull(enumType, value)) {
            return null;
        }

        if (!ClassUtil.isInterfaceImpl(enumType, Valuable.class)) {
            return null;
        }

        String key = enumType + ":(" + value.getClass() + ":" + value + ")" + (mustMatchAllBit == true ? "t" : "f");
        @SuppressWarnings("unchecked")
        List<E> result = (List<E>) COMPUTABLE2.get(key, new Callable<List<Enum<?>>>() {

            @Override
            public List<Enum<?>> call() throws Exception {
                Valuable<?>[] values = ReflectionUtil.invokeStaticMethod(enumType, "values");

                long allMask = 0L;
                for (Valuable<?> e : values) {
                    allMask += ConvertUtil.toLongValue(e.value());
                }

                long valueInPrimitive = ConvertUtil.toLongValue(value);
                if (valueInPrimitive > allMask) {
                    return Collections.emptyList();
                }

                List<Enum<?>> ret = new ArrayList<Enum<?>>(4);
                for (Valuable<?> elem : values) {
                    long elemInPrimitive = ConvertUtil.toLongValue(elem.value());
                    if ((valueInPrimitive & elemInPrimitive) > 0) {
                        ret.add((E) elem);
                        valueInPrimitive -= elemInPrimitive;
                    }
                }

                if (mustMatchAllBit && valueInPrimitive > 0) {
                    return Collections.emptyList();
                }

                if (CollectionUtil.isEmpty(ret)) {
                    return Collections.emptyList();
                }

                return ret;
            }
        });

        return result;
    }

    // transformer by the same name
    public static <F extends Enum<F>, T extends Enum<T>> T transformer(F from, Class<T> toClass) {
        if (ObjectUtil.isAnyNull(from, toClass)) {
            return null;
        }

        return Enum.valueOf(toClass, from.name());
    }

    public static <E extends Enum<E>> int size(final Class<E> enumType) {
        if (enumType == null) {
            return -1;
        }

        return enumType.getEnumConstants().length;
    }
}
